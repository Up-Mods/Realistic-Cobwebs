package dev.upcraft.cobwebs.fabric.entrypoints;

import com.teamresourceful.resourcefulconfig.client.ConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.upcraft.cobwebs.RealisticCobwebs;
import dev.upcraft.cobwebs.RealisticCobwebsConfig;

public class Modmenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new ConfigScreen(parent, null, RealisticCobwebs.CONFIGURATOR.getConfig(RealisticCobwebsConfig.class));
    }
}
