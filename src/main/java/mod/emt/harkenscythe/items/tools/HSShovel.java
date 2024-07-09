package mod.emt.harkenscythe.items.tools;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;

public class HSShovel extends ItemSpade implements IHSTool
{
    private final EnumRarity rarity;
    private final ToolMaterial material;

    public HSShovel(ToolMaterial material, EnumRarity rarity)
    {
        super(material);
        this.rarity = rarity;
        this.material = material;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return rarity;
    }

    @Override
    public ToolMaterial getToolMaterial()
    {
        return this.material;
    }
}
