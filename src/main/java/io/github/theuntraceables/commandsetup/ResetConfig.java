package io.github.theuntraceables.commandsetup;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.theuntraceables.setup.TheConfigs;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class ResetConfig {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("resetminecrafttoolsconfig").requires((commandSourceStack) -> {
                    return commandSourceStack.hasPermission(4);
                }).executes(
                        (context) -> {
                            return theCommand(context);
                        }
                )
        );
    }
    public static int theCommand(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        TheConfigs.resetConfigs();
        System.out.println("Config file reset for minecraft_tools mod!");
        context.getSource().sendSuccess(() -> {
            return Component.translatable("commands.resetminecrafttoolsconfig.success").withStyle(ChatFormatting.UNDERLINE);
        },true);
        return 0;
    }
}
