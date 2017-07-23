package mod.upcraftlp.cobwebs.proxy;

import mod.upcraftlp.cobwebs.init.CobwebHandler;
import mod.upcraftlp.cobwebs.init.ModConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event)
	{
		ModConfig.init(event);
		MinecraftForge.EVENT_BUS.register(new CobwebHandler());
	}
}
