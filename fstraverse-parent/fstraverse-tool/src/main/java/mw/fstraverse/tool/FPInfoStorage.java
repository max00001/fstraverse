package mw.fstraverse.tool;

import java.io.File;
import java.util.Map;
import java.util.Set;

/**
 * interface to storage of processors results it should also keep reference to
 * parent File
 * 
 * @author maxwhite
 *
 */
public interface FPInfoStorage {
    public FProcResult get(String type);

    public FProcResult put(String type,
            FProcResult fProcResult);

    public Set<Map.Entry<String, FProcResult>> getIterator();

    public File getParentNode();

    public String getPrintableString();

}
