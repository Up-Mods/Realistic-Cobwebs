package dev.upcraft.cobwebs.mixin;

import dev.upcraft.cobwebs.RealisticCobwebs;
import dev.upcraft.cobwebs.RealisticCobwebsTags;
import dev.upcraft.cobwebs.util.BurnPosInfo;
import dev.upcraft.cobwebs.util.WorldScheduler;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level implements WorldScheduler {

    @Unique
    private Set<BurnPosInfo> toTick;

    private ServerLevelMixin(WritableLevelData $$0, ResourceKey<Level> $$1, RegistryAccess $$2, Holder<DimensionType> $$3, Supplier<ProfilerFiller> $$4, boolean $$5, boolean $$6, long $$7, int $$8) {
        super($$0, $$1, $$2, $$3, $$5, $$6, $$7, $$8);
        throw new UnsupportedOperationException();
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void construct(MinecraftServer server, Executor executor, LevelStorageSource.LevelStorageAccess levelStorage, ServerLevelData levelData, ResourceKey dimension, LevelStem levelStem, boolean isDebug, long biomeZoomSeed, List customSpawners, boolean tickTime, CallbackInfo ci) {
        toTick = new ObjectOpenHashSet<>();
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ticks/LevelTicks;tick(JILjava/util/function/BiConsumer;)V", ordinal = 0, shift = At.Shift.AFTER))
    private void cobwebs$onTick(BooleanSupplier $$0, CallbackInfo ci) {
        if (!toTick.isEmpty()) {
            Set<BurnPosInfo> original = toTick;
            toTick = new ObjectOpenHashSet<>();
            original.forEach(info -> {
                if (this.getBlockState(info.pos()).is(RealisticCobwebsTags.COBWEBS)) {
                    RealisticCobwebs.burnBlock((ServerLevel) (Object) this, info.pos(), this.random, true, info.particleType());
                }
            });
        }
    }

    @Override
    public void cobwebs$markPos(BlockPos pos, ParticleOptions particleType) {
        toTick.add(new BurnPosInfo(pos, particleType));
    }
}
