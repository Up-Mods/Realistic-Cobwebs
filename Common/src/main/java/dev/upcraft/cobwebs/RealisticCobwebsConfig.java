package dev.upcraft.cobwebs;

import com.teamresourceful.resourcefulconfig.common.annotations.Config;
import com.teamresourceful.resourcefulconfig.common.annotations.ConfigEntry;
import com.teamresourceful.resourcefulconfig.common.config.EntryType;
import com.teamresourceful.resourcefulconfig.web.annotations.Link;
import com.teamresourceful.resourcefulconfig.web.annotations.WebInfo;

@WebInfo(
        title = "Realistic Cobwebs",
        description = "Make them cobwebs burn!",
        links = {
                @Link(
                        value = "https://modrinth.com/mod/KbSE3UgH",
                        icon = "modrinth",
                        title = "Modrinth"
                ),
                @Link(
                        value = "https://curseforge.com/projects/250393",
                        icon = "curseforge",
                        title = "Curseforge"
                ),
                @Link(
                        value = "https://github.com/Up-Mods/Realistic-Cobwebs",
                        icon = "github",
                        title = "Github")
        }
)
@Config(RealisticCobwebs.MODID)
public final class RealisticCobwebsConfig {

    @ConfigEntry(id = "torch_consume_chance", type = EntryType.FLOAT, translation = "config.realistic_cobwebs.torch_consume_chance")
    public static float torchConsumeChance = 0.03F;

    @ConfigEntry(id = "flint_and_steel_burst_enabled", type = EntryType.BOOLEAN, translation = "config.realistic_cobwebs.flint_and_steel_burst_enabled")
    public static boolean flintAndSteelBurstEnabled = true;

    @ConfigEntry(id = "flint_and_steel_damage_per_use", type = EntryType.INTEGER, translation = "config.realistic_cobwebs.flint_and_steel_damage_per_use")
    public static int flintAndSteelDamagePerUse = 1;
}
