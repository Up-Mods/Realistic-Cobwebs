package dev.upcraft.cobwebs;

import com.teamresourceful.resourcefulconfig.api.annotations.Config;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigEntry;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigInfo;

@ConfigInfo(
        title = "Realistic Cobwebs",
        description = "Make them cobwebs burn!",
        links = {
                @ConfigInfo.Link(
                        value = "https://modrinth.com/mod/KbSE3UgH",
                        icon = "modrinth",
                        text = "Modrinth"
                ),
                @ConfigInfo.Link(
                        value = "https://mods.cf/250393",
                        icon = "curseforge",
                        text = "Curseforge"
                ),
                @ConfigInfo.Link(
                        value = "https://github.com/Up-Mods/Realistic-Cobwebs",
                        icon = "github",
                        text = "Github"
                )
        }
)
@Config(RealisticCobwebs.MODID)
public final class RealisticCobwebsConfig {

    @ConfigEntry(id = "torch_consume_chance", translation = "config.realistic_cobwebs.torch_consume_chance")
    public static float torchConsumeChance = 0.03F;

    @ConfigEntry(id = "flint_and_steel_burst_enabled", translation = "config.realistic_cobwebs.flint_and_steel_burst_enabled")
    public static boolean flintAndSteelBurstEnabled = true;

    @ConfigEntry(id = "flint_and_steel_damage_per_use", translation = "config.realistic_cobwebs.flint_and_steel_damage_per_use")
    public static int flintAndSteelDamagePerUse = 1;

    @ConfigEntry(id = "fire_aspect_behavior", translation = "config.realistic_cobwebs.fire_aspect_behavior")
    public static FireAspectBehavior fireAspectBehavior = FireAspectBehavior.SINGLE;

    public enum FireAspectBehavior {
        SINGLE,
        BURST,
        DISABLED;

        boolean shouldDoBurst() {
            return this == BURST;
        }

        boolean shouldBurnCobwebs() {
            return this != DISABLED;
        }
    }
}
