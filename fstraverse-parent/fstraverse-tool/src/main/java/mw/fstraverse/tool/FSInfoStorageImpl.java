package mw.fstraverse.tool;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * saves data about traversed file system file (some tree-like structure) and
 * mapping to results list to keep results from processors
 * 
 * @author maxwhite
 *
 */
public class FSInfoStorageImpl implements FSInfoStorage {
    private ConcurrentNavigableMap<File, FPInfoStorage> dirTree;

    public FSInfoStorageImpl() {
        super();
        this.dirTree = new ConcurrentSkipListMap<>();
    }

    @Override
    public Set<Map.Entry<File, FPInfoStorage>> getIterator() {
        return dirTree.entrySet();
    }

    @Override
    public Set<File> getDescFileIterator() {
        return dirTree.descendingKeySet();
    }

    @Override
    public FPInfoStorage put(File file, File parentNode) {
        FPInfoStorage value = new FPInfoStorageImpl(parentNode);
        return dirTree.put(file, value);
    }

    @Override
    public FProcResult putResult(File file, String type,
            FProcResult fProcResult) {
        FPInfoStorage value = dirTree.get(file);
        return value.put(type, fProcResult);
    }

    @Override
    public void clear() {
        dirTree.clear();
    }

    @Override
    public FPInfoStorage get(File file) {
        // TODO Auto-generated method stub
        return dirTree.get(file);
    }

    @Override
    public Set<File> getFileIterator() {
        return dirTree.keySet();
    }

    @Override
    public File getRootFile() {
        return dirTree.firstKey();
    }

    @Override
    public void aggregate(String type) throws FSInfoStorageException {
        for (Map.Entry<File, FPInfoStorage> entry : dirTree.descendingMap()
                .entrySet()) {
            FPInfoStorage value = entry.getValue();
            if (value.getParentNode() != null) {
                FProcResult childResult = value.get(type);
                if (childResult != null) {
                    FProcResult parentResult = dirTree.get(
                            value.getParentNode()).get(type);
                    if (parentResult == null) {
                        // TODO ?exception
                        throw new FSInfoStorageException("Parent result of "
                                + type + " is not found for node "
                                + entry.getKey().getName());
                    } else {
                        parentResult.aggregate(childResult);
                    }

                }

            } else {
                // nothing to do, no parent
            }
        }

    }

}
