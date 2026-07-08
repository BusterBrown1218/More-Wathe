package xyz.busterbrown1218.more_wathe.components;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;
import xyz.busterbrown1218.more_wathe.MoreWathe;
import xyz.busterbrown1218.more_wathe.config.ConfigFile;

import java.util.HashMap;

public class ConfigComponent implements AutoSyncedComponent, ServerTickingComponent {
    public static final ComponentKey<ConfigComponent> KEY = ComponentRegistry.getOrCreate(Identifier.of(MoreWathe.MOD_ID, "config"), ConfigComponent.class);

    private World world;
    public ConfigComponent(World world) {
        this.world = world;
    }
    @Override
    public void serverTick() {
        KEY.sync(world);
    }

    public boolean hideMood = false;
    public int guardianKillerChance = 50;
    public boolean guardianDefaultsToAmnesiac = true;
    public HashMap<String, Double> modifierChances = new HashMap<>();
    public HashMap<String, Double> roleChances = new HashMap<>();

    @Override
    public void readFromNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        if (nbtCompound.contains("hideMood")) hideMood = nbtCompound.getBoolean("hideMood");
        if (nbtCompound.contains("guardianKillerChance")) guardianKillerChance = nbtCompound.getInt("guardianKillerChance");
        if (nbtCompound.contains("guardianDefaultsToAmnesiac")) guardianDefaultsToAmnesiac = nbtCompound.getBoolean("guardianDefaultsToAmnesiac");

        for (String key : nbtCompound.getKeys()) {
            if (key.startsWith("modifierChance_")) {
                modifierChances.put(key.substring(15), nbtCompound.getDouble(key));
            } else if (key.startsWith("roleChance_")) {
                roleChances.put(key.substring(11), nbtCompound.getDouble(key));
            }
        }
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        hideMood = ConfigFile.HANDLER.instance().hideMood;
        guardianKillerChance = ConfigFile.HANDLER.instance().guardianKillerChance;
        guardianDefaultsToAmnesiac = ConfigFile.HANDLER.instance().guardianDefaultsToAmnesiac;
        modifierChances = ConfigFile.HANDLER.instance().modifierChances;
        roleChances = ConfigFile.HANDLER.instance().roleChances;

        nbtCompound.putBoolean("hideMood", hideMood);
        nbtCompound.putInt("guardianKillerChance", guardianKillerChance);
        nbtCompound.putBoolean("guardianDefaultsToAmnesiac", guardianDefaultsToAmnesiac);
        for (String id : modifierChances.keySet()) {
            nbtCompound.putDouble("modifierChance_" + id, modifierChances.get(id));
        }
        for (String id : roleChances.keySet()) {
            nbtCompound.putDouble("roleChance_" + id, roleChances.get(id));
        }
    }
}
