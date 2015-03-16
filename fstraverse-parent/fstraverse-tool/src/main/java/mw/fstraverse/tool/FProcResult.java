package mw.fstraverse.tool;

public interface FProcResult {
    //result of processor (FSProcessor) activity
    public String getPrintableForm();
    /**
     * @return true if info about this result already aggregated to the parent node
     */
    public boolean isAggregated();
    public void setAggregated();
    public boolean isAggregatable();
    public void aggregate(FProcResult childResult) throws FSToolException;
}
