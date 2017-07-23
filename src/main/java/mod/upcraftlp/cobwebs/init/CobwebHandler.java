package mod.upcraftlp.cobwebs.init;

import java.util.Random;

import net.minecraft.block.BlockWeb;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CobwebHandler {

	@SubscribeEvent
	public void rightClickWeb(PlayerInteractEvent event) {
		if(event.getEntityPlayer().isSneaking() || event.getHand() == null || event.getFace() == null || event.getItemStack() == null || event.getItemStack().getItem() == null) return;
		ItemStack itemStack = event.getItemStack();
		if(itemStack.getItem() == Item.getItemFromBlock(Blocks.TORCH) || itemStack.getItem() == Items.FLINT_AND_STEEL) {
			World world = event.getEntityPlayer().worldObj;
			if(world.getBlockState(event.getPos()).getBlock() instanceof BlockWeb) {
				BlockPos webPos = event.getPos();
				Random random = new Random();
				for(int i = 0; i < 50; i++)	world.spawnParticle(EnumParticleTypes.FLAME, webPos.getX() + random.nextDouble(), webPos.getY() + random.nextDouble(), webPos.getZ() + random.nextDouble(), 0.005, 0.005, 0.005);
				world.setBlockToAir(webPos);
				if(itemStack.getItem() == Items.FLINT_AND_STEEL) {
					itemStack.damageItem(1, event.getEntityPlayer());
				}
				if(ModConfig.consumeTorches && !event.getEntityPlayer().capabilities.isCreativeMode &&itemStack.getItem() == Item.getItemFromBlock(Blocks.TORCH)) {
					itemStack.stackSize -= 1;
					if(itemStack.stackSize > 0) event.getEntityPlayer().setHeldItem(event.getHand(), itemStack); else event.getEntityPlayer().setHeldItem(event.getHand(), null);
				}
				event.setCanceled(true);
			}
		}
	}
	
}
