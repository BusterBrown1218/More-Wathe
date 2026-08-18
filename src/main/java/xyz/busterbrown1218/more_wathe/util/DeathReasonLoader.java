package xyz.busterbrown1218.more_wathe.util;

import dev.doctor4t.wathe.game.GameConstants.DeathReasons;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.agmas.noellesroles.Noellesroles;
import pro.fazeclan.river.stupid_express.StupidExpress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeathReasonLoader {
    public static List<Identifier> reasons = new ArrayList<>();

    public static final List<Identifier> defaultProtections = new ArrayList<>(Arrays.asList(DeathReasons.BAT, DeathReasons.GRENADE, DeathReasons.GUN, DeathReasons.KNIFE, DeathReasons.POISON));

    public static void loadReasons() {
        reasons.add(DeathReasons.GRENADE);
        reasons.add(DeathReasons.KNIFE);
        reasons.add(DeathReasons.BAT);
        reasons.add(DeathReasons.FELL_OUT_OF_TRAIN);
        reasons.add(DeathReasons.GENERIC);
        reasons.add(DeathReasons.GUN);
        reasons.add(DeathReasons.POISON);
        if (FabricLoader.getInstance().isModLoaded("noellesroles")) {
            reasons.add(Noellesroles.INFECTION_DEATH_REASON);
            reasons.add(Noellesroles.VOODOO_MAGIC_DEATH_REASON);
        }
        if (FabricLoader.getInstance().isModLoaded("stupid_express")) {
            reasons.add(StupidExpress.id("ignited"));
            reasons.add(StupidExpress.id("broken_heart"));
        }
    }
}
