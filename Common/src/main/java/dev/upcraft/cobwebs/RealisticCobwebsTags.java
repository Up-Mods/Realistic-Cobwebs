package dev.upcraft.cobwebs;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class RealisticCobwebsTags {

    public static final TagKey<Item> TORCHES = TagKey.create(Registries.ITEM, RealisticCobwebs.id("torches"));
    public static final TagKey<Item> SOUL_TORCHES = TagKey.create(Registries.ITEM, RealisticCobwebs.id("soul_torches"));
    public static final TagKey<Item> BURST_TRIGGERS = TagKey.create(Registries.ITEM, RealisticCobwebs.id("burst_triggers"));

    public static final TagKey<Block> COBWEBS = TagKey.create(Registries.BLOCK, RealisticCobwebs.id("cobwebs"));
}
