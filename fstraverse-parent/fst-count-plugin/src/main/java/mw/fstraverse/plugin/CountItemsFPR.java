/**
 * 
 */
package mw.fstraverse.plugin;

import mw.fstraverse.tool.FProcResult;

/**
 * @author maxwhite
 *
 */
public class CountItemsFPR implements FProcResult {
    private int files;
    private int dirs;
    private boolean aggregated = false;
    
    /* (non-Javadoc)
     * @see mw.fstraverse.main.FProcResult#getPrintableForm()
     */
    @Override
    public String getPrintableForm() {
        // TODO Auto-generated method stub
        return "Sub-directories: " + dirs + " Files: " + files + 
                (isAggregated()?" a":"");
    }
    
    public void incrementFiles() {
        files++;
    }
    
    public void incrementDirs() {
        dirs++;
    }
    

    public int getFiles() {
        return files;
    }

    public void setFiles(int files) {
        this.files = files;
    }

    public int getDirs() {
        return dirs;
    }

    public void setDirs(int dirs) {
        this.dirs = dirs;
    }

    @Override
    public boolean isAggregated() {
        return aggregated;
    }

    @Override
    public boolean isAggregatable() {
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
    public void aggregate(FProcResult childResult) {
        if (childResult instanceof CountItemsFPR) {
            dirs += ((CountItemsFPR)childResult).getDirs();
            files += ((CountItemsFPR)childResult).getFiles();
            childResult.setAggregated();
        } else {
            //TODO ?exception
        }
        
        
    }
    
    
    

}
