package mw.fstraverse.tool;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * @author maxwhite
 *
 */
public class FSPlugins {
    private static Logger logger = Logger.getLogger(FSPlugins.class
            .getPackage().getName());

    // singleton
    private static FSPlugins instance;

    class FSPluginInfo {
        private URL jarURL;
        private String pluginClass;

        public FSPluginInfo(String jarURL, String pluginClass) {
            super();
            try {
                this.jarURL = new URL(jarURL);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            this.pluginClass = pluginClass;
        }

        public URL getJarURL() {
            return jarURL;
        }

        public String getPluginClass() {
            return pluginClass;
        }

    }

    /**
     * map of information about available plugins from the config plugins.xml
     */
    private ConcurrentHashMap<String, FSPluginInfo> pluginsInfoMap = new ConcurrentHashMap<>();
    /**
     * map of loaded plugins
     */
    private ConcurrentHashMap<String, FSPlugin> pluginsMap = new ConcurrentHashMap<>();

    private FSPlugins() {
        initPluginsList();
    }

    private void initPluginsList() {
        // TODO load xml list of plugins
        File file = new File("D:/tmp/plugins.xml");
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(PluginsConfig.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            // jaxbUnmarshaller.setValidating(true);
            PluginsConfig plugins = (PluginsConfig) jaxbUnmarshaller
                    .unmarshal(file);

            List<PluginsConfig.Plugin> pluginList = plugins.getPlugin();

            for (PluginsConfig.Plugin plugin : pluginList) {
                pluginsInfoMap.put(
                        plugin.getId(),
                        new FSPluginInfo(plugin.getJarURI(), plugin
                                .getPluginClass()));
            }
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // write to pluginsInfoMap

    }

    public static synchronized FSPlugins getInstance() {
        if (instance == null) {
            instance = new FSPlugins();
        }
        return instance;
    }

    public FSProcessor newFSProcessor(String type) {
        return loadPlugin(type).newFSProcessor();
        // FSProcessor fsProcessor = loadPlugin(type).newFSProcessor();
        // if (fsProcessor == null) {
        // logger.severe("fsProcessor is null for " + type);
        // }
        // return fsProcessor;
    }

    public FProcReport newFProcReport(String type) {
        return loadPlugin(type).newFPReport();
        // FSPlugin plugin = loadPlugin(type);
        // if (plugin == null) {
        // logger.severe("Plugin is not available for " + type);
        // } else {
        // logger.info("Requested report-object for " + type +
        // ". Plugin available. " + plugin.getClass());
        // }
        //
        // FProcReport fProcReport = plugin.newFPReport();
        // if (fProcReport == null) {
        // logger.severe("fProcReport is null for " + type);
        // }
        // return fProcReport;
    }

    private FSPlugin loadPlugin(String type) {
        FSPlugin plugin = pluginsMap.get(type);
        if (plugin != null) {
            return plugin;
        } // else continue loading...
        ClassLoader loader = URLClassLoader.newInstance(
                new URL[] { getJarURL(type) }, FSPlugin.class.getClassLoader());
        Class<?> cl;
        try {
            cl = Class.forName(getPluginClassName(type), true, loader);
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

    private URL getJarURL(String type) {
        return pluginsInfoMap.get(type).getJarURL();
    }

    private String getPluginClassName(String type) {
        return pluginsInfoMap.get(type).getPluginClass();
    }

}
