package xyz.busterbrown1218.more_wathe;

import dev.doctor4t.wathe.api.Role;
import dev.doctor4t.wathe.api.WatheRoles;
import dev.doctor4t.wathe.api.event.AllowPlayerDeath;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.cca.PlayerShopComponent;
import dev.doctor4t.wathe.game.GameConstants;
import dev.doctor4t.wathe.game.GameFunctions;
import dev.doctor4t.wathe.index.WatheItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.agmas.harpymodloader.events.ModdedRoleAssigned;
import org.agmas.harpymodloader.events.ModifierAssigned;
import org.agmas.harpymodloader.events.ResetPlayerEvent;
import org.agmas.harpymodloader.modifiers.HMLModifiers;
import org.agmas.harpymodloader.modifiers.Modifier;
import xyz.busterbrown1218.more_wathe.components.AbilityPlayerComponent;
import xyz.busterbrown1218.more_wathe.components.EscapistPlayerComponent;
import xyz.busterbrown1218.more_wathe.components.GuardianAngelPlayerComponent;
import xyz.busterbrown1218.more_wathe.components.TelepathPlayerComponent;
import xyz.busterbrown1218.more_wathe.config.ConfigCommands;
import xyz.busterbrown1218.more_wathe.config.ConfigFile;
import xyz.busterbrown1218.more_wathe.packet.AbilityC2SPacket;
import xyz.busterbrown1218.more_wathe.util.DeathReasonLoader;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MoreWathe implements ModInitializer {
    public static String MOD_ID = "morewathe";

    public static Identifier ESCAPIST_ID = Identifier.of(MOD_ID, "escapist");
    public static Identifier MARKSMAN_ID = Identifier.of(MOD_ID, "marksman");
    public static Identifier GUARDIAN_ANGEL_ID = Identifier.of(MOD_ID, "guardian_angel");

    public static Identifier BIG_ID = Identifier.of(MOD_ID, "big");
    public static Identifier LOCKSMITH_ID = Identifier.of(MOD_ID, "locksmith");
    public static Identifier FAST_ID = Identifier.of(MOD_ID, "fast");
    public static Identifier WARBLED_ID = Identifier.of(MOD_ID, "warbled");
    public static Identifier TELEPATH_ID = Identifier.of(MOD_ID, "telepath");

    public static Role ESCAPIST = WatheRoles.registerRole(new Role(ESCAPIST_ID, new Color(170, 50, 50).getRGB(), false, true, Role.MoodType.FAKE, Integer.MAX_VALUE, true));
    public static Role MARKSMAN = WatheRoles.registerRole(new Role(MARKSMAN_ID, new Color(65, 15, 85).getRGB(), false, true, Role.MoodType.FAKE, Integer.MAX_VALUE, true));
    public static Role GUARDIAN_ANGEL = WatheRoles.registerRole(new Role(GUARDIAN_ANGEL_ID, new Color(250, 255, 150).getRGB(), false, false, Role.MoodType.REAL, WatheRoles.CIVILIAN.getMaxSprintTime(), false));

    public static Modifier BIG = HMLModifiers.registerModifier(new Modifier(BIG_ID, new Color(250, 233, 23).getRGB(), null ,null,false,false));
    public static Modifier LOCKSMITH = HMLModifiers.registerModifier(new Modifier(LOCKSMITH_ID, new Color(100, 25, 25).getRGB(), null,null,true,false));
    public static Modifier FAST = HMLModifiers.registerModifier(new Modifier(FAST_ID, new Color(60, 221, 198).getRGB(), null,null,false,false));
    public static Modifier WARBLED = HMLModifiers.registerModifier(new Modifier(WARBLED_ID, new Color(100, 185, 190).getRGB(), null, null, false, false));
    public static Modifier TELEPATH = HMLModifiers.registerModifier(new Modifier(TELEPATH_ID, new Color(200, 105, 200).getRGB(), null, null, true, false));

    public static EntityAttributeModifier bigModifier;
    public static EntityAttributeModifier fastModifier;

    private static final Random random = new Random();

    @Override
    public void onInitialize() {
        DeathReasonLoader.loadReasons();
        ConfigFile.HANDLER.load();
        registerAttributes();
        PayloadTypeRegistry.playC2S().register(AbilityC2SPacket.ID, AbilityC2SPacket.CODEC);
        registerEvents();
        ConfigCommands.registerCommands();
    }

    public void registerAttributes() {
        bigModifier = new EntityAttributeModifier(Identifier.of(MOD_ID, "big_modifier"), 0.10, EntityAttributeModifier.Operation.ADD_VALUE);
        fastModifier = new EntityAttributeModifier(Identifier.of(MOD_ID, "fast_modifier"), 0.10, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }

    public void registerEvents() {
        ModifierAssigned.EVENT.register((playerEntity, modifier) -> {
            if (modifier.equals(BIG)) {
                playerEntity.getAttributeInstance(EntityAttributes.GENERIC_SCALE).removeModifier(bigModifier);
                playerEntity.getAttributeInstance(EntityAttributes.GENERIC_SCALE).addPersistentModifier(bigModifier);
            }
            if (modifier.equals(LOCKSMITH)) {
                playerEntity.giveItemStack(WatheItems.LOCKPICK.getDefaultStack());
            }
            if (modifier.equals(FAST)) {
                playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).removeModifier(fastModifier);
                playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).addPersistentModifier(fastModifier);
            }
        });
        ModdedRoleAssigned.EVENT.register((playerEntity, role) -> {
            if (role.equals(MARKSMAN)) {
                playerEntity.giveItemStack(WatheItems.REVOLVER.getDefaultStack());
            } else if (role.equals(GUARDIAN_ANGEL)) {
                GameWorldComponent gameWorldComponent = GameWorldComponent.KEY.get(playerEntity.getWorld());
                List<UUID> potentialProtectees;

                if (random.nextInt(101) > 100 - ConfigFile.HANDLER.instance().guardianKillerChance) {
                    potentialProtectees = gameWorldComponent.getAllKillerTeamPlayers();
                } else {
                    potentialProtectees = new ArrayList<>();
                    gameWorldComponent.getRoles().forEach((player, playerRole) -> {
                        if (!playerRole.canUseKiller()) potentialProtectees.add(player);
                    });
                }

                potentialProtectees.remove(playerEntity.getUuid());

                GuardianAngelPlayerComponent guardianComponent = GuardianAngelPlayerComponent.KEY.get(playerEntity);
                guardianComponent.protecting = potentialProtectees.get(random.nextInt(0, potentialProtectees.size()));
                guardianComponent.sync();
                GuardianAngelPlayerComponent protectedComponent = GuardianAngelPlayerComponent.KEY.get(playerEntity.getWorld().getPlayerByUuid(guardianComponent.protecting));
                protectedComponent.hasGuardian = true;
                protectedComponent.sync();
            } else if (role.equals(ESCAPIST)) {
                EscapistPlayerComponent.KEY.get(playerEntity).reset();
            }
        });
        ResetPlayerEvent.EVENT.register(playerEntity -> {
            playerEntity.getAttributeInstance(EntityAttributes.GENERIC_SCALE).removeModifier(bigModifier);
            playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).removeModifier(fastModifier);
            GuardianAngelPlayerComponent.KEY.get(playerEntity).reset();
            TelepathPlayerComponent.KEY.get(playerEntity).reset();
        });
        AllowPlayerDeath.EVENT.register((victim, killer, deathReason) -> {
            if (!(GuardianAngelPlayerComponent.KEY.get(victim).isProtected > 0)) return true;
            return !ConfigFile.HANDLER.instance().protectedDeathReasons.contains(deathReason.toString());
        });

        ServerPlayNetworking.registerGlobalReceiver(AbilityC2SPacket.ID, ((payload, context) -> {
            AbilityPlayerComponent abilityComponent = (AbilityPlayerComponent) AbilityPlayerComponent.KEY.get(context.player());
            GameWorldComponent gameWorldComponent = (GameWorldComponent) GameWorldComponent.KEY.get(context.player().getWorld());
            if (abilityComponent.cooldown <= 0) {
                handleAbilities(context.player(), gameWorldComponent, abilityComponent, context);
            }
        }));
    }

    private void handleAbilities(PlayerEntity player, GameWorldComponent gameWorldComponent, AbilityPlayerComponent abilityComponent, ServerPlayNetworking.Context context) {
        if (!GameFunctions.isPlayerAliveAndSurvival(player)) return;
        if (gameWorldComponent.isRole(player, ESCAPIST)) {
            EscapistPlayerComponent escapistPlayerComponent = EscapistPlayerComponent.KEY.get(player);
            PlayerShopComponent playerShopComponent = PlayerShopComponent.KEY.get(player);
            if (!escapistPlayerComponent.placed) {
                abilityComponent.cooldown = GameConstants.getInTicks(0, 10);
                escapistPlayerComponent.setPosition();
            } else if (playerShopComponent.balance >= 100) {
                playerShopComponent.balance -= 100;
                playerShopComponent.sync();
                abilityComponent.cooldown = GameConstants.getInTicks(0, 30);
                escapistPlayerComponent.teleport();
            }
        } else if (gameWorldComponent.isRole(player, GUARDIAN_ANGEL)) {
            GuardianAngelPlayerComponent guardianComponent = GuardianAngelPlayerComponent.KEY.get(player);
            PlayerShopComponent playerShopComponent = PlayerShopComponent.KEY.get(player);
            if (playerShopComponent.balance >= 100) {
                playerShopComponent.balance -= 100;
                playerShopComponent.sync();
                abilityComponent.cooldown = GameConstants.getInTicks(1, 0);
                guardianComponent.startProtecting();
            }
        }
    }
}
