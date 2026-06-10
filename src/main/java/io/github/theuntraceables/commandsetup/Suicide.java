package io.github.theuntraceables.commandsetup;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.KillCommand;

import static io.github.theuntraceables.setup.TheConfigs.allowsuicide;

public class Suicide {
    private static final SimpleCommandExceptionType ERROR_COMMAND_DISABLED = new SimpleCommandExceptionType(
            Component.translatable("commands.suicide.failure"));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("suicide").requires((commandSourceStack) -> {
                    return commandSourceStack.hasPermission(0);
                }).executes(
                        (context) -> {
                            return theCommand(context);
                        }
                )
        );
    }
    public static int theCommand(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if (allowsuicide) {
            if (context.getSource().getEntity() != null) {
                context.getSource().getEntity().kill();
            context.getSource().sendSuccess(() -> {
                return Component.translatable("commands.suicide.success", context.getSource().getDisplayName().getString());
            }, true);
            }
        }
        else{
            throw ERROR_COMMAND_DISABLED.create();
        }

        return 0;
    }
}
