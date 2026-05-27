package dev.upcraft.cobwebs;

import com.teamresourceful.resourcefulconfig.api.loader.Configurator;
import dev.upcraft.cobwebs.service.RCPlatform;
import dev.upcraft.cobwebs.util.WorldScheduler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class RealisticCobwebs {

    public static final String MODID = "realistic_cobwebs";

    public static final Configurator CONFIGURATOR = new Configurator(MODID);

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }

    public static void init() {
        CONFIGURATOR.register(RealisticCobwebsConfig.class);
    }

    public static InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown() || stack.isEmpty()) {
            return InteractionResult.PASS;
        }

        BlockPos startPos = hitResult.getBlockPos();
        if (level.getBlockState(startPos).is(RealisticCobwebsTags.COBWEBS)) {
            var lookup = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            var hasFireAspect = RealisticCobwebsConfig.fireAspectBehavior.shouldBurnCobwebs() && RCPlatform.INSTANCE.getItemEnchantments(stack, lookup).getLevel(lookup.getOrThrow(Enchantments.FIRE_ASPECT)) > 0;
            var isBurst = (hasFireAspect && RealisticCobwebsConfig.fireAspectBehavior.shouldDoBurst()) || (RealisticCobwebsConfig.flintAndSteelBurstEnabled && stack.is(RealisticCobwebsTags.BURST_TRIGGERS));
            var isSoulTorch = stack.is(RealisticCobwebsTags.SOUL_TORCHES);
            if (isBurst || hasFireAspect || isSoulTorch || stack.is(RealisticCobwebsTags.TORCHES) || stack.is(RealisticCobwebsTags.BURST_TRIGGERS)) {
                if (level instanceof ServerLevel serverLevel) {
                    burnBlock(serverLevel, startPos, player.getRandom(), isBurst, isSoulTorch ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME);
                    if (!player.isCreative()) {
                        if(stack.getMaxDamage() > 0) {
                            stack.hurtAndBreak(RealisticCobwebsConfig.flintAndSteelDamagePerUse, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                        }
                        else if(player.getRandom().nextFloat() < RealisticCobwebsConfig.torchConsumeChance) {
                            stack.shrink(1);
                        }
                    }
                }
                if (stack.getMaxDamage() > 0) {
                    level.playSound(player, startPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 0.6F, player.getRandom().nextFloat() * 0.4F + 0.8F);
                }
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    public static void burnBlock(ServerLevel level, BlockPos webPos, RandomSource random, boolean burst, ParticleOptions particleType) {
        level.sendParticles(particleType, webPos.getX() + 0.5D, webPos.getY() + 0.5D, webPos.getZ() + 0.5D, 7 + random.nextInt(40), random.nextDouble() * 0.5D, random.nextDouble() * 0.5D, random.nextDouble() * 0.5D, 0.005);
        level.removeBlock(webPos, false);
        level.playSound(null, webPos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, random.nextFloat() * 0.3F, random.nextFloat() * 0.4F + 0.8F);

        if (burst) {
            for (BlockPos pos : BlockPos.betweenClosed(webPos.offset(-1, -1, -1), webPos.offset(1, 1, 1))) {
                if(level.getBlockState(pos).is(RealisticCobwebsTags.COBWEBS)) {
                    ((WorldScheduler) level).cobwebs$markPos(pos.immutable(), particleType);
                }
            }
        }
    }
}
