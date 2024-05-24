package dev.upcraft.cobwebs.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;

public record BurnPosInfo(BlockPos pos, ParticleOptions particleType) {
}
