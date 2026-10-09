package xyz.busterbrown1218.more_wathe.client;

import dev.doctor4t.wathe.api.Role;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.BsXinQin.kinswathe.client.KinsWatheInitializeClient;
import org.agmas.noellesroles.client.NoellesrolesClient;
import org.lwjgl.glfw.GLFW;
import xyz.busterbrown1218.more_wathe.MoreWathe;
import xyz.busterbrown1218.more_wathe.packet.AbilityC2SPacket;

import java.util.Arrays;

public class MoreWatheClient implements ClientModInitializer {
    public static KeyBinding abilityKeyBind;

    @Override
    public void onInitializeClient() {
        registerAbility();
        registerAbilityPackets();
    }

    private static final Role[] abilityUsers = new Role[]{MoreWathe.ESCAPIST, MoreWathe.GUARDIAN_ANGEL};

    public static void registerAbilityPackets() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (abilityKeyBind == null) return;
            if (abilityKeyBind.isPressed()) {
                client.execute(() -> {
                    if (MinecraftClient.getInstance().player == null) return;
                    GameWorldComponent worldComponent = GameWorldComponent.KEY.get(MinecraftClient.getInstance().player.getWorld());
                    boolean canUseAbility = false;
                    for (Role role : abilityUsers) {
                        if (worldComponent.isRole(MinecraftClient.getInstance().player, role)) {
                            canUseAbility = true;
                            break;
                        }
                    }
                    if (!canUseAbility) return;
                    ClientPlayNetworking.send(new AbilityC2SPacket());
                });
            }
        });
    }

    public static void registerAbility() {
        if (FabricLoader.getInstance().isModLoaded("noellesroles")) {
            if (abilityKeyBind == null) ClientTickEvents.START_CLIENT_TICK.register(client -> {
                abilityKeyBind = NoellesrolesClient.abilityBind;
            });
        } else if (FabricLoader.getInstance().isModLoaded("kinswathe")) {
            if (abilityKeyBind == null) ClientTickEvents.START_CLIENT_TICK.register(client -> {
                abilityKeyBind = KinsWatheInitializeClient.abilityBind;
            });
        } else {
            abilityKeyBind = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + MoreWathe.MOD_ID + ".ability", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "category.wathe.keybinds"));
        }
    }
}
