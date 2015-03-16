package mw.fstraverse.tool;

import java.io.File;

public interface FProcReport {
    public String report(FSInfoStorage fsInfoStorage, File file, 
            File reportabout, String outputFileName);
}
