package mod.upcraftlp.cobwebs;

import net.minecraft.block.BlockWeb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

import static mod.upcraftlp.cobwebs.Reference.*;

@Mod(modid = MOD_ID, name = MODNAME, version = VERSION, acceptedMinecraftVersions = MCVERSIONS, updateJSON = UPDATE_JSON, acceptableRemoteVersions = "*")
public class Main {

    @EventHandler
    public void init(FMLInitializationEvent event) {
        if (Reference.ModConfig.websBurn) {
            Blocks.FIRE.setFireInfo(Blocks.WEB, 15, 70);
        }
    }

    @Mod.EventBusSubscriber
    public static class WebHandler {

        private static final Random random = new Random();


        @SubscribeEvent
        public static void onRightClickWeb(PlayerInteractEvent.RightClickBlock event) {
            EntityPlayer player = event.getEntityPlayer();
            World world = event.getWorld();
            if (player.isSneaking() || event.getItemStack().isEmpty()) return;
            ItemStack stack = event.getItemStack();
            Item item = stack.getItem();
            if (item == Item.getItemFromBlock(Blocks.TORCH) || item == Items.FLINT_AND_STEEL) {
                BlockPos pos = event.getPos();
                if (world.getBlockState(pos).getBlock() instanceof BlockWeb) {
                    if (world instanceof WorldServer) {
                        BlockPos webPos = event.getPos();
                        WorldServer server = (WorldServer) world;
                        server.spawnParticle(EnumParticleTypes.FLAME, false, webPos.getX() + 0.5D, webPos.getY() + 0.5D, webPos.getZ() + 0.5D, 7 + random.nextInt(40), random.nextDouble() * 0.5D, random.nextDouble() * 0.5D, random.nextDouble() * 0.5D, 0.005);
                        world.setBlockToAir(webPos);
                        if (item == Items.FLINT_AND_STEEL) {
                            stack.damageItem(ModConfig.flintAndSteelDamage, event.getEntityPlayer());
                        } else if (random.nextDouble() < ModConfig.torchConsumeChance && !player.isCreative()) {
                            stack.shrink(1);
                        }
                    }
                    event.setCanceled(true);
                }
            }
        }
    }
}
