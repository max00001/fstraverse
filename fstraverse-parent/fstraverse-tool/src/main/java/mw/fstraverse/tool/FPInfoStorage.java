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
    public FProcResult get(Class<? extends FSProcessor> cl);

    public FProcResult put(Class<? extends FSProcessor> cl,
            FProcResult fProcResult);

    public Set<Map.Entry<Class<? extends FSProcessor>, FProcResult>> getIterator();

    public File getParentNode();

    public String getPrintableString();

}
