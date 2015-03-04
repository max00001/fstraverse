package mw.fstraverse.tool;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import mw.fstraverse.tool.FSPlugins.FS_PLUGIN_TYPE;

/**
 * main class of the tool
 * use traverse methods to work with directories and processors
 * @author maxwhite
 *
 */
public class ToolCover {
    private Logger logger = Logger.getLogger(ToolCover.class.getPackage().getName());
    //private ConcurrentNavigableMap<File, Map<Class<? extends FSProcessor>, FProcResult>> dirTree = new ConcurrentSkipListMap<>(); 
    FSInfoStorage dirTree = new FSInfoStorageImpl();
        
    /**
     * traverses recursively through the root file if it's a directory and 
     * its sub-directories
     * starts the processor for each of discovered elements (directory)
     * 
     * @param rootFile
     * @param fsProcessor
     * @return nothing
     */
    public void traverse(File rootFile, FSProcessor fsProcessor) {
        // simple traversing - processing of each item if it's a directory
        if (rootFile.isDirectory()) {
            logger.fine("traversing " + rootFile.getName());
            FProcResult fpResult = fsProcessor.process(rootFile);
            if (fpResult != null) {
                logger.warning(fpResult.getPrintableForm());
            }
            for (File file : rootFile.listFiles()) {
                if (file.isDirectory()) {
                    traverse(file, fsProcessor);
                }
            }
            
        } else {
            logger.warning("root file for ToolCover.traverse is not a pointer "
                    + "to a directory. Nothing to process.");
        }
        
    }
    
    public void traverse(File rootFile, List<FSProcessor> fsProcessorList) {
        //DONE build tree
        buildDirTree(rootFile);
        //DONE run processors from the list one by one for the folder
        for (FSProcessor fsProcessor : fsProcessorList) {
            for (File file  : dirTree.getFileIterator()) {
                FProcResult fpResult = fsProcessor.process(file);
                if (fpResult == null) {
                    // do nothing in this case, this processor doesn't produce a result
                } else {
                    //entry.getValue().put(fsProcessor.getClass(), fpResult);
                    dirTree.putResult(file, fsProcessor.getClass(), fpResult);
                }
            }
        }
        
        //Print the results to log
        printResults();
        
        
        for (FSProcessor fsProcessor : fsProcessorList) {
            try {
                logger.info("aggregating for " + fsProcessor.getClass());
                dirTree.aggregate(fsProcessor.getClass());
            } catch (FSInfoStorageException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        logger.info("--aggregated state");
        printResults();
    }
    
    //TODO - too much info about storage internals, refactor it (almost done)
    private void printResults() {
        for (Map.Entry<File, FPInfoStorage> entry  : dirTree.getIterator()) {
            logger.info("Info from tree. File: " + entry.getKey().getName());
            logger.info(entry.getValue().getPrintableString());
        }

    }

    private void buildDirTree(File rootFile) {
        dirTree.clear();
        addToDirTree(rootFile, null);
    }

    private void addToDirTree(File rootFile, File parentNode) {
        if (rootFile.isDirectory()) {
            logger.fine("addToDirTree " + rootFile.getName());
            dirTree.put(rootFile, parentNode);
            for (File file : rootFile.listFiles()) {
                if (file.isDirectory()) {
                    addToDirTree(file, rootFile);
                }
            }
            
        } else {
            logger.warning("root file for ToolCover.addToDirTree is not a pointer "
                    + "to a directory. Nothing to process.");
        }
        
    }
    
//    private void aggregate(Class<? extends FSProcessor> cl) {
//        //results aggregator:
//        // traversing from leaf nodes
//        // adds child results to the parent result
//        
//        
//        dirTree.aggregate(cl);
//    }
    
    public void report(FS_PLUGIN_TYPE type, File file) {
        FProcReport fProcReport = FSPlugins.newFProcReport(type);
        if (fProcReport != null) {
            String report = fProcReport.report(dirTree, file);
            logger.info("Report: " + report);
        } else {
            logger.warning("plugin-reporter is not defined for " + type);
        }
                
    }
    
}
