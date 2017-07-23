package mod.upcraftlp.cobwebs;

import net.minecraftforge.common.config.Config;

public class Reference {

	public static final String MOD_ID = "cobwebs";
	public static final String MODNAME = "Realistic Cobwebs Mod";
	public static final String VERSION = "@VERSION@";
	public static final String MCVERSIONS = "[1.12,)";
	public static final String UPDATE_JSON = "https://raw.githubusercontent.com/UpcraftLP/Realistic-Cobwebs/master/Versions.json";

	@Config(modid = MOD_ID)
	@Config.LangKey(MOD_ID + ".config.title")
	public static class ModConfig {

		@Config.Comment("true to enable consumption of torches when burning cobwebs")
		public static boolean consumeTorches = false;

		@Config.Comment("Whether or not Cobwebs (and deco webs) should burn")
		public static boolean websBurn = true;
	}
}
