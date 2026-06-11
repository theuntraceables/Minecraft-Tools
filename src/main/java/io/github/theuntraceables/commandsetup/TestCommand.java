package io.github.theuntraceables.commandsetup;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.StorageDataSource;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.DimensionSpecialEffectsManager;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.targets.CommonLaunchHandler;
import net.minecraftforge.fml.loading.targets.FMLServerDevLaunchHandler;

import java.io.File;
import java.nio.file.Path;

import static io.github.theuntraceables.setup.TheConfigs.*;
import static io.github.theuntraceables.setup.Tools.pluralize;


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

        String outputString = "NO OUTPUT GIVEN";

//        outputString = minecrafttoolsfolder.toString();

//        for(String name:playerDeathTimes.keySet()) {
//            int deathtime = playerDeathTimes.get(name);
//            int deathtime2 = deathtime/20;
//            outputString = "It has been " + deathtime2 + " second" + pluralize(deathtime2)
//                    + " (" + deathtime + " tick"
//                    + pluralize(deathtime) + ") since " + name + " died.";
//            System.out.println(outputString);
//        }

//        System.out.println("This does nothing right now.");

        loopcount = 10;
        for (int i = 0; i < loopcount; i++) {
            System.out.println();
        }




        final String outputString2 = outputString;
        context.getSource().sendSuccess(() -> {
            return Component.translatable("commands.templateblankliteral", outputString2);
        }, true);
//        templateblankliteral is just "%s". Pass any string as the second argument of the component and
//        that appears in chat.
        return 0;
    }
}
