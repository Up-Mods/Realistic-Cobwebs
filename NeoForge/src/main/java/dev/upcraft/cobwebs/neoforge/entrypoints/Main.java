package dev.upcraft.cobwebs.neoforge.entrypoints;

import dev.upcraft.cobwebs.RealisticCobwebs;
import net.neoforged.fml.common.Mod;

@Mod(RealisticCobwebs.MODID)
public class Main {

    public Main() {
        RealisticCobwebs.init();
    }
}
