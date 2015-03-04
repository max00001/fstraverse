package mw.fstraverse.tool;

import java.io.File;
import java.util.Map;
import java.util.Set;

public interface FSInfoStorage {

    public Set<Map.Entry<File, FPInfoStorage>> getIterator();
    public Set<File> getDescFileIterator();
    public Set<File> getFileIterator();
    public FPInfoStorage put(File file, File parentNode);
    public FProcResult putResult(File file, Class<? extends FSProcessor> cl, FProcResult fProcResult);
    public FPInfoStorage get(File file);
    public void clear();
    public File getRootFile();
    public void aggregate(Class<? extends FSProcessor> cl) throws FSInfoStorageException;
}
