package dev.upcraft.cobwebs.service;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.ServiceLoader;

// TODO move to sparkweave
public interface RCPlatform {

    RCPlatform INSTANCE = ServiceLoader.load(RCPlatform.class, RCPlatform.class.getClassLoader()).findFirst().orElseThrow(() -> new IllegalStateException("No platform implementation found for %s".formatted(RCPlatform.class.getName())));

    ItemEnchantments getItemEnchantments(ItemStack stack, HolderLookup.RegistryLookup<Enchantment> lookup);
}
