package xyz.busterbrown1218.more_wathe.client.mixin;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.cca.PlayerShopComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.agmas.harpymodloader.component.WorldModifierComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.busterbrown1218.more_wathe.MoreWathe;
import xyz.busterbrown1218.more_wathe.client.MoreWatheClient;
import xyz.busterbrown1218.more_wathe.components.AbilityPlayerComponent;
import xyz.busterbrown1218.more_wathe.components.EscapistPlayerComponent;
import xyz.busterbrown1218.more_wathe.components.GuardianAngelPlayerComponent;
import xyz.busterbrown1218.more_wathe.components.TelepathPlayerComponent;

import java.awt.*;

@Mixin(InGameHud.class)
public abstract class GameHudMixin {
    @Shadow
    public abstract TextRenderer getTextRenderer();

    @Inject(method = "renderMainHud", at = @At("TAIL"))
    public void drawHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        GameWorldComponent gameWorldComponent = (GameWorldComponent) GameWorldComponent.KEY.get(player.getWorld());
        if (gameWorldComponent.isRole(player, MoreWathe.ESCAPIST)) drawEscapistHud(context);
        if (gameWorldComponent.isRole(player, MoreWathe.GUARDIAN_ANGEL)) drawGuardianHud(context, player);
        if (GuardianAngelPlayerComponent.KEY.get(player).hasGuardian) drawGuardedHud(context);
        if (WorldModifierComponent.KEY.get(player.getWorld()).isModifier(player, MoreWathe.TELEPATH)) drawTelepathHud(context, player, tickCounter.getTickDelta(true));
    }

    // straight from Noelle's roles
    @Unique
    private void drawEscapistHud(DrawContext context) {
        AbilityPlayerComponent abilityPlayerComponent = (AbilityPlayerComponent) AbilityPlayerComponent.KEY.get(MinecraftClient.getInstance().player);
        EscapistPlayerComponent escapistPlayerComponent = EscapistPlayerComponent.KEY.get(MinecraftClient.getInstance().player);
        PlayerShopComponent playerShopComponent = PlayerShopComponent.KEY.get(MinecraftClient.getInstance().player);
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
        context.drawTextWithShadow(getTextRenderer(), line, context.getScaledWindowWidth() - getTextRenderer().getWidth(line) - 1, drawY, MoreWathe.ESCAPIST.color());
    }

    @Unique
    void drawGuardianHud(DrawContext context, PlayerEntity player) {
        AbilityPlayerComponent abilityPlayerComponent = (AbilityPlayerComponent) AbilityPlayerComponent.KEY.get(player);
        GuardianAngelPlayerComponent guardianAngelPlayerComponent = (GuardianAngelPlayerComponent) GuardianAngelPlayerComponent.KEY.get(player);
        PlayerShopComponent playerShopComponent = (PlayerShopComponent) PlayerShopComponent.KEY.get(player);
        int drawY = context.getScaledWindowHeight();


        Text line = Text.translatable("tip.guardian_angel.protect", MoreWatheClient.abilityKeyBind.getBoundKeyLocalizedText());
        if (playerShopComponent.balance < 100) {
            line = Text.translatable("tip.guardian_angel.not_enough_money");
        }
        if (abilityPlayerComponent.cooldown > 0) {
            line = Text.translatable("tip.morewathe.cooldown", abilityPlayerComponent.cooldown/20);
        }

        drawY -= getTextRenderer().getWrappedLinesHeight(line, 999999);
        context.drawTextWithShadow(getTextRenderer(), line, context.getScaledWindowWidth() - getTextRenderer().getWidth(line) - 1, drawY, MoreWathe.GUARDIAN_ANGEL.color());
        if (guardianAngelPlayerComponent.protecting == null) return;
        PlayerEntity protectee = player.getWorld().getPlayerByUuid(guardianAngelPlayerComponent.protecting);
        if (protectee == null) return;
        Text objectiveText = Text.translatable("tip.guardian_angel.objective", protectee.getDisplayName());
        context.drawTextWithShadow(getTextRenderer(), objectiveText, context.getScaledWindowWidth() - getTextRenderer().getWidth(objectiveText) - 1, 16, MoreWathe.GUARDIAN_ANGEL.color());
    }
    @Unique
    void drawGuardedHud(DrawContext context) {
        GuardianAngelPlayerComponent guardianAngelPlayerComponent = GuardianAngelPlayerComponent.KEY.get(MinecraftClient.getInstance().player);

        Text line = Text.translatable("tip.has_guardian_angel");
        if (guardianAngelPlayerComponent.isProtected > 0) line = Text.translatable("tip.has_guardian_angel.protected", guardianAngelPlayerComponent.isProtected / 20);

        context.drawTextWithShadow(getTextRenderer(), line, context.getScaledWindowWidth() - getTextRenderer().getWidth(line) - 1, 16, MoreWathe.GUARDIAN_ANGEL.color());
    }

    @Unique
    void drawTelepathHud(DrawContext context, PlayerEntity player, float delta) {
        TelepathPlayerComponent telepathPlayerComponent = TelepathPlayerComponent.KEY.get(player);

        for (int i = 0; i < telepathPlayerComponent.deaths.size(); i++) {
            TelepathPlayerComponent.TelepathDeath death = telepathPlayerComponent.deaths.get(i);

            String text = "§c" + death.displayName + "§7 "
                    + Text.translatable("hud.telepath.info").getString() + " "
                    + Text.translatable("death_reason." + death.reason.getNamespace() + "." + death.reason.getPath()).getString();

            context.drawTextWithShadow(getTextRenderer(), Text.of(text), 6, 26 + getTextRenderer().fontHeight * i, 0xFFFFFF);
        }
    }
}
