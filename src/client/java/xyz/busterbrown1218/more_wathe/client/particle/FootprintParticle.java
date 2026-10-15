package xyz.busterbrown1218.more_wathe.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class FootprintParticle extends SpriteBillboardParticle {
    protected FootprintParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z, 0, 0, 0);
        this.maxAge = 0;

        this.alpha = 0.4f;
        this.red = 0.2f;
        this.blue = 0.5f;
        this.green = 0.5f;

        this.scale = 0.15f;
        this.setSprite(spriteProvider);
    }

    @Override
    public Rotator getRotator() {
        return (quaternion, camera, tickDelta) -> quaternion.rotationTo(0f, 0f, 1f, 0f, 1f, 0f);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new FootprintParticle(world, x, y, z, this.spriteProvider);
        }
    }
}
