package mod.emt.harkenscythe.entities;

import mod.emt.harkenscythe.init.HSItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class HSEntityBlood extends HSEntityEssence
{
    public HSEntityBlood(World worldIn)
    {
        super(worldIn);
        this.setSize(0.8F, 0.8F);
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        Item item = stack.getItem();
        if (item == HSItems.essence_keeper || item == HSItems.essence_vessel)
        {
            stack.shrink(1);
            ItemStack newStack = item == HSItems.essence_keeper ? new ItemStack(HSItems.essence_keeper_blood) : new ItemStack(HSItems.essence_vessel_blood);
            newStack.setItemDamage(newStack.getMaxDamage() - 1);
            player.setHeldItem(hand, newStack);
            this.world.playSound(null, player.getPosition(), SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.PLAYERS, 1.0F, 1.0F);
            this.world.spawnParticle(EnumParticleTypes.CLOUD, this.posX, this.posY + 1.5D, this.posZ, 0.0D, 0.1D, 0.0D);
            this.setDead();
        }
        else if (item == HSItems.essence_keeper_blood || item == HSItems.essence_vessel_blood)
        {
            if (stack.getItemDamage() == 0) return false;
            if (stack.getItemDamage() > 0)
            {
                stack.setItemDamage(stack.getItemDamage() - 1);
            }
            if (stack.getItemDamage() <= 0)
            {
                stack.shrink(1);
                ItemStack newStack = item == HSItems.essence_keeper_blood ? new ItemStack(HSItems.essence_keeper_blood) : new ItemStack(HSItems.essence_vessel_blood);
                player.setHeldItem(hand, newStack);
            }
            this.world.playSound(null, player.getPosition(), SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.PLAYERS, 1.0F, 1.0F);
            this.world.spawnParticle(EnumParticleTypes.CLOUD, this.posX, this.posY + 1.5D, this.posZ, 0.0D, 0.1D, 0.0D);
            this.setDead();
        }
        return super.processInitialInteract(player, hand);
    }
}
