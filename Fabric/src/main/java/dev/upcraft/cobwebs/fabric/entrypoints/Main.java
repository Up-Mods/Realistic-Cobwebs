package dev.upcraft.cobwebs.fabric.entrypoints;

import dev.upcraft.cobwebs.RealisticCobwebs;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;

public class Main implements ModInitializer {

    @Override
    public void onInitialize() {
        RealisticCobwebs.init();
        UseBlockCallback.EVENT.register(RealisticCobwebs::onRightClickBlock);
    }
}
