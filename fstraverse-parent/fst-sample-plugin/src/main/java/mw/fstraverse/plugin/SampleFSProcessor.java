package mw.fstraverse.plugin;

import java.io.File;
import java.util.logging.Logger;

import mw.fstraverse.tool.FProcResult;
import mw.fstraverse.tool.FSProcessor;

public class SampleFSProcessor implements FSProcessor {
    private Logger logger = Logger.getLogger(
            SampleFSProcessor.class.getPackage().getName());

    @Override
    public FProcResult process(File file) {
        logger.fine(file.getName());
        System.out.println("processed by SampleFP: " + file.getName());
        return null;
    }

}
