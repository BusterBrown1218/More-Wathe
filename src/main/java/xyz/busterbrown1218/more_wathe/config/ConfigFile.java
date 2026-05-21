package xyz.busterbrown1218.more_wathe.config;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public class ConfigFile {
    public static final transient File configFile = FabricLoader.getInstance().getConfigDir().resolve("morewathe.json5").toFile();

    private boolean preventOtherPlayersWakeup;
    private boolean hideDisabledRoles;
    private boolean hideDisabledModifiers;

    public ConfigFile() {
        preventOtherPlayersWakeup = true;
        hideDisabledRoles = true;
        hideDisabledModifiers = true;
    }

    public boolean getPreventOtherPlayersWakeup() {
        return preventOtherPlayersWakeup;
    }
    public void setPreventOtherPlayersWakeup(boolean preventOtherPlayersWakeup) {
        this.preventOtherPlayersWakeup = preventOtherPlayersWakeup;
    }

    public boolean getHideDisabledRoles() {
        return hideDisabledRoles;
    }
    public void setHideDisabledRoles(boolean hideDisabledRoles) {
        this.hideDisabledRoles = hideDisabledRoles;
    }

    public boolean getHideDisabledModifiers() {
        return hideDisabledModifiers;
    }
    public void setHideDisabledModifiers(boolean hideDisabledModifiers) {
        this.hideDisabledModifiers = hideDisabledModifiers;
    }
}
