package xyz.busterbrown1218.more_wathe.client.mixin;

import cat.rezelyn.watheextended.api.hml.ConfigHelper;
import cat.rezelyn.watheextended.api.hml.ModifiersDisplay;
import cat.rezelyn.watheextended.api.wathe.RolesDisplay;
import cat.rezelyn.watheextended.client.screen.guidebook.GuidebookEntryBuilder;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.busterbrown1218.more_wathe.config.ConfigFile;

import java.util.HashMap;
import java.util.Map;

@Mixin(GuidebookEntryBuilder.class)
public class HideDisabledMixin {
    @WrapOperation(method = "buildRoles", at = @At(value = "INVOKE", target = "Lcat/rezelyn/watheextended/api/wathe/RolesDisplay;get()Ljava/util/Map;"))
    private static Map<String, RolesDisplay.RoleDisplay> disabledRoles(Operation<Map<String, RolesDisplay.RoleDisplay>> original) {
        if (!ConfigFile.HANDLER.instance().hideDisabledRoles) return original.call();
        Map<String, RolesDisplay.RoleDisplay> originalRoles = original.call();
        Map<String, RolesDisplay.RoleDisplay> enabledRoles = new HashMap<>();
        for (Map.Entry<String, RolesDisplay.RoleDisplay> entry : originalRoles.entrySet()) {
            if (!ConfigHelper.getDisabledRoles().contains(entry.getValue().id())) {
                enabledRoles.put(entry.getKey(), entry.getValue());
            }
        }
        return enabledRoles;
    }

    @WrapOperation(method = "buildModifiers", at = @At(value = "INVOKE", target = "Lcat/rezelyn/watheextended/api/hml/ModifiersDisplay;get()Ljava/util/Map;"))
    private static Map<String, ModifiersDisplay.ModifierDisplay> disabledModifiers(Operation<Map<String, ModifiersDisplay.ModifierDisplay>> original) {
        if (!ConfigFile.HANDLER.instance().hideDisabledModifiers) return original.call();
        Map<String, ModifiersDisplay.ModifierDisplay> originalModifiers = original.call();
        Map<String, ModifiersDisplay.ModifierDisplay> enabledModifiers = new HashMap<>();
        for (Map.Entry<String, ModifiersDisplay.ModifierDisplay> entry : originalModifiers.entrySet()) {
            if (!ConfigHelper.getDisabledModifiers().contains(entry.getValue().id())) {
                enabledModifiers.put(entry.getKey(), entry.getValue());
            }
        }
        return enabledModifiers;
    }
}
