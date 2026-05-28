package xyz.busterbrown1218.more_wathe;

import dev.doctor4t.wathe.api.Role;
import dev.doctor4t.wathe.api.WatheRoles;
import dev.doctor4t.wathe.index.WatheItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;
import org.agmas.harpymodloader.events.ModdedRoleAssigned;
import org.agmas.harpymodloader.events.ModifierAssigned;
import org.agmas.harpymodloader.events.ResetPlayerEvent;
import org.agmas.harpymodloader.modifiers.HMLModifiers;
import org.agmas.harpymodloader.modifiers.Modifier;
import org.agmas.noellesroles.Noellesroles;
import xyz.busterbrown1218.more_wathe.config.ConfigCommands;
import xyz.busterbrown1218.more_wathe.config.ConfigFile;
import xyz.busterbrown1218.more_wathe.config.ExtendedConfigHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MoreWathe implements ModInitializer {
    public static String MOD_ID = "morewathe";

    public static Identifier ESCAPIST_ID = Identifier.of(MOD_ID, "escapist");
    public static Identifier MARKSMAN_ID = Identifier.of(MOD_ID, "marksman");

    public static Identifier BIG_ID = Identifier.of(MOD_ID, "big");
    public static Identifier LOCKSMITH_ID = Identifier.of(MOD_ID, "locksmith");
    public static Identifier FAST_ID = Identifier.of(MOD_ID, "fast");

    public static Role ESCAPIST = WatheRoles.registerRole(new Role(ESCAPIST_ID, new Color(170, 50, 50).getRGB(), false, true, Role.MoodType.FAKE, Integer.MAX_VALUE, true));
    public static Role MARKSMAN = WatheRoles.registerRole(new Role(MARKSMAN_ID, new Color(65, 15, 85).getRGB(), false, true, Role.MoodType.FAKE, Integer.MAX_VALUE, true));

    public static Modifier BIG = HMLModifiers.registerModifier(new Modifier(BIG_ID, new Color(250, 233, 23).getRGB(), new ArrayList<>(List.of(Noellesroles.MORPHLING)),null,false,false));
    public static Modifier LOCKSMITH = HMLModifiers.registerModifier(new Modifier(LOCKSMITH_ID, new Color(100, 25, 25).getRGB(), null,null,true,false));
    public static Modifier FAST = HMLModifiers.registerModifier(new Modifier(FAST_ID, new Color(60, 221, 198).getRGB(), null,null,false,false));

    public static EntityAttributeModifier bigModifier;
    public static EntityAttributeModifier fastModifier;

    @Override
    public void onInitialize() {
        ConfigFile.HANDLER.load();
        registerAttributes();
        registerEvents();
        ConfigCommands.registerCommands();
        ExtendedConfigHelper.registerEntries();
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
                playerEntity.getAttributeInstance(EntityAttributes.GENERIC_SCALE).removeModifier(fastModifier);
                playerEntity.getAttributeInstance(EntityAttributes.GENERIC_SCALE).addPersistentModifier(fastModifier);
            }
        });
        ModdedRoleAssigned.EVENT.register((playerEntity, role) -> {
            if (role.equals(MARKSMAN)) {
                playerEntity.giveItemStack(WatheItems.REVOLVER.getDefaultStack());
            }
        });
        ResetPlayerEvent.EVENT.register(playerEntity -> {
            playerEntity.getAttributeInstance(EntityAttributes.GENERIC_SCALE).removeModifier(bigModifier);
            playerEntity.getAttributeInstance(EntityAttributes.GENERIC_SCALE).removeModifier(fastModifier);
        });
    }
}
