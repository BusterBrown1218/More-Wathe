package xyz.busterbrown1218.more_wathe.client.mixin.config;

import cat.rezelyn.watheextended.api.RolesDisplay;
import cat.rezelyn.watheextended.client.screen.config.RolesCategory;
import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.DoubleSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.gui.controllers.slider.IntegerSliderController;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.busterbrown1218.more_wathe.components.ConfigComponent;
import xyz.busterbrown1218.more_wathe.util.DeathReasonLoader;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

@Mixin(RolesCategory.class)
public class ExtendedRolesConfigMixin {
    @Inject(method = "build", at = @At(value = "INVOKE", target = "Ldev/isxander/yacl3/api/OptionGroup$Builder;option(Ldev/isxander/yacl3/api/Option;)Ldev/isxander/yacl3/api/OptionGroup$Builder;", shift = At.Shift.AFTER))
    private static void addChances(Screen parent, Set<String> blacklist, Map<String, Boolean> pendingState, BiConsumer<String, Screen> sendCommand, CallbackInfoReturnable<ConfigCategory> cir, @Local(name = "group") OptionGroup.Builder builder, @Local(name = "display") RolesDisplay.RoleDisplay display, @Local(name = "state") boolean state) {
        if (state) builder.option(Option.<Double>createBuilder()
                .name(Text.literal(display.display().getString() + " Chance"))
                .binding(
                        1.0,
                        () -> ConfigComponent.KEY.get(MinecraftClient.getInstance().player.getWorld()).roleChances.getOrDefault(display.id(), 1.0),
                        val -> sendCommand.accept("morewathe roleChance " + display.id() + " " + val, parent)
                )
                .controller(opt -> DoubleSliderControllerBuilder.create(opt)
                        .range(0.0, 1.0)
                        .step(0.01))

                .build());
    }

    @Inject(method = "roleOptions", at = @At(value = "HEAD"))
    private static void roleOptions(String id, OptionGroup.Builder builder, Screen parent, BiConsumer<String, Screen> sendCommand, CallbackInfo ci) {
        if (id.startsWith("morewathe:")) {
            switch (id) {
                case "morewathe:guardian_angel":
                    builder.option(Option.<Integer>createBuilder()
                            .name(Text.translatable("gui.morewathe.config.category.options.roles.guardian_angel.killer_chance"))
                            .description(OptionDescription.of(Text.translatable("gui.morewathe.config.category.options.roles.guardian_angel.killer_chance.desc").styled(style -> style.withColor(16777215))))
                            .binding(50, () -> ConfigComponent.KEY.get(MinecraftClient.getInstance().player.getWorld()).guardianKillerChance, value -> sendCommand.accept("morewathe guardianKillerChance " + value, parent))
                            .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(0, 100).step(1))
                            .build());
                    builder.option(Option.<Boolean>createBuilder()
                            .name(Text.translatable("gui.morewathe.config.category.options.roles.guardian_angel.guardianDefaultsToAmnesiac"))
                            .description(OptionDescription.of(Text.translatable("gui.morewathe.config.category.options.roles.guardian_angel.guardianDefaultsToAmnesiac.desc").styled(style -> style.withColor(16777215))))
                            .binding(true, () -> ConfigComponent.KEY.get(MinecraftClient.getInstance().player.getWorld()).guardianDefaultsToAmnesiac, value -> sendCommand.accept("morewathe guardianDefaultsToAmnesiac " + value, parent))
                            .controller(option -> BooleanControllerBuilder.create(option).coloured(true))
                            .build());
                    for (Identifier deathReason : DeathReasonLoader.reasons) {
                        builder.option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("gui.morewathe.config.category.options.roles.guardian_angel.guardianProtectsDeath").append(Text.translatable("death_reason." + deathReason.getNamespace() + "." + deathReason.getPath())))
                                .binding(DeathReasonLoader.defaultProtections.contains(deathReason), () -> ConfigComponent.KEY.get(MinecraftClient.getInstance().player.getWorld()).protectedDeathReasons.contains(deathReason.toString()), value -> sendCommand.accept("morewathe guardianProtectsDeath " + deathReason + " " + value, parent))
                                .controller(option -> BooleanControllerBuilder.create(option).coloured(true))
                                .build());
                    }
                    break;
                case "morewathe:investigator":
                    builder.option(Option.<Integer>createBuilder()
                            .name(Text.translatable("gui.morewathe.config.category.options.roles.investigator.maxFootprints"))
                            .description(OptionDescription.of(Text.translatable("gui.morewathe.config.category.options.roles.investigator.maxFootprints.desc").styled(style -> style.withColor(16777215))))
                            .binding(30, () -> ConfigComponent.KEY.get(MinecraftClient.getInstance().player.getWorld()).maxFootprints, value -> sendCommand.accept("morewathe maxFootprints " + value, parent))
                            .controller(option -> IntegerSliderControllerBuilder.create(option).range(1, 200).step(1))
                            .build());
                    break;
            }
        }
    }
}
