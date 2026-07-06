package xyz.busterbrown1218.more_wathe.role;

import dev.doctor4t.wathe.api.WatheRoles;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.client.gui.RoleAnnouncementTexts;
import dev.doctor4t.wathe.util.AnnounceWelcomePayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class GuardianRole {
    public static void resetRole(ServerPlayerEntity playerEntity) {
        GameWorldComponent gameWorldComponent = GameWorldComponent.KEY.get(playerEntity.getWorld());
        // May make it so it switches to amnesiac or something later, not sure
        gameWorldComponent.addRole(playerEntity, WatheRoles.CIVILIAN);
        ServerPlayNetworking.send(
                playerEntity,
                new AnnounceWelcomePayload(
                        RoleAnnouncementTexts.ROLE_ANNOUNCEMENT_TEXTS.indexOf(RoleAnnouncementTexts.CIVILIAN),
                        gameWorldComponent.getAllKillerTeamPlayers().size(),
                        0
                )
        );
    }
}
