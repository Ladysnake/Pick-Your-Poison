package ladysnake.pickyourpoison.mixin;

import ladysnake.pickyourpoison.common.PickYourPoison;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Blocks.class)
public class BlocksMixin {
    @Inject(method = "canSpawnOnLeaves", at = @At("RETURN"))
    private static void canSpawnOnLeaves(BlockState state, BlockView world, BlockPos pos, EntityType<?> type, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() || type == PickYourPoison.POISON_DART_FROG);
    }

}
