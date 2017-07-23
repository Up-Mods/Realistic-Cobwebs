package mod.upcraftlp.cobwebs;

import net.minecraft.block.BlockWeb;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * (c)2017 UpcraftLP
 */
public class BlockDecoWeb extends BlockWeb {

    public BlockDecoWeb() {
        this.setRegistryName("deco_web");
        this.setUnlocalizedName("deco_web");
        this.setSoundType(SoundType.CLOTH);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (!worldIn.isRemote) {
            entityIn.setInWeb(); //no fall damage, slowdown
            if (entityIn instanceof EntityLivingBase) {
                worldIn.setBlockToAir(pos);
                int stackSize = this.quantityDropped(null, RANDOM.nextInt(2), RANDOM);
                if (stackSize > 0) {
                    EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.STRING, stackSize));
                    item.setDefaultPickupDelay();
                    worldIn.spawnEntity(item);
                }
            }
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.STRING;
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        fortune = MathHelper.clamp(fortune, 0, 3);
        return fortune + random.nextInt(3);
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        return Arrays.asList(new ItemStack(Item.getItemFromBlock(this)));
    }

}
