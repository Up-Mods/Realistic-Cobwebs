package dev.upcraft.cobwebs.neoforge.events;

import dev.upcraft.cobwebs.RealisticCobwebs;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RealisticCobwebs.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CobwebsEventHandler {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        var result = RealisticCobwebs.onRightClickBlock(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec());
        if(result != InteractionResult.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(result);
        }
    }
}
