package xyz.busterbrown1218.more_wathe.config;

import cat.rezelyn.watheextended.api.config.ClientConfig;
import cat.rezelyn.watheextended.api.config.ServerConfig;

public class ExtendedConfigHelper {

    public static void registerEntries() {
        ServerConfig.register(ServerConfig.Entry.worldBool("morewathe.hideMood", false, world -> ConfigComponent.KEY.get(world).hideMood, (world, value) -> {
            ConfigFile.HANDLER.instance().hideMood = value;
            ConfigFile.HANDLER.save();
        }));
        ServerConfig.register(ServerConfig.Entry.globalBool("morewathe.preventOtherPlayersWakeup", true, () -> ConfigFile.HANDLER.instance().preventOtherPlayersWakeup, value -> {
            ConfigFile.HANDLER.instance().preventOtherPlayersWakeup = value;
            ConfigFile.HANDLER.save();
        }));
    }

    public static boolean getHideDisabledRoles() {
        return ConfigFile.HANDLER.instance().hideDisabledRoles;
    }
    public static boolean getHideDisabledModifiers() {
        return ConfigFile.HANDLER.instance().hideDisabledModifiers;
    }

    public static void setHideDisabledRoles(boolean value) {
        ConfigFile.HANDLER.instance().hideDisabledRoles = value;
    }
    public static void setHideDisabledModifiers(boolean value) {
        ConfigFile.HANDLER.instance().hideDisabledModifiers = value;
    }
}
