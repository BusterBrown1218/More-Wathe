package xyz.busterbrown1218.more_wathe.particle;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import xyz.busterbrown1218.more_wathe.MoreWathe;

public class MoreWatheParticles {
    public static final SimpleParticleType FOOTPRINT_PARTICLE = registerParticle("footprint", FabricParticleTypes.simple());

    private static SimpleParticleType registerParticle(String name, SimpleParticleType particleType) {
        return Registry.register(Registries.PARTICLE_TYPE, Identifier.of(MoreWathe.MOD_ID, name), particleType);
    }
}
