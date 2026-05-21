package xyz.busterbrown1218.more_wathe.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.wathe.block.TrimmedBedBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.busterbrown1218.more_wathe.MoreWathe;

@Mixin(TrimmedBedBlock.class)
public class WakeupMixin {
    @WrapOperation(method = "onUse", at = @At(value = "INVOKE", target = "Ldev/doctor4t/wathe/block/TrimmedBedBlock;wakePlayers(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z"))
    public boolean wakePlayers(TrimmedBedBlock instance, World world, BlockPos pos, Operation<Boolean> original) {
        if (MoreWathe.configFile.getPreventOtherPlayersWakeup()) return true;
        return original.call(instance, world, pos);
    }
}
