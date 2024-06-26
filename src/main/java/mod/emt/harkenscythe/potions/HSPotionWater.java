package mod.emt.harkenscythe.potions;

import mod.emt.harkenscythe.HarkenScythe;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class HSPotionWater extends Potion
{
    public HSPotionWater(String name)
    {
        super(false, 0x334CB2);
        setPotionName("effect." + HarkenScythe.MOD_ID + "." + name);
        setRegistryName(HarkenScythe.MOD_ID, name);
    }

    @Override
    public boolean shouldRender(PotionEffect effect)
    {
        return false;
    }

    @Override
    public boolean shouldRenderInvText(PotionEffect effect)
    {
        return false;
    }

    @Override
    public boolean shouldRenderHUD(PotionEffect effect)
    {
        return false;
    }
}
