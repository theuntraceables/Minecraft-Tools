package io.github.theuntraceables.commandsetup;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;


// /test IS A TEST
// DON'T FORGET TO COMMENT IT OUT IN "./src/main/java/io.github.theuntraceables.setup.TestCommand"

public class TestCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("test").requires((commandSourceStack) -> {
                    return commandSourceStack.hasPermission(2);
                }).executes(
                        (context) -> {
                            return testCommand(context);
                        }
                )
        );
    }
    public static int testCommand(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        int loopcount = 10;
        for (int i = 0; i < loopcount; i++) {
            System.out.println();
        }

//        String teststring;
//        teststring = System.getProperty("user.dir");
//        java.nio.file.Path thedirectory = FMLPaths.GAMEDIR.get();
//        teststring = thedirectory.toString();
//        System.out.println(teststring);

        System.out.println("This does nothing right now.");

        loopcount = 10;
        for (int i = 0; i < loopcount; i++) {
            System.out.println();
        }
        return 0;
    }
}
