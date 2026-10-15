package xyz.busterbrown1218.more_wathe.components;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;
import xyz.busterbrown1218.more_wathe.MoreWathe;

public class EscapistPlayerComponent implements AutoSyncedComponent, ServerTickingComponent {
    public static final ComponentKey<EscapistPlayerComponent> KEY = ComponentRegistry.getOrCreate(Identifier.of(MoreWathe.MOD_ID, "escapist"), EscapistPlayerComponent.class);
    private final PlayerEntity player;
    public boolean placed = false;
    public double x = 0;
    public double y = 0;
    public double z = 0;

    public void reset() {
        this.placed = false;
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.sync();
    }

    public EscapistPlayerComponent(PlayerEntity player) {
        this.player = player;
    }

    public void sync() {
        KEY.sync(this.player);
    }

    public void serverTick() {
        this.sync();
    }

    public void setPosition() {
        x = player.getX();
        y = player.getY();
        z = player.getZ();
        placed = true;
        this.sync();
    }

    public void teleport() {
        player.refreshPositionAfterTeleport(x,y,z);
        placed = false;
        this.sync();
    }


    public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putDouble("x", this.x);
        tag.putDouble("y", this.y);
        tag.putDouble("z", this.z);
        tag.putBoolean("placed", this.placed);
    }

    public void readFromNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.x = tag.contains("x") ? tag.getDouble("x") : 0;
        this.y = tag.contains("y") ? tag.getDouble("y") : 0;
        this.z = tag.contains("z") ? tag.getDouble("z") : 0;
        this.placed = tag.contains("placed") && tag.getBoolean("placed");
    }
}
