package mw.fstraverse.tool;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;


public class ToolCoverTest {

    ToolCover toolCover;
    FSPlugins mockFSPlugins;
    private File file;
    private String printableFormOfResult = "abc";
    FSInfoStorage dirTree;
    String testType = "Test"; //plugin type for tests
  /*  FSProcessor fsProcessor = new FSProcessor() {

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
                    return printableFormOfResult;
                }

                @Override
                public void aggregate(FProcResult childResult)
                        throws FSInfoStorageException {
                    // nothing

                }
            };
        }
    };*/
    FSProcessor fsProcessor = mock(FSProcessor.class);
    FProcReport fpReport = mock(FProcReport.class);
    FProcResult fpResult = mock(FProcResult.class);
    
    @Before
    public void setUp() throws Exception {
        try {
            toolCover = new ToolCover();
            
            Field field = toolCover.getClass().getDeclaredField("fsPlugins");
            field.setAccessible(true);
            mockFSPlugins = mock(FSPlugins.class);
            field.set(toolCover, mockFSPlugins); //implement mock
            
            //mock behavior
            when(mockFSPlugins.newFSProcessor(testType)).thenReturn(fsProcessor);
            when(mockFSPlugins.newFProcReport(testType)).thenReturn(fpReport);
            when(fsProcessor.process(any())).thenReturn(fpResult);
            when(fpResult.getPrintableForm()).thenReturn(printableFormOfResult);

            
            file = new File(".");
            toolCover.process(file, testType);

            Method method;
            method = ToolCover.class
                    .getDeclaredMethod("getDirTree", File.class);
            method.setAccessible(true);
            dirTree = (FSInfoStorage) method.invoke(toolCover, file);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testInitScenario() {
        ToolCover toolCover = new ToolCover();
        ScenarioConfig scenario = toolCover.initScenario();
        if (scenario.getStep().size() == 0) {
            fail("No steps found in the scenario.");
        }
    }

    @Test
    public void testProcess() {
        try {
            assertTrue(dirTree.getIterator().size() > 0);
            assertEquals(file, dirTree.getRootFile());
            Set<Entry<String, FProcResult>> rrr = dirTree
                    .get(file).getIterator();
            String ttt = rrr.iterator().next().getValue().getPrintableForm();
            assertEquals(printableFormOfResult, ttt);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Test
    public void testAggregate() {
        toolCover.aggregate(file, testType);
        //nothing to assert in this implementation. Test just simple run without checks
        //verify(sa.agg) - check if aggregate method of dirTree is called
    }

    @Test
    public void testReport() {
        toolCover.report(file, testType, System.getProperty("java.io.tmpdir"));
        fail("Not yet implemented");
    }

    @Test
    public void testRunScenario() {
        fail("Not yet implemented");
    }

}
