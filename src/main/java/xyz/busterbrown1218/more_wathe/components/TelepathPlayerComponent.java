package xyz.busterbrown1218.more_wathe.components;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ClientTickingComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;
import xyz.busterbrown1218.more_wathe.MoreWathe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TelepathPlayerComponent implements AutoSyncedComponent, ServerTickingComponent, ClientTickingComponent {
    public static final ComponentKey<TelepathPlayerComponent> KEY = ComponentRegistry.getOrCreate(Identifier.of(MoreWathe.MOD_ID, "telepath"), TelepathPlayerComponent.class);
    private final PlayerEntity player;
    public List<TelepathDeath> deaths = new ArrayList<>();

    public TelepathPlayerComponent(PlayerEntity player) {
        this.player = player;
    }

    public void reset() {
        deaths.clear();
        this.sync();
    }

    public void sync() {
        KEY.sync(this.player);
    }

    @Override
    public void clientTick() {
        commonTick();
    }

    @Override
    public void serverTick() {
        commonTick();
    }

    private void commonTick() {
        deaths.removeIf(telepathDeath -> {
            telepathDeath.displayTime -= 1;
            return telepathDeath.displayTime <= 0;
        });
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        deaths.clear();
        for (String key : tag.getKeys()) {
            if (tag.getInt(key) <= 0) {
                tag.remove(key);
                continue;
            }
            String displayName = key.substring(0, key.indexOf(":"));
            Identifier reason = Identifier.of(key.substring(key.indexOf(":") + 1));

            deaths.add(new TelepathDeath(reason, displayName, tag.getInt(key)));
        }
        deaths.sort(Comparator.comparingInt(TelepathDeath::getDisplayTime));
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        for (String key : tag.getKeys()) {
            tag.remove(key);
        }
        for (TelepathDeath death : deaths) {
            tag.putInt(death.displayName + ":" + death.reason.toString(), death.displayTime);
        }
    }

    public static class TelepathDeath {
        public Identifier reason;
        public String displayName;
        public int displayTime;
        public TelepathDeath(Identifier reason, String displayName, int displayTime) {
            this.reason = reason;
            this.displayName = displayName;
            this.displayTime = displayTime;
        }

        public int getDisplayTime() {
            return displayTime;
        }
    }
}
