/**
 * 
 */
package mw.fstraverse.plugin;

import java.io.File;

import mw.fstraverse.tool.FSProcessor;

/**
 * @author maxwhite
 *
 */
public class SizeFSP implements FSProcessor {

    /* (non-Javadoc)
     * @see mw.fstraverse.main.FSProcessor#process(java.io.File)
     */
    @Override
    public SizeFPR process(File file) {
        if (file.isDirectory()) {
            // do nothing
            SizeFPR result = new SizeFPR(file.length());
            for (File everyFile : file.listFiles()) {
                if (!everyFile.isDirectory()) {
                    result.addSize(everyFile.length());
                }
            }
            return result;
            
        } else {
            return new SizeFPR(file.length());
        }
    }

}
