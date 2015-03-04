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

    
    private Map<Class<? extends FSProcessor>, FProcResult> resMap;
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
    public FProcResult get(Class<? extends FSProcessor> cl) {
        return resMap.get(cl);
    }


    @Override
    public FProcResult put(Class<? extends FSProcessor> cl,
            FProcResult fProcResult) {
        return resMap.put(cl, fProcResult);
    }


    @Override
    public Set<Entry<Class<? extends FSProcessor>, FProcResult>> getIterator() {
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
        for (Map.Entry<Class<? extends FSProcessor>, FProcResult> resultEntry : getIterator()) {
            result +=
                    
                    "\tClass: " + 
                    resultEntry.getKey().getSimpleName() + 
                    " \tResult: " + resultEntry.getValue().getPrintableForm() + "\n";
        }
        return result;
    }
    
    

}
