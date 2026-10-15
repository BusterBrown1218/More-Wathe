package xyz.busterbrown1218.more_wathe;

import net.minecraft.entity.player.PlayerEntity;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.ladysnake.cca.api.v3.world.WorldComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentInitializer;
import xyz.busterbrown1218.more_wathe.components.*;

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
        registry.beginRegistration(PlayerEntity.class, GuardianAngelPlayerComponent.KEY).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(GuardianAngelPlayerComponent::new);
        registry.beginRegistration(PlayerEntity.class, TelepathPlayerComponent.KEY).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(TelepathPlayerComponent::new);
        registry.beginRegistration(PlayerEntity.class, FootprintPlayerComponent.KEY).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(FootprintPlayerComponent::new);
        registry.beginRegistration(PlayerEntity.class, InvestigatorPlayerComponent.KEY).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(InvestigatorPlayerComponent::new);
    }
}
