package xyz.busterbrown1218.more_wathe.mixin;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.game.GameFunctions;
import dev.doctor4t.wathe.game.gamemode.MurderGameMode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.busterbrown1218.more_wathe.components.GuardianAngelPlayerComponent;
import xyz.busterbrown1218.more_wathe.role.GuardianRole;

import java.util.List;

@Mixin(MurderGameMode.class)
public class GuardianResetMixin {
    @Inject(method = "tickServerGameLoop", at = @At(value = "HEAD"))
    private static void tick(ServerWorld serverWorld, GameWorldComponent gameWorldComponent, CallbackInfo ci) {
        List<ServerPlayerEntity> alivePlayers = serverWorld.getPlayers(GameFunctions::isPlayerAliveAndSurvival);
        ServerPlayerEntity guardian = null;
        for (ServerPlayerEntity player : alivePlayers) {
            if (GuardianAngelPlayerComponent.KEY.get(player).protecting != null) {
                guardian = player;
                break;
            }
        }
        if (guardian == null) return;
        PlayerEntity guardedPlayer = serverWorld.getPlayerByUuid(GuardianAngelPlayerComponent.KEY.get(guardian).protecting);
        if (guardedPlayer == null) {
            GuardianRole.resetRole(guardian);
            return;
        }
        if (!GameFunctions.isPlayerAliveAndSurvival(guardedPlayer)) GuardianRole.resetRole(guardian);
    }
}
