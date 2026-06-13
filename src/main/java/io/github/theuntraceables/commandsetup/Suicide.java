package io.github.theuntraceables.commandsetup;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import io.github.theuntraceables.setup.TheConfigs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

import static io.github.theuntraceables.setup.TheConfigs.getGameRule;


public class Suicide {
    private static final DynamicCommandExceptionType ERROR_COMMAND_DISABLED = new DynamicCommandExceptionType((entityName) -> {
        return Component.translatable("commands.suicide.failure", entityName);
    });
    private static final DynamicCommandExceptionType ERROR_NOT_ENTITY = new DynamicCommandExceptionType((entityName) -> {
        return Component.translatable("commands.suicide.failure.notentity", entityName);
    });
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
        Entity contextEntity = context.getSource().getEntity();
        if(contextEntity != null) {
            if ((boolean) getGameRule("allowSuicide",contextEntity)) {
//            kinda just copied the line above from the EnderMan.java file and changed a little bit and it seems to work
                if (contextEntity != null) {
                    contextEntity.kill();
                    context.getSource().sendSuccess(() -> {
                        return Component.translatable("commands.suicide.success", context.getSource().getDisplayName().getString());
                    }, true);
                }
            } else {
                throw ERROR_COMMAND_DISABLED.create(context.getSource().getDisplayName().getString());
            }
        }
        else {
            throw ERROR_NOT_ENTITY.create(context.getSource().getDisplayName().getString());
        }

        return 0;
    }
}
