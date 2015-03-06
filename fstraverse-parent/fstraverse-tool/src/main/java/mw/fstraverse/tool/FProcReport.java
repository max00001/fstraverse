package mw.fstraverse.tool;

import java.io.File;

public interface FProcReport {
    public void aggregate();
    public String getPrintableString(FProcResult fProcResult);
    public String report(FSInfoStorage fsInfoStorage, File file, String outputFileName);
}
