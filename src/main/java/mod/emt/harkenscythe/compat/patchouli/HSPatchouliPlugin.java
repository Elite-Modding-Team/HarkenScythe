package mod.emt.harkenscythe.compat.patchouli;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemHandlerHelper;

import mod.emt.harkenscythe.HarkenScythe;
import mod.emt.harkenscythe.init.HSItems;
import vazkii.patchouli.api.PatchouliAPI;

@SuppressWarnings("unused")
public class HSPatchouliPlugin
{
    private static PatchouliAPI.IPatchouliAPI api = null;

    public static void giveBookToPlayer(EntityPlayer player)
    {
        ItemStack bookStack = getAPI().getBookStack(new ResourceLocation(HarkenScythe.MOD_ID, "reaper_guidebook").toString());
        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(HSItems.reaper_guidebook));
    }

    private static PatchouliAPI.IPatchouliAPI getAPI()
    {
        if (api == null)
        {
            api = PatchouliAPI.instance;
            if (api.isStub())
            {
                HarkenScythe.LOGGER.warn("Failed to intercept Patchouli API. Issues may occur!");
            }
        }
        return api;
    }
}
