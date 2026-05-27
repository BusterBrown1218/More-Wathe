package xyz.busterbrown1218.more_wathe.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.wathe.client.WatheClient;
import dev.doctor4t.wathe.client.gui.MoodRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.busterbrown1218.more_wathe.config.ConfigComponent;

@Mixin(MoodRenderer.class)
public class MoodRenderMixin {
    @WrapOperation(method = "renderHud", at = @At(value = "INVOKE", target = "Ldev/doctor4t/wathe/client/gui/MoodRenderer;renderCivilian(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/client/gui/DrawContext;F)V"))
    private static void renderCivilian(TextRenderer textRenderer, DrawContext context, float prevMood, Operation<Void> original) {
        if (MinecraftClient.getInstance().player == null) return;
        if (!ConfigComponent.KEY.get(MinecraftClient.getInstance().player.getWorld()).hideMood) original.call(textRenderer, context, prevMood);
    }

    @WrapOperation(method = "renderHud", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I"))
    private static int drawText(DrawContext instance, TextRenderer textRenderer, Text text, int x, int y, int color, Operation<Integer> original) {
        if (MinecraftClient.getInstance().player == null) return original.call(instance, textRenderer, text, x, y, color);
        if (ConfigComponent.KEY.get(MinecraftClient.getInstance().player.getWorld()).hideMood && !WatheClient.isKiller()) {
            x = 5;
        }
        return original.call(instance, textRenderer, text, x, y, color);
    }
}
