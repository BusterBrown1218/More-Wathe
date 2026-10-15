package xyz.busterbrown1218.more_wathe.components;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import xyz.busterbrown1218.more_wathe.MoreWathe;

import java.util.List;
import java.util.UUID;

public class InvestigatorPlayerComponent implements AutoSyncedComponent {
    public static final ComponentKey<InvestigatorPlayerComponent> KEY = ComponentRegistry.getOrCreate(Identifier.of(MoreWathe.MOD_ID, "investigator"), InvestigatorPlayerComponent.class);
    private final PlayerEntity player;
    private List<UUID> playersToObserve;
    private UUID investigatedPlayer = null;

    public InvestigatorPlayerComponent(PlayerEntity player) {
        this.player = player;
    }

    public void sync() {
        KEY.sync(player);
    }

    public void reset() {
        investigatedPlayer = null;
        playersToObserve = GameWorldComponent.KEY.get(player.getWorld()).getRoles().keySet().stream().toList();
        sync();
    }

    public PlayerEntity getInvestigatedPlayer() {
        if (investigatedPlayer == null) return null;
        return player.getWorld().getPlayerByUuid(investigatedPlayer);
    }

    public void cycleInvestigatedPlayer() {
        int index = investigatedPlayer == null ? -1 : playersToObserve.indexOf(investigatedPlayer);
        investigatedPlayer = null;
        while (getInvestigatedPlayer() == null) {
            investigatedPlayer = playersToObserve.get((index + 1) % playersToObserve.size());
            index += 1;
        }
        player.sendMessage(Text.translatable("tip.investigator.observe", getInvestigatedPlayer().getDisplayName()), true);
        sync();
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (tag.contains("investigated")) investigatedPlayer = tag.getUuid("investigated");
        else investigatedPlayer = null;
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (investigatedPlayer == null) {
            if (tag.contains("investigated")) tag.remove("investigated");
        } else tag.putUuid("investigated", investigatedPlayer);
    }
}
