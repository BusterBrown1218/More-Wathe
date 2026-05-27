package xyz.busterbrown1218.more_wathe.config;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;
import xyz.busterbrown1218.more_wathe.MoreWathe;

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

    @Override
    public void readFromNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        if (nbtCompound.contains("hideMood")) hideMood = nbtCompound.getBoolean("hideMood");
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        hideMood = ConfigFile.HANDLER.instance().hideMood;
        nbtCompound.putBoolean("hideMood", hideMood);
    }
}
