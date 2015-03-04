package mw.fstraverse.plugin;

import java.io.File;

import mw.fstraverse.tool.FProcResult;
import mw.fstraverse.tool.FSProcessor;

public class CountItemsFSP implements FSProcessor {

    @Override
    public FProcResult process(File file) {
        if (file.isDirectory()) {
            // do nothing
            CountItemsFPR result = new CountItemsFPR();
            for (File everyFile : file.listFiles()) {
                if (everyFile.isDirectory()) {
                    result.incrementDirs();
                } else {
                    result.incrementFiles();
                }
            }
            return result;
            
        } else {
            return null;
        }
    }

}
