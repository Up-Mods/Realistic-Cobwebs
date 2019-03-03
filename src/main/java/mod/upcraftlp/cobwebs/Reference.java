package mod.upcraftlp.cobwebs;

import net.minecraftforge.common.config.Config;

public class Reference {

	public static final String MOD_ID = "cobwebs";
	public static final String MODNAME = "Realistic Cobwebs Mod";
	public static final String VERSION = "@VERSION@";
	public static final String MCVERSIONS = "[1.12,1.13)";
	public static final String UPDATE_JSON = "https://raw.githubusercontent.com/UpcraftLP/Realistic-Cobwebs/master/Versions.json";

	@Config(modid = MOD_ID)
	@Config.LangKey(MOD_ID + ".config.title")
	public static class ModConfig {

		@Config.Comment("Whether or not Cobwebs (and deco webs) should burn")
		public static boolean websBurn = true;

		@Config.Comment("Set Torch Consumption Chance")
		public static double torchConsumeChance = 3.0D;

		@Config.Comment("Flint and Steel Damage Cost")
		public static int flintAndSteelDamage = 1;
	}
}
