package dev.upcraft.cobwebs.neoforge.events;

import dev.upcraft.cobwebs.RealisticCobwebs;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = RealisticCobwebs.MODID)
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
