package mod.upcraftlp.cobwebs;

import mod.upcraftlp.cobwebs.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MODNAME, version = Reference.VERSION, acceptableRemoteVersions = "*")
public class Main {

	@SidedProxy(clientSide = Reference.CLIENT_PATH, serverSide = Reference.SERVER_PATH)
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}
}
