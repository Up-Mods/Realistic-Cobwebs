package mod.upcraftlp.cobwebs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWeb;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

import static mod.upcraftlp.cobwebs.Reference.*;

@Mod(modid = MOD_ID, name = MODNAME, version = VERSION, acceptedMinecraftVersions = MCVERSIONS, updateJSON = UPDATE_JSON)
public class Main {

	@EventHandler
	public void init(FMLInitializationEvent event) {
		if(Reference.ModConfig.websBurn) {
			Blocks.FIRE.setFireInfo(Blocks.WEB, 15, 70);
			Blocks.FIRE.setFireInfo(ModThings.DECO_WEB, 15, 70);
		}
	}

	@GameRegistry.ObjectHolder(MOD_ID)
	public static class ModThings {

		public static final Block DECO_WEB = null;

	}

	@Mod.EventBusSubscriber
	public static class WebHandler {

		public static final Random random = new Random();

		@SubscribeEvent
		public static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
			event.getRegistry().register(new BlockDecoWeb());
		}

		@SubscribeEvent
		public static void onRegisterItems(RegistryEvent.Register<Item> event) {
			event.getRegistry().register(new ItemBlock(ModThings.DECO_WEB).setRegistryName(ModThings.DECO_WEB.getRegistryName()));
		}

		@SideOnly(Side.CLIENT)
		@SubscribeEvent
		public static void onRegisterModels(ModelRegistryEvent event) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModThings.DECO_WEB), 0, new ModelResourceLocation(ModThings.DECO_WEB.getRegistryName(), "inventory"));
		}

		@SubscribeEvent
		public static void onRightClickWeb(PlayerInteractEvent.RightClickBlock event) {
			EntityPlayer player = event.getEntityPlayer();
			World world = event.getWorld();
			if(player.isSneaking() || event.getItemStack().isEmpty()) return;
			ItemStack stack = event.getItemStack();
			Item item = stack.getItem();
			if(item == Item.getItemFromBlock(Blocks.TORCH) || item == Items.FLINT_AND_STEEL) {
				BlockPos pos = event.getPos();
				if(world.getBlockState(pos).getBlock() instanceof BlockWeb) {
					if(world instanceof WorldServer) {
						BlockPos webPos = event.getPos();
						WorldServer server = (WorldServer) world;
						server.spawnParticle(EnumParticleTypes.FLAME, false, webPos.getX() + 0.5D, webPos.getY() + 0.5D, webPos.getZ() + 0.5D, 7 + random.nextInt(40), random.nextDouble() * 0.5D, random.nextDouble() * 0.5D, random.nextDouble() * 0.5D,0.005);
						world.setBlockToAir(webPos);
						if(item == Items.FLINT_AND_STEEL) {
							stack.damageItem(1, event.getEntityPlayer());
						}
						else if(ModConfig.consumeTorches && !player.isCreative()) {
							stack.shrink(1);
						}
					}
					event.setCanceled(true);
				}
			}
		}
	}
}
