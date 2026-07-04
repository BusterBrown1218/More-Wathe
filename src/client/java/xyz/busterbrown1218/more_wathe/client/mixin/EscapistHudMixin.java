package xyz.busterbrown1218.more_wathe.client.mixin;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.cca.PlayerShopComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.busterbrown1218.more_wathe.MoreWathe;
import xyz.busterbrown1218.more_wathe.MoreWatheComponents;
import xyz.busterbrown1218.more_wathe.client.MoreWatheClient;
import xyz.busterbrown1218.more_wathe.components.AbilityPlayerComponent;
import xyz.busterbrown1218.more_wathe.components.EscapistPlayerComponent;

@Mixin(InGameHud.class)
public abstract class EscapistHudMixin {
    // straight from Noelle's roles
    @Shadow
    public abstract TextRenderer getTextRenderer();

    @Inject(method = "render", at = @At("TAIL"))
    public void phantomHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        GameWorldComponent gameWorldComponent = (GameWorldComponent) GameWorldComponent.KEY.get(MinecraftClient.getInstance().player.getWorld());
        AbilityPlayerComponent abilityPlayerComponent = (AbilityPlayerComponent) AbilityPlayerComponent.KEY.get(MinecraftClient.getInstance().player);
        EscapistPlayerComponent escapistPlayerComponent = EscapistPlayerComponent.KEY.get(MinecraftClient.getInstance().player);
        PlayerShopComponent playerShopComponent = PlayerShopComponent.KEY.get(MinecraftClient.getInstance().player);
        if (gameWorldComponent.isRole(MinecraftClient.getInstance().player, MoreWathe.ESCAPIST)) {
            int drawY = context.getScaledWindowHeight();


            Text line = Text.translatable("tip.escapist.teleport", MoreWatheClient.abilityKeyBind.getBoundKeyLocalizedText());
            if (!escapistPlayerComponent.placed) {
                line = Text.translatable("tip.escapist.place", MoreWatheClient.abilityKeyBind.getBoundKeyLocalizedText());
            } else {
                if (playerShopComponent.balance < 100) {
                    line = Text.translatable("tip.escapist.not_enough_money");
                }
            }

            if (abilityPlayerComponent.cooldown > 0) {
                line = Text.translatable("tip.morewathe.cooldown", abilityPlayerComponent.cooldown/20);
            }

            drawY -= getTextRenderer().getWrappedLinesHeight(line, 999999);
            context.drawTextWithShadow(getTextRenderer(), line, context.getScaledWindowWidth() - getTextRenderer().getWidth(line), drawY, MoreWathe.ESCAPIST.color());
        }
    }
}
