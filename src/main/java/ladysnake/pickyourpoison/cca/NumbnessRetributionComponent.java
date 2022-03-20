package ladysnake.pickyourpoison.cca;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.NbtCompound;

public class NumbnessRetributionComponent implements AutoSyncedComponent {
    private float damageAccumulated;

    public float getDamageAccumulated() {
        return damageAccumulated;
    }

    public void setDamageAccumulated(float damageAccumulated) {
        this.damageAccumulated = damageAccumulated;
    }

    @Override
    public void readFromNbt(NbtCompound compoundTag) {
        this.damageAccumulated = compoundTag.getFloat("DamageAccumulated");
    }

    @Override
    public void writeToNbt(NbtCompound compoundTag) {
        compoundTag.putFloat("DamageAccumulated", this.damageAccumulated);
    }
}
