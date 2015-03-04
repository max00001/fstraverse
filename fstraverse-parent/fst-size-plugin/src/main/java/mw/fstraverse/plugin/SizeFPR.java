package mw.fstraverse.plugin;

import mw.fstraverse.tool.FProcResult;
import mw.fstraverse.tool.FSInfoStorageException;

public class SizeFPR implements FProcResult {
    private long size;
    private boolean aggregated;

    public SizeFPR(long size) {
        super();
        setSize(size);
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
    
    public void addSize(long size) {
        this.size += size;
    }

    @Override
    public String getPrintableForm() {
        return "Size of this item is " + size + (isAggregated()?" a":"");
    }

    @Override
    public boolean isAggregated() {
        // TODO Auto-generated method stub
        return aggregated;
    }

    @Override
    public boolean isAggregatable() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void setAggregated() {
        if (isAggregatable()) {
            aggregated = true;
        } else {
            //TODO nothing to change, probably it should throw an exception
        }

        
    }

    @Override
    public void aggregate(FProcResult childResult) throws FSInfoStorageException {
        if (childResult instanceof SizeFPR) {
            size += ((SizeFPR)childResult).getSize();
            childResult.setAggregated();
        } else {
            //TODO ?exception
            throw new FSInfoStorageException("Size aggregation failed.");
        }
        
    }
    
}
