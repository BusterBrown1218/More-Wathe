package xyz.busterbrown1218.more_wathe.config;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ConfigCommands {
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("morewathe")
                    .then(literal("preventOtherPlayersWakeup")
                            .requires(source -> source.hasPermissionLevel(2))
                            .then(argument("value", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean value = BoolArgumentType.getBool(context, "value");
                                        ConfigFile.HANDLER.instance().preventOtherPlayersWakeup = value;
                                        ConfigFile.HANDLER.save();
                                        context.getSource().sendFeedback(() -> Text.literal("preventOtherPlayersWakeup: " + value), true);
                                        return Command.SINGLE_SUCCESS;
                                    })))
                    .then(literal("hideMood")
                            .requires(source -> source.hasPermissionLevel(2))
                            .then(argument("value", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean value = BoolArgumentType.getBool(context, "value");
                                        ConfigFile.HANDLER.instance().hideMood = value;
                                        ConfigFile.HANDLER.save();
                                        context.getSource().sendFeedback(() -> Text.literal("hideMood: " + value), true);
                                        return Command.SINGLE_SUCCESS;
                                    })))
            );
        });
    }
}
