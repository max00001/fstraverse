package mw.fstraverse.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import mw.fstraverse.tool.FSPlugins.FS_PLUGIN_TYPE;

/**
 * application to start the tool
 *
 */
public class App {
    private static Logger logger = Logger.getLogger("mw.fstraverse");
    private static Properties props = new Properties();

    public static void main(String[] args) {
        loadProperties();
        try {
            FileHandler fh = new FileHandler(props.getProperty("log.location",
                    "d:/tmp/fs_traverse.log"));
            logger.setLevel(Level.FINE);
            fh.setLevel(Level.FINE);
            fh.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    // TODO Auto-generated method stub
                    return String.format("%d %s %n", record.getMillis(),
                            record.getMessage());
                }
            });
            logger.addHandler(fh);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ToolCover toolCover = new ToolCover();

        FSProcessor sampleFSProcessor = FSPlugins
                .newFSProcessor(FS_PLUGIN_TYPE.SAMPLE);
        FSProcessor sizeFSP = FSPlugins.newFSProcessor(FS_PLUGIN_TYPE.SIZE);
        FSProcessor countFSP = FSPlugins.newFSProcessor(FS_PLUGIN_TYPE.COUNT);

        File file = new File(props.getProperty("dir.to.traverse",
                "d:/SampleDir"));

        List<FSProcessor> list = new ArrayList<>(3);
        list.add(sampleFSProcessor);
        list.add(sizeFSP);
        list.add(countFSP);

        toolCover.traverse(file, list);
        
        toolCover.report(FS_PLUGIN_TYPE.COUNT, file);

        logger.info("Main complete");

    }

    private static void loadProperties() {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream("config.properties");
            props.load(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
