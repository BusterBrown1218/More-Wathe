package xyz.busterbrown1218.more_wathe.config;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import dev.doctor4t.wathe.api.Role;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;
import org.agmas.harpymodloader.commands.argument.ModifierArgumentType;
import org.agmas.harpymodloader.commands.argument.RoleArgumentType;
import org.agmas.harpymodloader.modifiers.Modifier;

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
                    .then(literal("modifierChance")
                            .requires(source -> source.hasPermissionLevel(2))
                            .then(argument("modifier", ModifierArgumentType.create())
                                    .then(argument("value", DoubleArgumentType.doubleArg(0.0, 1.0))
                                            .executes((context -> {
                                                Modifier modifier = ModifierArgumentType.getModifier(context, "modifier");
                                                double weight = DoubleArgumentType.getDouble(context, "value");
                                                ConfigFile.HANDLER.instance().modifierChances.put(modifier.identifier().toString(), weight);
                                                ConfigFile.HANDLER.save();
                                                context.getSource().sendFeedback(() -> Text.literal(modifier.identifier().toString() + " weight set to " + weight), true);
                                                return Command.SINGLE_SUCCESS;
                                            })))))
                    .then(literal("roleChance")
                            .requires(source -> source.hasPermissionLevel(2))
                            .then(argument("role", RoleArgumentType.create())
                                    .then(argument("value", DoubleArgumentType.doubleArg(0.0, 1.0))
                                            .executes((context -> {
                                                Role role = RoleArgumentType.getRole(context, "role");
                                                double weight = DoubleArgumentType.getDouble(context, "value");
                                                ConfigFile.HANDLER.instance().roleChances.put(role.identifier().toString(), weight);
                                                ConfigFile.HANDLER.save();
                                                context.getSource().sendFeedback(() -> Text.literal(role.identifier().toString() + " weight set to " + weight), true);
                                                return Command.SINGLE_SUCCESS;
                                            })))))
            );
        });
    }
}
