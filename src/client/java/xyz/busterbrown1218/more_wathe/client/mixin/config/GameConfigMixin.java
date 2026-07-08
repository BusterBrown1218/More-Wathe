package xyz.busterbrown1218.more_wathe.client.mixin.config;

import cat.rezelyn.watheextended.client.screen.config.OptionsCategory;
import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.yacl3.api.LabelOption;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.busterbrown1218.more_wathe.components.ConfigComponent;

import java.util.function.BiConsumer;

@Mixin(OptionsCategory.class)
public class GameConfigMixin {
    @Inject(method = "buildGamerulesGroup", at = @At(value = "TAIL", shift = At.Shift.BEFORE))
    private static void addGamerules(Screen parent, BiConsumer<String, Screen> sendCommand, CallbackInfoReturnable<OptionGroup> cir, @Local(name = "group") OptionGroup.Builder group) {
        group.option(Option.<Boolean>createBuilder()
                .name(Text.translatable("gui.morewathe.config.category.options.opt.hideMood"))
                .description(OptionDescription.of(Text.translatable("gui.morewathe.config.category.options.opt.hideMood.desc").styled((style) -> style.withColor(16777215))))
                .binding(false, () -> ConfigComponent.KEY.get(MinecraftClient.getInstance().player.getWorld()).hideMood, value -> sendCommand.accept("morewathe hideMood " + value, parent))
                .controller((opt) -> BooleanControllerBuilder.create(opt).coloured(true).formatValue((v) -> Text.translatable(v ? "text.watheextended.enabled" : "text.watheextended.disabled")))
                .build());
    }

    @Inject(method = "buildRolesOptionsGroup", at = @At(value = "TAIL", shift = At.Shift.BEFORE))
    private static void addRoleOptions(Screen parent, BiConsumer<String, Screen> sendCommand, CallbackInfoReturnable<OptionGroup> cir, @Local(name = "group") OptionGroup.Builder group) {
        group.option(LabelOption.create(Text.translatable("gui.morewathe.config.category.options.roles.guardian_angel.group").styled((s) -> s.withColor(11184810))));
        group.option(Option.<Integer>createBuilder()
                .name(Text.translatable("gui.morewathe.config.category.options.roles.guardian_angel.killer_chance"))
                .description(OptionDescription.of(Text.translatable("gui.morewathe.config.category.options.roles.guardian_angel.killer_chance.desc").styled(style -> style.withColor(16777215))))
                .binding(50, () -> ConfigComponent.KEY.get(MinecraftClient.getInstance().player.getWorld()).guardianKillerChance, value -> sendCommand.accept("morewathe guardianKillerChance " + value, parent))
                .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(0, 100).step(1))
                .build());
        group.option(Option.<Boolean>createBuilder()
                .name(Text.translatable("gui.morewathe.config.category.options.roles.guardian_angel.guardianDefaultsToAmnesiac"))
                .description(OptionDescription.of(Text.translatable("gui.morewathe.config.category.options.roles.guardian_angel.guardianDefaultsToAmnesiac.desc").styled(style -> style.withColor(16777215))))
                .binding(true, () -> ConfigComponent.KEY.get(MinecraftClient.getInstance().player.getWorld()).guardianDefaultsToAmnesiac, value -> sendCommand.accept("morewathe guardianDefaultsToAmnesiac " + value, parent))
                .controller(option -> BooleanControllerBuilder.create(option).coloured(true))
                .build());
    }
}
