package xyz.busterbrown1218.more_wathe.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.wathe.api.event.AllowPlayerDeath;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.game.GameConstants;
import dev.doctor4t.wathe.game.GameFunctions;
import dev.doctor4t.wathe.index.WatheSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import org.agmas.harpymodloader.component.WorldModifierComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.busterbrown1218.more_wathe.MoreWathe;
import xyz.busterbrown1218.more_wathe.components.TelepathPlayerComponent;

import java.util.List;
import java.util.UUID;

@Mixin(GameFunctions.class)
public class PlayerDeathMixin {
    @WrapOperation(method = "killPlayer(Lnet/minecraft/entity/player/PlayerEntity;ZLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Identifier;)V", at = @At(value = "INVOKE", target = "Ldev/doctor4t/wathe/api/event/AllowPlayerDeath;allowDeath(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Identifier;)Z"))
    private static boolean playerDeath(AllowPlayerDeath instance, PlayerEntity victim, PlayerEntity killer, Identifier reason, Operation<Boolean> original) {
        boolean playerDead = original.call(instance, victim, killer, reason);
        if (!playerDead) return false;
        if (killer == null) return true;
        List<UUID> killerPlayers = GameWorldComponent.KEY.get(killer.getWorld()).getAllKillerTeamPlayers();
        if (killerPlayers.contains(killer.getUuid())) {
            killerPlayers.remove(killer.getUuid());
            for (UUID player : killerPlayers) {
                if (WorldModifierComponent.KEY.get(killer.getWorld()).isModifier(player, MoreWathe.TELEPATH)) {
                    PlayerEntity cohort = killer.getWorld().getPlayerByUuid(player);
                    TelepathPlayerComponent.KEY.get(cohort).deaths.add(new TelepathPlayerComponent.TelepathDeath(reason, victim.getDisplayName().getString(), GameConstants.getInTicks(0, 25))); // Maybe make configurable later?
                    TelepathPlayerComponent.KEY.get(cohort).sync();
                    cohort.playSoundToPlayer(WatheSounds.ITEM_KNIFE_STAB, SoundCategory.PLAYERS, 0.5f, 1.0f);
                }
            }
        }

        return true;
    }
}
