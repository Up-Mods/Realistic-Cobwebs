package dev.upcraft.cobwebs;

import com.teamresourceful.resourcefulconfig.common.config.Configurator;
import dev.upcraft.cobwebs.util.WorldScheduler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class RealisticCobwebs {

    public static final String MODID = "realistic_cobwebs";

    public static final Configurator CONFIGURATOR = new Configurator();

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MODID, path);
    }

    public static void init() {
        CONFIGURATOR.registerConfig(RealisticCobwebsConfig.class);
    }

    public static InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown() || stack.isEmpty()) {
            return InteractionResult.PASS;
        }

        BlockPos startPos = hitResult.getBlockPos();
        if (level.getBlockState(startPos).is(RealisticCobwebsTags.COBWEBS)) {
            var isBurst = stack.is(RealisticCobwebsTags.BURST_TRIGGERS);
            if (isBurst || stack.is(RealisticCobwebsTags.TORCHES)) {
                if (level instanceof ServerLevel serverLevel) {
                    burnBlock(serverLevel, startPos, serverLevel.getRandom(), isBurst && RealisticCobwebsConfig.flintAndSteelBurstEnabled);
                    if (!player.isCreative()) {
                        if(stack.getMaxDamage() > 0) {
                            stack.hurtAndBreak(RealisticCobwebsConfig.flintAndSteelDamagePerUse, player, (player1) -> player1.broadcastBreakEvent(hand));
                        }
                        else if(player.getRandom().nextFloat() < RealisticCobwebsConfig.torchConsumeChance) {
                            stack.shrink(1);
                        }
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }

        return InteractionResult.PASS;
    }

    public static void burnBlock(ServerLevel level, BlockPos webPos, RandomSource random, boolean burst) {
        level.sendParticles(ParticleTypes.FLAME, webPos.getX() + 0.5D, webPos.getY() + 0.5D, webPos.getZ() + 0.5D, 7 + random.nextInt(40), random.nextDouble() * 0.5D, random.nextDouble() * 0.5D, random.nextDouble() * 0.5D, 0.005);
        level.removeBlock(webPos, false);

        if (burst) {
            for (BlockPos pos : BlockPos.betweenClosed(webPos.offset(-1, -1, -1), webPos.offset(1, 1, 1))) {
                if(level.getBlockState(pos).is(RealisticCobwebsTags.COBWEBS)) {
                    ((WorldScheduler) level).cobwebs$markPos(pos.immutable());
                }
            }
        }
    }
}
