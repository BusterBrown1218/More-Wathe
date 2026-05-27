package xyz.busterbrown1218.more_wathe.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import xyz.busterbrown1218.more_wathe.MoreWathe;

public class ConfigFile {
    public static ConfigClassHandler<ConfigFile> HANDLER = ConfigClassHandler.<ConfigFile>createBuilder(ConfigFile.class)
            .id(Identifier.of(MoreWathe.MOD_ID, "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("morewathe.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build()).build();

    @SerialEntry
    public boolean preventOtherPlayersWakeup = true;
    @SerialEntry
    public boolean hideDisabledRoles = true;
    @SerialEntry
    public boolean hideDisabledModifiers = true;
    @SerialEntry
    public boolean hideMood = false;
}
