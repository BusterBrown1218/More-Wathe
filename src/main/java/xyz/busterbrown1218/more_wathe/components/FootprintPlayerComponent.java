package xyz.busterbrown1218.more_wathe.components;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.game.GameFunctions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;
import xyz.busterbrown1218.more_wathe.MoreWathe;
import xyz.busterbrown1218.more_wathe.config.ConfigFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class FootprintPlayerComponent implements AutoSyncedComponent, ServerTickingComponent {
    public static final ComponentKey<FootprintPlayerComponent> KEY = ComponentRegistry.getOrCreate(Identifier.of(MoreWathe.MOD_ID, "footstep"), FootprintPlayerComponent.class);
    private final PlayerEntity player;
    private List<Integer> xLocations = new ArrayList<>();
    private List<Integer> yLocations = new ArrayList<>();
    private List<Integer> zLocations = new ArrayList<>();
    private int count = 0;
    private static final Random rand = new Random();

    public FootprintPlayerComponent(PlayerEntity player) {
        this.player = player;
    }

    public void sync() {
        KEY.sync(this.player);
    }

    public List<Vec3d> getLocations() {
        List<Vec3d> coordinates = new ArrayList<>();
        for (int i = 0; i < xLocations.size(); i++) {
            Vec3d coordinate = new Vec3d((double) xLocations.get(i) / 16, (double) yLocations.get(i) / 8, (double) zLocations.get(i) / 16).add(0, 0.01, 0);
            coordinates.add(coordinate);
        }
        return coordinates;
    }

    public void reset() {
        xLocations.clear();
        yLocations.clear();
        zLocations.clear();
        sync();
    }

    @Override
    public void serverTick() {
        if (!(GameWorldComponent.KEY.get(player.getWorld()).getGameStatus().equals(GameWorldComponent.GameStatus.ACTIVE) && GameFunctions.isPlayerAliveAndSurvival(player))) return;
        count++;
        if (count == 5) {
            count = 0;
            if (xLocations.size() > ConfigFile.HANDLER.instance().maxFootprints) {
                xLocations.removeFirst();
                yLocations.removeFirst();
                zLocations.removeFirst();
            }
            xLocations.add((int) (player.getPos().x * 16) + rand.nextInt(-2, 3));
            yLocations.add((int) (player.getPos().y * 8));
            zLocations.add((int) (player.getPos().z * 16) + rand.nextInt(-2, 3));
            sync();
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (tag.contains("xLocations")) xLocations = Arrays.stream(tag.getIntArray("xLocations")).boxed().collect(Collectors.toCollection(ArrayList::new));
        if (tag.contains("yLocations")) yLocations = Arrays.stream(tag.getIntArray("yLocations")).boxed().collect(Collectors.toCollection(ArrayList::new));
        if (tag.contains("zLocations")) zLocations = Arrays.stream(tag.getIntArray("zLocations")).boxed().collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putIntArray("xLocations", xLocations);
        tag.putIntArray("yLocations", yLocations);
        tag.putIntArray("zLocations", zLocations);
    }
}
