package xyz.busterbrown1218.more_wathe;

import net.minecraft.entity.player.PlayerEntity;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.ladysnake.cca.api.v3.world.WorldComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentInitializer;
import xyz.busterbrown1218.more_wathe.components.AbilityPlayerComponent;
import xyz.busterbrown1218.more_wathe.components.ConfigComponent;
import xyz.busterbrown1218.more_wathe.components.EscapistPlayerComponent;

public class MoreWatheComponents implements EntityComponentInitializer, WorldComponentInitializer {
    public MoreWatheComponents() {

    }
    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry worldComponentFactoryRegistry) {
        worldComponentFactoryRegistry.register(ConfigComponent.KEY, ConfigComponent::new);
    }

    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(PlayerEntity.class, EscapistPlayerComponent.KEY).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(EscapistPlayerComponent::new);
        registry.beginRegistration(PlayerEntity.class, AbilityPlayerComponent.KEY).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(AbilityPlayerComponent::new);
    }
}
