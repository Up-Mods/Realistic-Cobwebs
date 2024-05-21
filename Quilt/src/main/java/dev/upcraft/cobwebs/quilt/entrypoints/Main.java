package dev.upcraft.cobwebs.quilt.entrypoints;

import dev.upcraft.cobwebs.RealisticCobwebs;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class Main implements ModInitializer {

    @Override
    public void onInitialize(ModContainer mod) {
        RealisticCobwebs.init();
        UseBlockCallback.EVENT.register(RealisticCobwebs::onRightClickBlock);
    }
}
