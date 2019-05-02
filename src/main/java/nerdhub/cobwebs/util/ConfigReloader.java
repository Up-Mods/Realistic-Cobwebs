package nerdhub.cobwebs.util;

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class ConfigReloader implements SimpleSynchronousResourceReloadListener {

    private static final Identifier ID = new Identifier("cobwebs", "config_reloader");

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public void apply(ResourceManager var1) {
        ConfigHandler.getRegisteredConfigs().forEach(ConfigHandler::reloadConfig);
    }
}
