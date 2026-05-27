package xyz.busterbrown1218.more_wathe.client;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;
import xyz.busterbrown1218.more_wathe.config.ConfigFile;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class ClientConfigCommands {
    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
           dispatcher.register(literal("hideDisabledRoles")
                   .then(argument("value", BoolArgumentType.bool())
                           .executes(context -> {
                               boolean value = BoolArgumentType.getBool(context, "value");
                               ConfigFile.HANDLER.instance().hideDisabledRoles = value;
                               ConfigFile.HANDLER.save();
                               context.getSource().sendFeedback(Text.literal("hideDisabledRoles: " + value));
                               return Command.SINGLE_SUCCESS;
                           }))
                   );
           dispatcher.register(literal("hideDisabledModifiers")
                   .then(argument("value", BoolArgumentType.bool())
                           .executes(context -> {
                               boolean value = BoolArgumentType.getBool(context, "value");
                               ConfigFile.HANDLER.instance().hideDisabledModifiers = value;
                               ConfigFile.HANDLER.save();
                               context.getSource().sendFeedback(Text.literal("hideDisabledModifiers: " + value));
                               return Command.SINGLE_SUCCESS;
                           })));
        });
    }
}
