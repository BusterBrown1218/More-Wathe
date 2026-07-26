package xyz.busterbrown1218.more_wathe.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.wathe.api.Role;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.agmas.harpymodloader.Harpymodloader;
import org.agmas.harpymodloader.modded_murder.ModdedMurderGameMode;
import org.agmas.harpymodloader.modifiers.HMLModifiers;
import org.agmas.harpymodloader.modifiers.Modifier;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.busterbrown1218.more_wathe.MoreWathe;
import xyz.busterbrown1218.more_wathe.config.ConfigFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Mixin(ModdedMurderGameMode.class)
public class RoleAndModifierWeights {
    @Inject(method = "initializeGame", at = @At(value = "HEAD"))
    private void checkPlayerCount(ServerWorld serverWorld, GameWorldComponent gameWorldComponent, List<ServerPlayerEntity> players, CallbackInfo ci) {
        // sorry if this causes trouble to other mods that set role/modifier maximums
        if (players.size() < 8) {
            Harpymodloader.setRoleMaximum(MoreWathe.GUARDIAN_ANGEL, 0);
        } else {
            Harpymodloader.setRoleMaximum(MoreWathe.GUARDIAN_ANGEL, 1);
        }
    }

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
