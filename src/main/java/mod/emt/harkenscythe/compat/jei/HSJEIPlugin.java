package mod.emt.harkenscythe.compat.jei;

import java.util.List;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mod.emt.harkenscythe.compat.jei.blood.HSJEIRecipeCategoryBloodAltar;
import mod.emt.harkenscythe.compat.jei.blood.HSJEIRecipeWrapperBloodAltar;
import mod.emt.harkenscythe.compat.jei.soul.HSJEIRecipeCategorySoulAltar;
import mod.emt.harkenscythe.compat.jei.soul.HSJEIRecipeWrapperSoulAltar;
import mod.emt.harkenscythe.init.HSAltarRecipes;
import mod.emt.harkenscythe.recipe.HSRecipeBloodAltar;
import mod.emt.harkenscythe.recipe.HSRecipeSoulAltar;

@JEIPlugin
public class HSJEIPlugin implements IModPlugin
{
    @Override
    public void register(IModRegistry registry)
    {
        // Register Blood Altar category
        registry.addRecipeCategories(new HSJEIRecipeCategoryBloodAltar(registry.getJeiHelpers().getGuiHelper()));
        registry.handleRecipes(HSRecipeBloodAltar.class, HSJEIRecipeWrapperBloodAltar::new, HSJEIRecipeCategoryBloodAltar.UID);

        // Register Soul Altar category
        registry.addRecipeCategories(new HSJEIRecipeCategorySoulAltar(registry.getJeiHelpers().getGuiHelper()));
        registry.handleRecipes(HSRecipeSoulAltar.class, HSJEIRecipeWrapperSoulAltar::new, HSJEIRecipeCategorySoulAltar.UID);

        // Add Blood Altar recipes
        List<HSRecipeBloodAltar> bloodAltarRecipes = HSAltarRecipes.getBloodAltarRecipes();
        registry.addRecipes(bloodAltarRecipes, HSJEIRecipeCategoryBloodAltar.UID);

        // Add Soul Altar recipes
        List<HSRecipeSoulAltar> soulAltarRecipes = HSAltarRecipes.getSoulAltarRecipes();
        registry.addRecipes(soulAltarRecipes, HSJEIRecipeCategorySoulAltar.UID);
    }
}
