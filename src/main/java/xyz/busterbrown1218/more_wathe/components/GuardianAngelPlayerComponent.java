package xyz.busterbrown1218.more_wathe.components;

import dev.doctor4t.wathe.game.GameConstants;
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

import java.util.UUID;

public class GuardianAngelPlayerComponent implements AutoSyncedComponent, ServerTickingComponent, ClientTickingComponent {
    public static final ComponentKey<GuardianAngelPlayerComponent> KEY = ComponentRegistry.getOrCreate(Identifier.of(MoreWathe.MOD_ID, "guardian_angel"), GuardianAngelPlayerComponent.class);
    private final PlayerEntity player;
    public short isProtected = 0;
    public boolean hasGuardian = false;
    public UUID protecting;

    public GuardianAngelPlayerComponent(PlayerEntity playerEntity) {
        player = playerEntity;
    }

    @Override
    public void clientTick() {

    }

    public void sync() {
        KEY.sync(this.player);
    }
    public void reset() {
        isProtected = 0;
        hasGuardian = false;
        protecting = null;
        sync();
    }

    @Override
    public void serverTick() {
        if (isProtected > 0) {
            isProtected -= 1;
            sync();
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        isProtected = tag.contains("protected") ? tag.getShort("protected") : 0;
        hasGuardian = tag.contains("guarded") ? tag.getBoolean("guarded") : false;
        protecting = tag.contains("protecting") ? tag.getUuid("protecting") : null;
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putShort("protected", isProtected);
        tag.putBoolean("guarded", hasGuardian);
        if (protecting != null) tag.putUuid("protecting", protecting);
    }

    public void startProtecting() {
        GuardianAngelPlayerComponent willBeProtected = GuardianAngelPlayerComponent.KEY.get(this.player.getWorld().getPlayerByUuid(protecting));
        willBeProtected.isProtected = (short) GameConstants.getInTicks(0, 20);
        willBeProtected.sync();
    }
}
