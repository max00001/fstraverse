package mw.fstraverse.tool;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Logger;

public class FSPlugins {
    private static Logger logger = Logger.getLogger(FSPlugins.class
            .getPackage().getName());
    
    public enum FS_PLUGIN_TYPE {
        SAMPLE("file:fst-sample-plugin.jar",
                "mw.fstraverse.plugin.SamplePlugin"), 
        SIZE("file:fst-size-plugin.jar", 
                "mw.fstraverse.plugin.SizePlugin"), 
        COUNT("file:fst-count-plugin.jar", 
                "mw.fstraverse.plugin.CountItemsPlugin");

        private URL jarURL;
        private String pluginClassName;

        FS_PLUGIN_TYPE(String url, String pluginClassName) {
            try {
                this.jarURL = new URL(url);
                this.pluginClassName = pluginClassName;
            } catch (MalformedURLException e) {
                logger.severe(String.format("FSPlugins failure: %s - %s",
                        this, Arrays.toString(e.getStackTrace())));
            }

        }

        public URL getJarURL() {
            return jarURL;
        }

        public String getPluginClassName() {
            return pluginClassName;
        }

    };
    
    private static Map<FS_PLUGIN_TYPE, FSPlugin> pluginsMap=
             Collections.synchronizedMap(new EnumMap<FS_PLUGIN_TYPE, FSPlugin>(FS_PLUGIN_TYPE.class));
    
    private FSPlugins() {
        // TODO Auto-generated constructor stub
    }

    public static FSProcessor newFSProcessor(FS_PLUGIN_TYPE type) {
        return loadPlugin(type).newFSProcessor();
//        FSProcessor fsProcessor = loadPlugin(type).newFSProcessor();
//        if (fsProcessor == null) {
//            logger.severe("fsProcessor is null for " + type);
//        }
//        return fsProcessor;
    }
    
    public static FProcReport newFProcReport(FS_PLUGIN_TYPE type) {
        return loadPlugin(type).newFPReport();
//        FSPlugin plugin = loadPlugin(type);
//        if (plugin == null) {
//            logger.severe("Plugin is not available for " + type);
//        } else {
//            logger.info("Requested report-object for " + type + ". Plugin available. " + plugin.getClass());
//        }
//        
//        FProcReport fProcReport = plugin.newFPReport();
//        if (fProcReport == null) {
//            logger.severe("fProcReport is null for " + type);
//        }
//        return fProcReport;
    }

    private static FSPlugin loadPlugin(FS_PLUGIN_TYPE type) {
        FSPlugin plugin = pluginsMap.get(type);
        if (plugin != null) {
            return plugin;
        } // else continue loading...
        ClassLoader loader = URLClassLoader.newInstance(
                new URL[] { type.getJarURL() },
                FSPlugin.class.getClassLoader());
        Class<?> cl;
        try {
            cl = Class.forName(type.getPluginClassName(), true, loader);
            Class<? extends FSPlugin> fsPluginClass = cl
                    .asSubclass(FSPlugin.class);
            // do not use Class.newInstance
            Constructor<? extends FSPlugin> ctor = fsPluginClass
                    .getConstructor();
            plugin = ctor.newInstance();
            pluginsMap.put(type, plugin);
            logger.info("Added plugin for " + type);
            return plugin;
        } catch (ClassNotFoundException e) {
            logException(e);
        } catch (NoSuchMethodException e) {
            logException(e);
        } catch (SecurityException e) {
            logException(e);
        } catch (InstantiationException e) {
            logException(e);
        } catch (IllegalAccessException e) {
            logException(e);
        } catch (IllegalArgumentException e) {
            logException(e);
        } catch (InvocationTargetException e) {
            logException(e);
        }
        logger.warning("Plugin is not available for " + type);
        return null;
    }

    private static void logException(Throwable e) {
        logger.severe(String.format(
                "FSPlugins loadPlugin failure: %s %s -- %s", e.getClass(),
                e.getMessage(), Arrays.toString(e.getStackTrace())));
    }

}
