package xyz.busterbrown1218.more_wathe.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.wathe.api.Role;
import org.agmas.harpymodloader.modded_murder.ModdedMurderGameMode;
import org.agmas.harpymodloader.modifiers.HMLModifiers;
import org.agmas.harpymodloader.modifiers.Modifier;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.busterbrown1218.more_wathe.config.ConfigFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Mixin(ModdedMurderGameMode.class)
public class RoleAndModifierWeights {
    @Redirect(method = "assignModifiers", at = @At(value = "FIELD", target = "Lorg/agmas/harpymodloader/modifiers/HMLModifiers;MODIFIERS:Ljava/util/ArrayList;", opcode = Opcodes.GETSTATIC))
    private ArrayList<Modifier> shuffledModifiers() {
        ArrayList<Modifier> reshuffledModifiers = new ArrayList<>(HMLModifiers.MODIFIERS);

        HashMap<String, Double> weights = ConfigFile.HANDLER.instance().modifierChances;

        Random rand = new Random();
        reshuffledModifiers.sort((modifier1, modifier2) -> {
            double weight1 = weights.getOrDefault(modifier1.identifier().toString(), 1.0);
            double weight2 = weights.getOrDefault(modifier2.identifier().toString(), 1.0);

            double score1 = Math.pow(rand.nextDouble(), 1.0 / weight1);
            double score2 = Math.pow(rand.nextDouble(), 1.0 / weight2);

            return Double.compare(score2, score1);
        });

        return reshuffledModifiers;
    }
    @WrapOperation(method = "assignCivilianReplacingRoles", at = @At(value = "INVOKE", target = "Ljava/util/Collections;shuffle(Ljava/util/List;)V"))
    private static void shuffleNonKillers(List<Role> list, Operation<Void> original) {
        HashMap<String, Double> weights = ConfigFile.HANDLER.instance().roleChances;

        Random rand = new Random();
        list.sort((role1, role2) -> {
            double weight1 = weights.getOrDefault(role1.identifier().toString(), 1.0);
            double weight2 = weights.getOrDefault(role2.identifier().toString(), 1.0);

            double score1 = Math.pow(rand.nextDouble(), 1.0 / weight1);
            double score2 = Math.pow(rand.nextDouble(), 1.0 / weight2);

            return Double.compare(score2, score1);
        });
    }

    @WrapOperation(method = "assignKillerReplacingRoles", at = @At(value = "INVOKE", target = "Ljava/util/Collections;shuffle(Ljava/util/List;)V"))
    private static void shuffleKillers(List<Role> list, Operation<Void> original) {
        HashMap<String, Double> weights = ConfigFile.HANDLER.instance().roleChances;

        Random rand = new Random();
        list.sort((role1, role2) -> {
            double weight1 = weights.getOrDefault(role1.identifier().toString(), 1.0);
            double weight2 = weights.getOrDefault(role2.identifier().toString(), 1.0);

            double score1 = Math.pow(rand.nextDouble(), 1.0 / weight1);
            double score2 = Math.pow(rand.nextDouble(), 1.0 / weight2);

            return Double.compare(score2, score1);
        });
    }
}
