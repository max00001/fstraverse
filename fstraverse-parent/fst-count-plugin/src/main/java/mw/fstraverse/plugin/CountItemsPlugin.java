package mw.fstraverse.plugin;

//TODO remove commented logger
//import java.util.logging.Logger;

import mw.fstraverse.tool.FProcReport;
import mw.fstraverse.tool.FSPlugin;
import mw.fstraverse.tool.FSProcessor;

public class CountItemsPlugin implements FSPlugin {

//    private static Logger logger = Logger.getLogger(FSPlugin.class
//            .getPackage().getName());
    
    @Override
    public FSProcessor newFSProcessor() {
//        logger.info("new FSProcessor for COUNT");
        return new CountItemsFSP();
    }

    @Override
    public FProcReport newFPReport(String type) {
//        logger.info("new FProcReport for COUNT");
        return new CountItemsReport(type);
    }

}
