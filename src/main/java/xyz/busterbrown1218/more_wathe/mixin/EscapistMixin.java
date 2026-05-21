package xyz.busterbrown1218.more_wathe.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.wathe.api.Role;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import net.minecraft.entity.player.PlayerEntity;
import org.agmas.noellesroles.Noellesroles;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.busterbrown1218.more_wathe.MoreWathe;

@Mixin(Noellesroles.class)
public class EscapistMixin {
    @WrapOperation(method = "lambda$registerPackets$16", at = @At(value = "INVOKE", target = "Ldev/doctor4t/wathe/cca/GameWorldComponent;isRole(Lnet/minecraft/entity/player/PlayerEntity;Ldev/doctor4t/wathe/api/Role;)Z"))
    private static boolean orEscapist(GameWorldComponent instance, PlayerEntity player, Role role, Operation<Boolean> original) {
        return instance.isRole(player, role) || instance.isRole(player, MoreWathe.ESCAPIST);
    }
}
