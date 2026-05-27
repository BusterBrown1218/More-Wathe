package xyz.busterbrown1218.more_wathe;

import org.ladysnake.cca.api.v3.world.WorldComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentInitializer;
import xyz.busterbrown1218.more_wathe.config.ConfigComponent;

public class MoreWatheComponents implements WorldComponentInitializer {
    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry worldComponentFactoryRegistry) {
        worldComponentFactoryRegistry.register(ConfigComponent.KEY, ConfigComponent::new);
    }
}
