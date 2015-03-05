package mw.fstraverse.plugin;

import static org.junit.Assert.*;
import mw.fstraverse.tool.FSProcessor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CountItemsPluginTest {
    
    private CountItemsPlugin countItemsPlugin;

    @Before
    public void setUp() throws Exception {
        countItemsPlugin = new CountItemsPlugin();
    }

    @After
    public void tearDown() throws Exception {
        
    }

    @Test
    public void testNewFSProcessor() {
        FSProcessor fsProcessor = countItemsPlugin.newFSProcessor();
        assertNotNull("COUNT fsProcessor is not created ", fsProcessor);
    }

}
