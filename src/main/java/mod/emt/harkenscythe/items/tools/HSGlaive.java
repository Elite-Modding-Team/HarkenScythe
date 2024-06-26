package mod.emt.harkenscythe.items.tools;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import mod.emt.harkenscythe.init.HSDamageSource;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class HSGlaive extends ItemSword
{
    private final float attackSpeed;
    private final EnumRarity rarity;

    public HSGlaive(ToolMaterial material, float attackSpeed, EnumRarity rarity)
    {
        super(material);
        this.attackSpeed = attackSpeed;
        this.rarity = rarity;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        player.playSound(SoundEvents.ENTITY_PLAYER_BREATH, 0.8F, 0.9F);
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }

    // TODO: The glaive does not use an AoE attack, it will only be able to attack one entity at a time but does armor piercing damage
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
        float damage = this.getAttackDamage() + 4.0F; // Has to be done like this otherwise it'll calculate wrong
        float range = 3.0F;
        AxisAlignedBB bb = new AxisAlignedBB(entityLiving.posX - range, entityLiving.posY - range, entityLiving.posZ - range, entityLiving.posX + range, entityLiving.posY + range, entityLiving.posZ + range);

        for (int i = 0; i < worldIn.getEntitiesWithinAABB(EntityLiving.class, bb).size(); i++)
        {
            EntityLiving entityInAABB = worldIn.getEntitiesWithinAABB(EntityLiving.class, bb).get(i);

            if (Math.min(1.0F, (getMaxItemUseDuration(stack) - timeLeft) / 20.0F) >= 1.0F)
            {
                // Damage x 2 (Mojang is very strange with damage values...)
                entityInAABB.attackEntityFrom(new HSDamageSource("hs_reap", entityLiving), damage * 2);
            }
        }

        if (Math.min(1.0F, (getMaxItemUseDuration(stack) - timeLeft) / 20.0F) >= 1.0F && entityLiving instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) entityLiving;
            player.swingArm(EnumHand.MAIN_HAND);
            player.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
            stack.damageItem(2, player);
            player.addStat(StatList.getObjectUseStats(this));
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return rarity;
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = HashMultimap.create();

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Damage modifier", this.getAttackDamage() + 3.0D, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Speed modifier", this.attackSpeed - 4.0D, 0));
        }

        return multimap;
    }
}
