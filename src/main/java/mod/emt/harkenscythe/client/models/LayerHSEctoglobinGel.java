package mod.emt.harkenscythe.client.models;

import mod.emt.harkenscythe.client.renderers.RenderHSEctoglobin;
import mod.emt.harkenscythe.entities.HSEntityEctoglobin;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerHSEctoglobinGel implements LayerRenderer<HSEntityEctoglobin>
{
    private final RenderHSEctoglobin ectoglobinRenderer;
    private final ModelBase slimeModel = new ModelSlime(0);

    public LayerHSEctoglobinGel(RenderHSEctoglobin ectoglobinRenderer)
    {
        this.ectoglobinRenderer = ectoglobinRenderer;
    }

    @Override
    public void doRenderLayer(HSEntityEctoglobin entityEctoglobin, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (!entityEctoglobin.isInvisible())
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableNormalize();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.slimeModel.setModelAttributes(this.ectoglobinRenderer.getMainModel());
            this.slimeModel.render(entityEctoglobin, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.disableBlend();
            GlStateManager.disableNormalize();
        }
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return true;
    }
}
