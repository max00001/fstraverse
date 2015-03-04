package mw.fstraverse.plugin;

import mw.fstraverse.tool.FProcReport;
import mw.fstraverse.tool.FSPlugin;
import mw.fstraverse.tool.FSProcessor;

public class SizePlugin implements FSPlugin {

    @Override
    public FSProcessor newFSProcessor() {
        return new SizeFSP();
    }

    @Override
    public FProcReport newFPReport() {
        // TODO Auto-generated method stub
        return null;
    }

}
