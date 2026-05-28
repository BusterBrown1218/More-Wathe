package xyz.busterbrown1218.more_wathe.client.mixin;

import cat.rezelyn.watheextended.client.screen.config.ScreenUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ScreenUtils.class)
public class ExtendedScreenMixin {
    @Inject(method = "modsNamespace", at = @At("TAIL"), cancellable = true)
    private static void namespace(String namespace, CallbackInfoReturnable<String> cir) {
        if (namespace.equals("morewathe")) {
            cir.setReturnValue("More Wathe");
        }
    }
}
