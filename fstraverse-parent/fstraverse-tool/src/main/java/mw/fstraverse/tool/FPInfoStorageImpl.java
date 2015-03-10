/**
 * 
 */
package mw.fstraverse.tool;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author maxwhite
 *
 */
public class FPInfoStorageImpl implements FPInfoStorage {

    
    private Map<String, FProcResult> resMap;
    private File parentNode;


    /**
     * 
     */
    public FPInfoStorageImpl() {
        super();
        parentNode = null;
        resMap = new ConcurrentHashMap<>();
    }

    
    public FPInfoStorageImpl(File parentNode) {
        super();
        this.parentNode = parentNode;
        resMap = new ConcurrentHashMap<>();
    }


    @Override
    public FProcResult get(String type) {
        return resMap.get(type);
    }


    @Override
    public FProcResult put(String type,
            FProcResult fProcResult) {
        return resMap.put(type, fProcResult);
    }


    @Override
    public Set<Entry<String, FProcResult>> getIterator() {
        return resMap.entrySet();
    }


    @Override
    public File getParentNode() {
        return parentNode;
    }


    @Override
    public String getPrintableString() {
        String result;
        if (parentNode == null) {
            result = "";
        } else {
            result = "\tParent node: \"" + parentNode.getName() + "\"\n";
        }
        for (Map.Entry<String, FProcResult> resultEntry : getIterator()) {
            result +=
                    
                    "\tProcessor: " + 
                    resultEntry.getKey() + 
                    " \tResult: " + resultEntry.getValue().getPrintableForm() + "\n";
        }
        return result;
    }
    
    

}
