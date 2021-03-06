package mw.fstraverse.tool;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

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
    private FSPlugins fsPlugins; //for testing purposes this is a class member
    private ScenarioConfig scenario;
    
    
    // FSInfoStorage dirTree = new FSInfoStorageImpl();

    /**
     * 
     */
    public ToolCover(String filename) throws FSToolException {
        fsPlugins = FSPlugins.getInstance();
        initScenario(filename);
    }

    /**
     * 
     * runs a processor for dirTree
     * 
     * 
     * @param rootFile
     * @param fsProcessor
     * @return nothing
     */
    private void process(File rootFile, String type) {
        FSInfoStorage dirTree = getDirTree(rootFile);
        for (File file : dirTree.getFileIterator()) {
            FProcResult fpResult = fsPlugins.newFSProcessor(type).process(file);
            if (fpResult == null) {
                // do nothing in this case, this processor doesn't produce a
                // result
            } else {
                dirTree.putResult(file, type, fpResult);
            }
        }
        // Print the results to log - temporary for debug purposes
        printResults(dirTree);

    }
    
    private void aggregate(File rootFile, String type) {
        FSInfoStorage dirTree = storages.get(rootFile);
        if (dirTree == null) {
            logger.warning("cannot aggregate report " + type + " for " + rootFile
                    + " as there is no data storage for this directory.");
        } else {
            try {
                dirTree.aggregate(type);
            } catch (FSToolException e) {
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
            File[] files = rootFile.listFiles();
            if (files == null) {
                //exceptional case, do not add the directory into the tree
                logger.fine("addToDirTree - skipped " + rootFile.getName());
            } else {
                dirTree.put(rootFile, parentNode);

                for (File file : files) {
                    if (file.isDirectory() /*&& file.canRead() && !file.isHidden()*/) {
                        addToDirTree(dirTree, file, rootFile);
                    }
                }
            }

        } else {
            logger.warning("root file for ToolCover.addToDirTree is not a directory. Nothing to process.");
        }

    }


    private void report(File file, File reportabout, String type, String outputDir) {
        FSInfoStorage dirTree = storages.get(file);
        if (dirTree == null) {
            logger.warning("cannot compile report " + type + " for " + file
                    + " as there is no data storage for this directory.");
            logger.warning("Storage contains " + storages.size() + " trees.");
            for (File f : storages.keySet()) {
                logger.info("  for - " + f.getName());
            }
        } else {
            FProcReport fProcReport = fsPlugins.newFProcReport(
                    type);
            if (fProcReport != null) {
                String report = fProcReport.report(dirTree, file, reportabout, outputDir);
                logger.info("Report: " + report);
            } else {
                logger.warning("plugin-reporter is not defined for " + type);
            }
        }

    }
    
    private File getScenarioConfigFile(String filename) throws FSToolException {
        File file = null;
        if ((filename == null) || (filename.isEmpty())) {
            logger.info("Scenario filename is not provided via command line or config file.");
        } else {
            logger.info("Scenario loading. Filename: " + filename);
            file = new File(filename);
        }
        
        if ((file == null) || (!file.exists())) {
            logger.info("Loading default \"scenario.xml\"");
            file = new File("scenario.xml");
        }
        
        if (!file.exists()) {
            logger.info("Loading default \"src/main/ext-resources/scenario.xml\"");
            file = new File("src/main/ext-resources/scenario.xml");
        }
        if (!file.exists()) {
            FSToolException e = new FSToolException("Scenario config file cannot be found.");
            logger.throwing(this.getClass().getName(), "getScenarioConfigFile", e);
            throw e;
        }
        return file;
    }


    private void initScenario(String filename) throws FSToolException {
        File file = getScenarioConfigFile(filename);
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(ScenarioConfig.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            // jaxbUnmarshaller.setValidating(true);
            ScenarioConfig scenarioConfig = (ScenarioConfig) jaxbUnmarshaller
                    .unmarshal(file);
            scenario = scenarioConfig;

        } catch (JAXBException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }


    }
    
    public void runScenario() {
        if (scenario == null) {
            logger.warning("No scenario initialized");
        }
        List<ScenarioConfig.Step> steps = scenario.getStep();
        ConcurrentHashMap<String, Future<Boolean>> stepsStatus = new ConcurrentHashMap<>();
        
        ExecutorService es = Executors.newCachedThreadPool();
        
        for (Step step : steps) {
            String stepID = step.getStepID(); 
            File file = new File(step.getDirectory());
            File reportabout = (((step.getReportabout() == null) || 
                    (step.getReportabout().isEmpty())) ? 
                            null: new File(step.getReportabout()));
            
            String type = step.getPlugin();
            String outputDir = step.getOutput();
            String nextTo = step.getNextto();

            Callable<Boolean> stepCallable = new Callable<Boolean>() {

                @Override
                public Boolean call() throws Exception {
                    logger.info(step.toString() + " called.");

                    if ((nextTo != null) && (!nextTo.isEmpty())) {
                        //wait for the predecessor completion
                        Future<Boolean> previous = stepsStatus.get(nextTo);
                        if (previous != null) {
                            logger.info(step.toString() + " starts waiting for previous.");
                            previous.get(); //wait for completion
                            logger.info(step.toString() + " previous complete.");
                        } else {
                            logger.info(step.toString() + " previous is not found.");
                        }
                    }

                    logger.info(step.toString() + " excutions started.");

                    try {
                        switch (step.getAction()) {
                        case "Process":
                            process(file, type);
                            break;
                        case "Report":
                            report(file, reportabout, type, outputDir);
                            break;
                        case "Aggregate":
                            aggregate(file, type);
                            break;

                        default:
                            break;
                        }
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, e.getMessage(), e);
                    }
                    logger.info(toString() + " complete.");

                    return true;
                }
            };
            
            
            //TODO lock stepsStatus to start and register a task
            Future<Boolean> future = es.submit(stepCallable);
            stepsStatus.put(stepID, future);
            
        }
        
        es.shutdown();
        
        try {
            logger.info("waiting for completion");
            es.awaitTermination(1, TimeUnit.HOURS);
            logger.info("completion...");
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        
        for (Step step2 : steps) {
            try {
                logger.info(step2.toString() + " complete with result: " + stepsStatus.get(step2.getStepID()).get());
            } catch (InterruptedException e) {
                logger.warning(step2.toString() + " - interrupted.");
                logger.log(Level.WARNING, e.getMessage(), e);
            } catch (ExecutionException e) {
                logger.warning(step2.toString() + " - failed.");
                logger.log(Level.WARNING, e.getMessage(), e);
            }

        } 
        logger.info("executor service is completed");
    }

}
