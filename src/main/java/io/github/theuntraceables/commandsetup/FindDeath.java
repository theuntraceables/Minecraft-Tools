package io.github.theuntraceables.commandsetup;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import io.github.theuntraceables.setup.TheConfigs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

import static io.github.theuntraceables.setup.Tools.deleteFirstChar;
import static io.github.theuntraceables.setup.Tools.deleteLastChar;

public class FindDeath {
    private static final DynamicCommandExceptionType ERROR_COMMAND_DISABLED = new DynamicCommandExceptionType((entityName) -> {
        return Component.translatable("commands.finddeath.failure.disabled", entityName);
    });
    private static final DynamicCommandExceptionType ERROR_NEVER_DIED = new DynamicCommandExceptionType((entityName) -> {
        return Component.translatable("commands.finddeath.failure.neverdied", entityName);
    });
    private static final DynamicCommandExceptionType ERROR_NOT_PLAYER = new DynamicCommandExceptionType((entityName) -> {
        return Component.translatable("commands.finddeath.failure.notplayer", entityName);
    });
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("finddeath").requires((commandSourceStack) -> {
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
//        don't have to check if it's null because it already checks if it's a player
        if (contextEntity.level().getGameRules().getBoolean(TheConfigs.RULE_ALLOW_FINDDEATH)) {
            if (contextEntity instanceof Player) {

                Optional<GlobalPos> lastDeathLocation = ((Player) contextEntity).getLastDeathLocation();
                if (!lastDeathLocation.isEmpty()) {
                    GlobalPos trueLastDeath = lastDeathLocation.get();
                    String dimensionName = trueLastDeath.dimension().toString();
                    dimensionName = deleteLastChar(deleteFirstChar(dimensionName.split("/")[1]));
                    final String dimensionNameFinal = dimensionName;
//                    System.out.println(dimensionName);
                    context.getSource().sendSuccess(() -> {
                        return Component.translatable("commands.finddeath.success",
                                contextEntity.getDisplayName().getString(),
                                trueLastDeath.pos().getX(),
                                trueLastDeath.pos().getY(),
                                trueLastDeath.pos().getZ(),
                                dimensionNameFinal
                        );
                    }, true);
                }
                else {
                    throw ERROR_NEVER_DIED.create(context.getSource().getDisplayName().getString());
                }
            }
            else {
                throw ERROR_NOT_PLAYER.create(context.getSource().getDisplayName().getString());
            }
        }
        else {
            throw ERROR_COMMAND_DISABLED.create(context.getSource().getDisplayName().getString());
        }
        return 0;
    }
}
