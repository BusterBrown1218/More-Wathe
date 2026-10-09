package xyz.busterbrown1218.more_wathe.client.mixin.config;

import cat.rezelyn.watheextended.api.ModifiersDisplay;
import cat.rezelyn.watheextended.client.screen.config.ModifiersCategory;
import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.DoubleSliderControllerBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.busterbrown1218.more_wathe.components.ConfigComponent;

import java.util.Map;
import java.util.function.BiConsumer;

@Mixin(ModifiersCategory.class)
public class ExtendedModifiersConfigMixin {
    @Inject(method = "build", at = @At(value = "INVOKE", target = "Ldev/isxander/yacl3/api/OptionGroup$Builder;option(Ldev/isxander/yacl3/api/Option;)Ldev/isxander/yacl3/api/OptionGroup$Builder;", shift = At.Shift.AFTER))
    private static void addChance(Screen parent, Map<String, Boolean> pendingState, BiConsumer<String, Screen> sendCommand, CallbackInfoReturnable<ConfigCategory> cir, @Local(name = "group") OptionGroup.Builder builder, @Local(name = "display") ModifiersDisplay.ModifierDisplay display, @Local(name = "state") boolean state) {
        if (state) builder.option(Option.<Double>createBuilder()
                .name(Text.literal(display.display().getString() + " Chance"))
                .binding(
                        1.0,
                        () -> ConfigComponent.KEY.get(MinecraftClient.getInstance().player.getWorld()).modifierChances.getOrDefault(display.id(), 1.0),
                        val -> sendCommand.accept("morewathe modifierChance " + display.id() + " " + val, parent)
                )
                .controller(opt -> DoubleSliderControllerBuilder.create(opt)
                        .range(0.0, 1.0)
                        .step(0.01))

                .build());
    }
}
