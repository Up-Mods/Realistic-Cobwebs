package mod.upcraftlp.cobwebs.init;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModConfig {

	public static boolean consumeTorches;
	
	public static void init(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		/**CONFIG START**/
			consumeTorches = config.getBoolean("consumeTorches", Configuration.CATEGORY_GENERAL, true, "false to disable consumption of torches when burning cobwebs");
		
		
		
		
		
		/**CONFIG END**/
		config.save();
	}
}
