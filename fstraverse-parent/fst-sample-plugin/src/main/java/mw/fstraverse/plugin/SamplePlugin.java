package mw.fstraverse.plugin;

import mw.fstraverse.tool.FProcReport;
import mw.fstraverse.tool.FSPlugin;
import mw.fstraverse.tool.FSProcessor;

public class SamplePlugin implements FSPlugin {

    @Override
    public FSProcessor newFSProcessor() {
        return new SampleFSProcessor();
    }

    @Override
    public FProcReport newFPReport(String type) {
        // TODO Auto-generated method stub
        return null;
    }

}
