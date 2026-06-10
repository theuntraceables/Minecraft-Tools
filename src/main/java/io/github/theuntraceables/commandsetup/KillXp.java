package io.github.theuntraceables.commandsetup;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.phys.AABB;

import java.util.Collection;
import java.util.List;

import static io.github.theuntraceables.setup.Tools.pluralize;

public class KillXp {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("xpkill").requires((commandSourceStack) -> {
                    return commandSourceStack.hasPermission(2);
                }).executes(
                        (context) -> {
                            return killAllXp(context);
                        }
                ).then(Commands.argument("targets", EntityArgument.entities()).executes(
                        (context) -> {
                            return killXpAtSelectors(context);
                        }
                ))
        );
    }

    private static int killAllXp(CommandContext<CommandSourceStack> context) throws CommandSyntaxException{
        int xpkilled = 0;
//        System.out.println("NOARGSGIVEN");
        CommandSourceStack source = context.getSource();
        Entity theplayer = source.getEntity();
//        System.out.println(theplayer.toString());
        if(theplayer instanceof ServerPlayer){
//            System.out.println("player used the command");
            AABB boundbox = theplayer.getBoundingBox().inflate(1000.0D,1000.0D,1000.0D);
            List<Entity> targets = theplayer.level().getEntities(theplayer, boundbox, entity -> true);
            for(Entity entity:targets){
                if(entity instanceof ExperienceOrb) {
                    xpkilled += 1;
                    entity.kill();
                }
            }
            System.out.println("Killed " + xpkilled + " xp orb" + pluralize(xpkilled) + ".");

            final Integer xpkilled2 = xpkilled;

            if(xpkilled == 1) {
                source.sendSuccess(() -> {
                    return Component.translatable("commands.xpkill.success.single");
                }, true);
            } else if (xpkilled == 0) {
                source.sendSuccess(() -> {
                    return Component.translatable("commands.xpkill.success.none");
                },true);
            }
            else {
                source.sendSuccess(() -> {
                    return Component.translatable("commands.xpkill.success.multiple", xpkilled2);
                },true);
            }
        }
        else {
            source.sendSuccess(() -> {
                return Component.translatable("commands.itemkill.failure.nonplayerandempty");
            },true);
            System.out.println("Executor of command was not a player, but no argument was given.");
            System.out.println("/xpkill (from Nightmare mod) must either be used by a player or an argument of");
            System.out.println("target selector (eg \"/xpkill @e\") must be provided for the argument.");
        }
        return xpkilled;
    }


    private static int killXpAtSelectors(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        int xpkilled = 0;

        CommandSourceStack source = context.getSource();


        Collection<? extends Entity> targets = EntityArgument.getEntities(context, "targets");
        for(Entity entity:targets){
            if(entity instanceof ExperienceOrb) {
                xpkilled += 1;
                entity.kill();
            }
        }
        System.out.println("Killed " + xpkilled + " xp orb" + pluralize(xpkilled) + ".");

        final Integer xpkilled2 = xpkilled;

        if(xpkilled == 1) {
            source.sendSuccess(() -> {
                return Component.translatable("commands.xpkill.success.single");
            }, true);
        } else if (xpkilled == 0) {
            source.sendSuccess(() -> {
                return Component.translatable("commands.xpkill.success.none");
            },true);
        }
        else {
            source.sendSuccess(() -> {
                return Component.translatable("commands.xpkill.success.multiple", xpkilled2);
            },true);
        }

        return xpkilled;
    }
}
