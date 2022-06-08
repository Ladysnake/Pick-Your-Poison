package ladysnake.pickyourpoison.mixin;

import com.mojang.authlib.GameProfile;
import ladysnake.pickyourpoison.common.PickYourPoison;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.encryption.PlayerPublicKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile, @Nullable PlayerPublicKey publicKey) {
        super(world, profile, publicKey);
    }

    @ModifyConstant(method = "tickMovement", constant = @Constant(floatValue = 6.0f))
    private float replaceMinFoodLevelForSprinting(float foodLevel) {
        if (this.hasStatusEffect(PickYourPoison.STIMULATION)) {
            return -1.0f;
        }
        return 6.0f;
    }
}
