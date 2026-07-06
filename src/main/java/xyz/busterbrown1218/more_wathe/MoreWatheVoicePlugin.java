package xyz.busterbrown1218.more_wathe;

import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import de.maxhenkel.voicechat.api.opus.OpusDecoder;
import de.maxhenkel.voicechat.api.opus.OpusEncoder;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import org.agmas.harpymodloader.component.WorldModifierComponent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MoreWatheVoicePlugin implements VoicechatPlugin {
    @Override
    public String getPluginId() {
        return MoreWathe.MOD_ID;
    }

    public static VoicechatApi API;
    public static OpusDecoder decoder;
    public static OpusEncoder encoder;

    @Override
    public void initialize(VoicechatApi api) {
        API = api;
        decoder = api.createDecoder();
        encoder = api.createEncoder();
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(MicrophonePacketEvent.class, this::warbleVoice);
    }

    private final ConcurrentHashMap<UUID, Double> playerPhases = new ConcurrentHashMap<>();

    private void warbleVoice(MicrophonePacketEvent event) {
        PlayerEntity player = (PlayerEntity) event.getSenderConnection().getPlayer().getPlayer();
        if (!WorldModifierComponent.KEY.get(player.getWorld()).isModifier(player, MoreWathe.WARBLED)) return;
        UUID playerId = player.getUuid();

        byte[] opusData = event.getPacket().getOpusEncodedData();
        short[] pcmSamples = decoder.decode(opusData);

        double currentPhase = playerPhases.getOrDefault(playerId, 0.0);

        double sum = 0;
        int zeroCrossings = 0;

        for (int i = 0; i < pcmSamples.length; i++) {
            sum += pcmSamples[i] * pcmSamples[i];

            if (i > 0 && ((pcmSamples[i] >= 0 && pcmSamples[i - 1] < 0) || (pcmSamples[i] < 0 && pcmSamples[i - 1] >= 0))) {
                zeroCrossings++;
            }
        }

        double rms = Math.sqrt(sum / pcmSamples.length);

        double volumeFactor = Math.min(1.0, rms / 3000.0);

        double frameDuration = (double) pcmSamples.length / 48000;
        double targetFrequency = (zeroCrossings / frameDuration) / 2.0;

        targetFrequency -= 150;

        if (targetFrequency < 100) targetFrequency = 100;
        if (targetFrequency > 1000) targetFrequency = 1000;

        for (int i = 0; i < pcmSamples.length; i++) {
            double sineWave = Math.sin(currentPhase);

            currentPhase += (2.0 * Math.PI * targetFrequency) / 48000;
            if (currentPhase > 2.0 * Math.PI) {
                currentPhase -= 2.0 * Math.PI;
            }

            int synthSample = (int) (sineWave * Short.MAX_VALUE * Math.sqrt(volumeFactor) * 0.15);

            if (synthSample > Short.MAX_VALUE) synthSample = Short.MAX_VALUE;
            if (synthSample < Short.MIN_VALUE) synthSample = Short.MIN_VALUE;

            pcmSamples[i] = (short) synthSample;
        }

        playerPhases.put(playerId, currentPhase);

        byte[] modifiedOpusData = encoder.encode(pcmSamples);
        event.getPacket().setOpusEncodedData(modifiedOpusData);
    }
}
