package mod.emt.harkenscythe.client.color;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mod.emt.harkenscythe.item.armor.HSArmorDyeable;

@SideOnly(Side.CLIENT)
public class HSArmorDyeableColorHandler implements IItemColor
{
    @Override
    public int colorMultiplier(ItemStack stack, int tintIndex)
    {
        return tintIndex > 0 ? -1 : ((HSArmorDyeable) stack.getItem()).getColor(stack);
    }
}
