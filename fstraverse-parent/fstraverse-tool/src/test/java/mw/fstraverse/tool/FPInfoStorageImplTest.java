/**
 * 
 */
package mw.fstraverse.tool;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author maxwhite
 *
 */
public class FPInfoStorageImplTest {
    private File f = new File("d:/");
    private FPInfoStorage stor = new FPInfoStorageImpl(f);
    private FSProcessor fsp;
    private String fspType = "testType";
    private FProcResult fpr;
    private String printableForm = "bbb";
    

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        fsp = new FSProcessor() {

            @Override
            public FProcResult process(File file) {
                // method stub
                return new FProcResult() {

                    @Override
                    public void setAggregated() {
                        // nothing to do
                    }

                    @Override
                    public boolean isAggregated() {
                        return false;
                    }

                    @Override
                    public boolean isAggregatable() {
                        return false;
                    }

                    @Override
                    public String getPrintableForm() {
                        // TODO Auto-generated method stub
                        return printableForm;
                    }

                    @Override
                    public void aggregate(FProcResult childResult)
                            throws FSToolException {
                        // nothing

                    }
                };
            }
        };
        fpr = new FProcResult() {
            
            @Override
            public void setAggregated() {
                //nothing to implement
            }
            
            @Override
            public boolean isAggregated() {
                return false;
            }
            
            @Override
            public boolean isAggregatable() {
                return false;
            }
            
            @Override
            public String getPrintableForm() {
                return printableForm;
            }
            
            @Override
            public void aggregate(FProcResult childResult)
                    throws FSToolException {
                //nothing to implement
            }
        };
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link mw.fstraverse.tool.FPInfoStorageImpl#FPInfoStorageImpl()}.
     */
    @Test
    public void testFPInfoStorageImpl() {
        FPInfoStorage stor2 = new FPInfoStorageImpl();
        assertNull(stor2.getParentNode());
    }

    /**
     * Test method for {@link mw.fstraverse.tool.FPInfoStorageImpl#FPInfoStorageImpl(java.io.File)}.
     */
    @Test
    public void testFPInfoStorageImplFile() {
        assertEquals(f, stor.getParentNode());
    }


    /**
     * Test method for {@link mw.fstraverse.tool.FPInfoStorageImpl#put(java.lang.Class, mw.fstraverse.tool.FProcResult)}.
     */
    @Test
    public void testPut() { // the same as testGet, I removed testGet
        stor.put(fspType, fpr);
        assertEquals(fpr, stor.get(fspType));
    }

    /**
     * Test method for {@link mw.fstraverse.tool.FPInfoStorageImpl#getIterator()}.
     */
    @Test
    public void testGetIterator() {
        stor.put(fspType, fpr);
        Set<Map.Entry<String, FProcResult>> it = stor.getIterator();
        assertTrue(it.size() > 0);
    }

    /**
     * Test method for {@link mw.fstraverse.tool.FPInfoStorageImpl#getParentNode()}.
     */
    @Test
    public void testGetParentNode() {
        assertEquals(f, stor.getParentNode());;
    }

    /**
     * Test method for {@link mw.fstraverse.tool.FPInfoStorageImpl#getPrintableString()}.
     */
    @Test
    public void testGetPrintableString() {
        assertNotNull(stor.getPrintableString());
    }

}
