package mw.fstraverse.tool;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import mw.fstraverse.tool.FSPlugins.FSPluginInfo;
import mw.fstraverse.tool.ScenarioConfig.Step;

/**
 * main class of the tool 
 * reads scenario.xml config
 * runs the commands from it
 * 
 * @author maxwhite
 *
 */
public class ToolCover {
    private static Logger logger = Logger.getLogger(ToolCover.class
            .getPackage().getName());
    private ConcurrentHashMap<File, FSInfoStorage> storages = new ConcurrentHashMap<>();

    // FSInfoStorage dirTree = new FSInfoStorageImpl();

    /**
     * 
     * runs a processor for dirTree
     * 
     * 
     * @param rootFile
     * @param fsProcessor
     * @return nothing
     */
    public void process(File rootFile, FSProcessor fsProcessor) {
        FSInfoStorage dirTree = getDirTree(rootFile);
        for (File file : dirTree.getFileIterator()) {
            FProcResult fpResult = fsProcessor.process(file);
            if (fpResult == null) {
                // do nothing in this case, this processor doesn't produce a
                // result
            } else {
                dirTree.putResult(file, fsProcessor.getClass(), fpResult);
            }
        }
        // Print the results to log - temporary for debug purposes
        printResults(dirTree);

    }
    
    public void aggregate(File rootFile, FSProcessor fsProcessor) {
        FSInfoStorage dirTree = storages.get(rootFile);
        if (dirTree == null) {
            logger.warning("cannot aggregate report " + fsProcessor + " for " + rootFile
                    + " as there is no data storage for this directory.");
        } else {
            try {
                dirTree.aggregate(fsProcessor.getClass());
            } catch (FSInfoStorageException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        
    }


    // TODO - too much info about storage internals, refactor it (almost done)
    private void printResults(FSInfoStorage dirTree) {
        for (Map.Entry<File, FPInfoStorage> entry : dirTree.getIterator()) {
            logger.info("Info from tree. File: " + entry.getKey().getName());
            logger.info(entry.getValue().getPrintableString());
        }

    }

    private FSInfoStorage getDirTree(File rootFile) {
        FSInfoStorage dirTree = storages.get(rootFile);
        if (dirTree == null) {
            dirTree = new FSInfoStorageImpl();
            addToDirTree(dirTree, rootFile, null);
            storages.put(rootFile, dirTree);
        }
        return dirTree;
    }

    private void addToDirTree(FSInfoStorage dirTree, File rootFile,
            File parentNode) {
        if (rootFile.isDirectory()) {
            logger.fine("addToDirTree " + rootFile.getName());
            dirTree.put(rootFile, parentNode);
            for (File file : rootFile.listFiles()) {
                if (file.isDirectory()) {
                    addToDirTree(dirTree, file, rootFile);
                }
            }

        } else {
            logger.warning("root file for ToolCover.addToDirTree is not a pointer "
                    + "to a directory. Nothing to process.");
        }

    }


    public void report(File file, String type, String outputDir) {
        FSInfoStorage dirTree = storages.get(file);
        if (dirTree == null) {
            logger.warning("cannot compile report " + type + " for " + file
                    + " as there is no data storage for this directory.");
            logger.warning("Storage contains " + storages.size() + " trees.");
            for (File f : storages.keySet()) {
                logger.info("  for - " + f.getName());
            }
        } else {
            FProcReport fProcReport = FSPlugins.getInstance().newFProcReport(
                    type);
            if (fProcReport != null) {
                String report = fProcReport.report(dirTree, file, outputDir);
                logger.info("Report: " + report);
            } else {
                logger.warning("plugin-reporter is not defined for " + type);
            }
        }

    }
    
    private File getScenarioConfigFile() {
        File file = new File("scenario.xml");
        if (!file.exists()) {
            file = new File("src/main/ext-resources/scenario.xml");
        }
        if (!file.exists()) {
            //TODO exception
            logger.warning("Scenario config file doesn't exist.");
        }
        return file;
    }


    public ScenarioConfig initScenario() {
        // TODO load correct file
        File file = getScenarioConfigFile();
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(ScenarioConfig.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            // jaxbUnmarshaller.setValidating(true);
            ScenarioConfig scenario = (ScenarioConfig) jaxbUnmarshaller
                    .unmarshal(file);
            return scenario;

        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // if it is not returned before
        return null;

    }
    
    public void runScenario(ScenarioConfig scenario) {
        List<ScenarioConfig.Step> steps = scenario.getStep();
        for (Step step : steps) {
            String stepID = step.getStepID(); //TODO need to track status of the step to check the sequence - nextto
            File file = new File(step.getDirectory());
            String type = step.getPlugin();
            String outputDir = step.getOutput(); //TODO apply for reporting
            
            switch (step.getAction()) {
            case "Process":
                process(file, FSPlugins.getInstance().newFSProcessor(type));
                break;
            case "Report":
                report(file, type, outputDir);
                break;
            case "Aggregate":
                aggregate(file, FSPlugins.getInstance().newFSProcessor(type));
                break;

            default:
                break;
            }
        }
    }

}
