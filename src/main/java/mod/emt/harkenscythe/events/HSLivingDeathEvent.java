package mod.emt.harkenscythe.events;

import mod.emt.harkenscythe.HarkenScythe;
import mod.emt.harkenscythe.entities.HSEntitySoul;
import mod.emt.harkenscythe.init.HSEnchantments;
import mod.emt.harkenscythe.items.tools.HSScythe;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = HarkenScythe.MOD_ID)
public class HSLivingDeathEvent
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityDeath(LivingDeathEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();
        World world = entity.getEntityWorld();
        if (!world.isRemote)
        {
            // Scythe reap
            DamageSource damageSource = event.getSource();
            if (damageSource.getTrueSource() instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
                if ((player.getHeldItemMainhand().getItem() instanceof HSScythe && damageSource.getDamageType().equals("hs_reap")) || triggerEnchantment(HSEnchantments.SOULSTEAL, player))
                {
                    HSEntitySoul soul = new HSEntitySoul(world);
                    soul.setPosition(entity.posX, entity.posY, entity.posZ);
                    world.spawnEntity(soul);
                }
            }
        }
    }

    private static boolean triggerEnchantment(Enchantment enchantment, EntityPlayer player)
    {
        int level = EnchantmentHelper.getMaxEnchantmentLevel(enchantment, player);
        return (level > 0 && player.getRNG().nextFloat() < 0.15F * level);
    }
}
