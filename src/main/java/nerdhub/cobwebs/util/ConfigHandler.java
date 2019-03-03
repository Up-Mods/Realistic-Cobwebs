package nerdhub.cobwebs.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;


public class ConfigHandler {

    private static final Map<Class<?>, Object> CONFIG_OBJECTS = new IdentityHashMap<>();
    private static final Map<Class<?>, String> CONFIG_ID_LOOKUP = new IdentityHashMap<>();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger logger = LogManager.getLogger("cobwebs");

    @SuppressWarnings("unchecked")
    public static <T> T getConfig(Class<T> clazz) {
        if(!CONFIG_OBJECTS.containsKey(clazz)) {
            throw new IllegalStateException("config not registered before accessing: " + clazz.getCanonicalName());
        }
        return (T) CONFIG_OBJECTS.get(clazz);
    }

    /**
     * @param configName the filename of the config (without .json extension)
     */
    public static <T> T registerConfig(String configName, Class<T> configClass) {
        CONFIG_ID_LOOKUP.put(configClass, configName + ".json");
        return reloadConfig(configClass);
    }

    public static <T> T reloadConfig(Class<T> configClass) {
        File configFile = new File(FabricLoader.getInstance().getConfigDirectory(), CONFIG_ID_LOOKUP.get(configClass));
        if(!configFile.exists()) {
            try(FileWriter writer = new FileWriter(configFile)) {
                gson.toJson(configClass.newInstance(), writer);
            }
            catch (IOException | IllegalAccessException | InstantiationException e) {
                logger.error("unable to write config file for {}", configClass.getCanonicalName());
                logger.trace("file location: " + configFile.getAbsolutePath(), e);
            }
        }
        T config;
        try(FileReader reader = new FileReader(configFile)) {
            config = gson.fromJson(reader, configClass);
        }
        catch (IOException e) {
           logger.error("unable to read config file from " + configFile.getAbsolutePath() + ", falling back to default values!", e);
            try {
                config = configClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e1) {
                throw new RuntimeException(e1);
            }
        }
        CONFIG_OBJECTS.put(configClass, config);
        return config;
    }

    public static Set<Class<?>> getRegisteredConfigs() {
        return CONFIG_OBJECTS.keySet();
    }
}
