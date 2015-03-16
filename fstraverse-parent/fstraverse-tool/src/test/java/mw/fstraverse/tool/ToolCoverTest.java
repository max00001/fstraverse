package mw.fstraverse.tool;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;


public class ToolCoverTest {

    static final String testScenarioFileName = "./src/test/ext-resources/test-scenario.xml";
    
    ToolCover toolCover;
    FSPlugins mockFSPlugins;
    private File file;
    private File reportAbout;
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
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testInitScenario() {
        ToolCover toolCover;
        try {
            toolCover = new ToolCover(testScenarioFileName);
            Field field = toolCover.getClass().getDeclaredField("scenario");
            field.setAccessible(true);        
            ScenarioConfig scenario = (ScenarioConfig)field.get(toolCover); 
            if (scenario.getStep().size() == 0) {
                fail("No steps found in the scenario.");
            }
        } catch (FSToolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testRunScenario() {
        try {
            toolCover = new ToolCover(testScenarioFileName);
            
            Field field = toolCover.getClass().getDeclaredField("fsPlugins");
            field.setAccessible(true);
            mockFSPlugins = mock(FSPlugins.class);
            field.set(toolCover, mockFSPlugins); //implement mock
            
            //mock behavior
            when(mockFSPlugins.newFSProcessor(testType)).thenReturn(fsProcessor);
            when(mockFSPlugins.newFProcReport(testType)).thenReturn(fpReport);
            when(fsProcessor.process(any())).thenReturn(fpResult);
            when(fpResult.getPrintableForm()).thenReturn(printableFormOfResult);

            toolCover.runScenario();
            
            file = new File("d:/SampleDir");

            Method method;
            method = ToolCover.class
                    .getDeclaredMethod("getDirTree", File.class);
            method.setAccessible(true);
            dirTree = (FSInfoStorage) method.invoke(toolCover, file);

            
            
            
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
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FSToolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}
