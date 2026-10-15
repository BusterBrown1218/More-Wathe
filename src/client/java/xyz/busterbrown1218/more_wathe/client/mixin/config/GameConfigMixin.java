package xyz.busterbrown1218.more_wathe.client.mixin.config;

import cat.rezelyn.watheextended.client.screen.config.GameCategory;
import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.busterbrown1218.more_wathe.components.ConfigComponent;

import java.util.function.BiConsumer;

@Mixin(GameCategory.class)
public class GameConfigMixin {
    @Inject(method = "gamerulesGroup", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Ldev/isxander/yacl3/api/LabelOption;create(Lnet/minecraft/text/Text;)Ldev/isxander/yacl3/api/LabelOption;", ordinal = 0))
    private static void addGamerules(Screen parent, BiConsumer<String, Screen> sendCommand, CallbackInfoReturnable<OptionGroup> cir, @Local(name = "group") OptionGroup.Builder group) {
        group.option(Option.<Boolean>createBuilder()
                .name(Text.translatable("gui.morewathe.config.category.options.opt.hideMood"))
                .description(OptionDescription.of(Text.translatable("gui.morewathe.config.category.options.opt.hideMood.desc").styled((style) -> style.withColor(16777215))))
                .binding(false, () -> ConfigComponent.KEY.get(MinecraftClient.getInstance().player.getWorld()).hideMood, value -> sendCommand.accept("morewathe hideMood " + value, parent))
                .controller((opt) -> BooleanControllerBuilder.create(opt).coloured(true))
                .build());
    }
}
