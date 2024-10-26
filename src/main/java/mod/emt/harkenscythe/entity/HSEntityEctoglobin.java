package mod.emt.harkenscythe.entity;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import mod.emt.harkenscythe.init.HSAdvancements;

public class HSEntityEctoglobin extends HSEntityGlobin
{
    private static final DataParameter<Integer> SKIN_TYPE = EntityDataManager.createKey(HSEntityEctoglobin.class, DataSerializers.VARINT);

    public HSEntityEctoglobin(World world)
    {
        super(world);
    }

    public int getSkin()
    {
        return this.dataManager.get(SKIN_TYPE);
    }

    public void setSkin(int skinType)
    {
        this.dataManager.set(SKIN_TYPE, skinType);
    }

    @Override
    public void onDeath(DamageSource cause)
    {
        super.onDeath(cause);
        if (cause.getTrueSource() instanceof EntityPlayerMP)
        {
            HSAdvancements.ENCOUNTER_ECTOGLOBIN.trigger((EntityPlayerMP) cause.getTrueSource());
        }
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.getDataManager().register(SKIN_TYPE, this.rand.nextInt(3));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("Variant", getSkin());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        setSkin(nbt.getInteger("Variant"));
    }

    @Override
    protected EnumParticleTypes getParticleType()
    {
        // TODO: Replace with more fitting particle
        return EnumParticleTypes.DRIP_WATER;
    }

    @Override
    protected HSEntityEctoglobin createInstance()
    {
        return new HSEntityEctoglobin(this.world);
    }
}
