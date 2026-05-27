package dev.upcraft.cobwebs.fabric.service;

import com.google.auto.service.AutoService;
import dev.upcraft.cobwebs.service.RCPlatform;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

@AutoService(RCPlatform.class)
public class FabricRCPlatform implements RCPlatform {
    @Override
    public ItemEnchantments getItemEnchantments(ItemStack stack, HolderLookup.RegistryLookup<Enchantment> lookup) {
        return stack.getEnchantments();
    }
}
