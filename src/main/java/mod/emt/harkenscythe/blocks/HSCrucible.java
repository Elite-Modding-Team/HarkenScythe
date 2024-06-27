package mod.emt.harkenscythe.blocks;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import mod.emt.harkenscythe.init.HSItems;
import mod.emt.harkenscythe.items.HSDyeableArmor;
import mod.emt.harkenscythe.tileentities.HSTileEntityCrucible;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public abstract class HSCrucible extends Block
{
    public static final int MAX_LEVEL = 11;
    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, MAX_LEVEL);
    protected static final AxisAlignedBB AABB_LEGS = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
    protected static final AxisAlignedBB AABB_WALL_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_EAST = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D);

    protected HSCrucible(MapColor color)
    {
        super(Material.IRON, color);
        setDefaultState(blockState.getBaseState().withProperty(LEVEL, 0));
    }

    public void setLevel(World world, BlockPos pos, IBlockState state, int level)
    {
        world.setBlockState(pos, state.withProperty(LEVEL, MathHelper.clamp(level, 0, MAX_LEVEL)), 2);
        world.updateComparatorOutputLevel(pos, this);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(LEVEL, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(LEVEL);
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isPassable(IBlockAccess world, BlockPos pos)
    {
        return true;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return FULL_BLOCK_AABB;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face)
    {
        if (face == EnumFacing.UP)
        {
            return BlockFaceShape.BOWL;
        }
        else
        {
            return face == EnumFacing.DOWN ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
        }
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entity, boolean isActualState)
    {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_LEGS);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_WEST);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_NORTH);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_EAST);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_SOUTH);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(this);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof HSTileEntityCrucible)
        {
            ItemStack heldStack = player.getHeldItem(hand);
            Item heldItem = heldStack.getItem();
            int essenceCount = ((HSTileEntityCrucible) te).getEssenceCount();
            if (essenceCount > 0 && heldItem instanceof HSDyeableArmor)
            {
                HSDyeableArmor armor = (HSDyeableArmor) heldItem;
                if (armor.hasColor(heldStack) && ((armor.getArmorMaterial() == HSItems.ARMOR_BLOODWEAVE && this instanceof HSBloodCrucible) || (armor.getArmorMaterial() == HSItems.ARMOR_SOULWEAVE && this instanceof HSSoulCrucible)))
                {
                    armor.removeColor(heldStack);
                    ((HSTileEntityCrucible) te).setEssenceCount(world, pos, state, essenceCount - 10);
                    player.world.playSound(null, pos, SoundEvents.ENTITY_BOBBER_SPLASH, SoundCategory.BLOCKS, 0.1F, 2.0F);
                    player.addStat(StatList.ARMOR_CLEANED);
                    return true;
                }
            }
        }
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state)
    {
        return new ItemStack(Item.getItemFromBlock(this));
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos pos)
    {
        return blockState.getValue(LEVEL);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, LEVEL);
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new HSTileEntityCrucible();
    }

    protected void fillCrucible(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldStack, Item heldItem, int essenceCount, Item keeperType, Item vesselType)
    {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof HSTileEntityCrucible)
        {
            if (!player.capabilities.isCreativeMode)
            {
                if (heldStack.getItemDamage() + 1 < heldStack.getMaxDamage())
                {
                    heldStack.setItemDamage(heldStack.getItemDamage() + 1);
                }
                else
                {
                    heldStack.shrink(1);
                    if (heldItem == keeperType)
                    {
                        player.setHeldItem(hand, new ItemStack(HSItems.essence_keeper));
                    }
                    else if (heldItem == vesselType)
                    {
                        player.setHeldItem(hand, new ItemStack(HSItems.essence_vessel));
                    }
                }
            }
            world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
            ((HSTileEntityCrucible) te).setEssenceCount(world, pos, state, essenceCount + 1);
            player.addStat(StatList.getObjectUseStats(heldItem));
        }
    }

    protected void emptyCrucible(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldStack, Item heldItem, int essenceCount, Item keeperType, Item vesselType)
    {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof HSTileEntityCrucible)
        {
            if (!player.capabilities.isCreativeMode)
            {
                if (heldItem == HSItems.essence_keeper || heldItem == HSItems.essence_vessel)
                {
                    heldStack.shrink(1);
                    ItemStack newStack = heldItem == HSItems.essence_keeper ? new ItemStack(keeperType) : new ItemStack(vesselType);
                    newStack.setItemDamage(newStack.getMaxDamage() - 1);
                    player.setHeldItem(hand, newStack);
                }
                else if (heldItem == keeperType || heldItem == vesselType)
                {
                    if (heldStack.getItemDamage() == 0) return;
                    if (heldStack.getItemDamage() > 0)
                    {
                        heldStack.setItemDamage(heldStack.getItemDamage() - 1);
                    }
                    if (heldStack.getItemDamage() <= 0)
                    {
                        heldStack.shrink(1);
                        ItemStack newStack = heldItem == keeperType ? new ItemStack(keeperType) : new ItemStack(vesselType);
                        player.setHeldItem(hand, newStack);
                    }
                }
            }
            world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
            ((HSTileEntityCrucible) te).setEssenceCount(world, pos, state, essenceCount - 1);
            player.addStat(StatList.getObjectUseStats(heldItem));
        }
    }
}
