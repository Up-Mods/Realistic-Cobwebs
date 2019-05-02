package nerdhub.cobwebs;

import nerdhub.cobwebs.config.ModConfig;
import nerdhub.cobwebs.util.ConfigHandler;
import nerdhub.cobwebs.util.ConfigReloader;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.block.Blocks;
import net.minecraft.block.CobwebBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class RealisticCobwebs implements ModInitializer {

    private static final Random random = new Random();

    @Override
    public void onInitialize() {
        ConfigHandler.registerConfig("cobwebs", ModConfig.class);
        ResourceManagerHelper.get(ResourceType.DATA).registerReloadListener(new ConfigReloader());
        UseBlockCallback.EVENT.register((playerEntity, world, hand, blockHitResult) -> {
            if(playerEntity.isSneaking() ||playerEntity.getStackInHand(hand).isEmpty())return ActionResult.PASS;
            ItemStack stack = playerEntity.getStackInHand(hand);
            Item item = stack.getItem();
            if(item == Item.getItemFromBlock(Blocks.TORCH)|| item == Items.FLINT_AND_STEEL){
                if(blockHitResult.getType() == HitResult.Type.BLOCK){
                    if(world.getBlockState(blockHitResult.getBlockPos()).getBlock() instanceof CobwebBlock){
                        if(world instanceof ServerWorld){
                            BlockPos webPos = blockHitResult.getBlockPos();
                            ServerWorld server = (ServerWorld) world;
                            server.spawnParticles(ParticleTypes.FLAME, webPos.getX() + 0.5D, webPos.getY() + 0.5D, webPos.getZ() + 0.5D, 7 + random.nextInt(40), random.nextDouble() * 0.5D, random.nextDouble() * 0.5D, random.nextDouble() * 0.5D,0.005);
                            world.clearBlockState(webPos, false);
                            if(item == Items.FLINT_AND_STEEL){
                                stack.applyDamage(1, playerEntity, (player) -> {  });
                            }else if(item == Items.TORCH && random.nextDouble()< ConfigHandler.getConfig(ModConfig.class).torchConsumeChance){
                                stack.subtractAmount(ConfigHandler.getConfig(ModConfig.class).flintAndSteelDamagePerUse);
                            }
                        }
                        return ActionResult.SUCCESS;
                    }
                }
            }
            return ActionResult.PASS;
        });

    }


}
