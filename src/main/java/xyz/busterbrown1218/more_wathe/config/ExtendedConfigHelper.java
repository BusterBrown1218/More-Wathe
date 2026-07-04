package xyz.busterbrown1218.more_wathe.config;

public class ExtendedConfigHelper {
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
