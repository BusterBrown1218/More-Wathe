package xyz.busterbrown1218.more_wathe.client.mixin;

import cat.rezelyn.watheextended.client.screen.config.ClientCategory;
import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.busterbrown1218.more_wathe.config.ExtendedConfigHelper;

import java.util.function.BiConsumer;

@Mixin(ClientCategory.class)
public class ExtendedClientConfigMixin {
    @Inject(method = "build", at = @At(value = "INVOKE", target = "Lcat/rezelyn/watheextended/api/kinswathe/ConfigHelper;isLoaded()Z"))
    private static void build(Screen parent, boolean isOp, BiConsumer<String, Screen> sendCommand, CallbackInfoReturnable<ConfigCategory> cir, @Local(name = "builder") ConfigCategory.Builder builder) {
        builder.option(Option.<Boolean>createBuilder()
                .name(Text.translatable("gui.morewathe.config.category.client.opt.hideDisabledRoles"))
                .description(OptionDescription.of(Text.translatable("gui.morewathe.config.category.client.opt.hideDisabledRoles.desc")))
                .binding(true, ExtendedConfigHelper::getHideDisabledRoles, ExtendedConfigHelper::setHideDisabledRoles)
                .controller(option -> BooleanControllerBuilder.create(option).coloured(true))
                .build());
        builder.option(Option.<Boolean>createBuilder()
                .name(Text.translatable("gui.morewathe.config.category.client.opt.hideDisabledModifiers"))
                .description(OptionDescription.of(Text.translatable("gui.morewathe.config.category.client.opt.hideDisabledModifiers.desc")))
                .binding(true, ExtendedConfigHelper::getHideDisabledModifiers, ExtendedConfigHelper::setHideDisabledModifiers)
                .controller(option -> BooleanControllerBuilder.create(option).coloured(true))
                .build());
    }
}
