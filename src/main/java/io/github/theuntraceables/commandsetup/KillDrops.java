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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;

import java.util.Collection;
import java.util.List;

import static io.github.theuntraceables.setup.Tools.pluralize;

public class KillDrops {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("killdrops").requires((commandSourceStack) -> {
                    return commandSourceStack.hasPermission(2);
                }).executes(
                        (context) -> {
                            return killAllDrops(context);
                        }
                ).then(Commands.argument("targets", EntityArgument.entities()).executes(
                        (context) -> {
                            return killDropsAtSelectors(context);
                        }
                ))
        );
    }

    private static int killAllDrops(CommandContext<CommandSourceStack> context) throws CommandSyntaxException{
        int entitieskilled = 0;
        int totalitemskilled = 0;
//        System.out.println("NOARGSGIVEN");
        CommandSourceStack source = context.getSource();
        Entity theplayer = source.getEntity();
//        System.out.println(theplayer.toString());
        if(theplayer instanceof ServerPlayer){
//            System.out.println("player used the command");
            AABB boundbox = theplayer.getBoundingBox().inflate(1000.0D,1000.0D,1000.0D);
            List<Entity> targets = theplayer.level().getEntities(theplayer, boundbox, entity -> true);
            String entityname = "";
            for(Entity entity:targets){
                if(entity instanceof ExperienceOrb || entity instanceof ItemEntity) {
                    if (entity instanceof ItemEntity) {
                        totalitemskilled += ((ItemEntity) entity).getItem().getCount();
                    }
                    entityname = entity.getDisplayName().getString();
                    entitieskilled += 1;
                    entity.kill();
                }
            }
            System.out.println("Killed " + entitieskilled + " entit" + pluralize(entitieskilled,"ie") + ".");

            final String entitykillmessage = entitieskilled + " Entities (" + totalitemskilled + " item"
                    + pluralize(totalitemskilled) + " total)";

            final String entityname2 = entityname + " (" + totalitemskilled + " item"
                    + pluralize(totalitemskilled) + " total)";

            if(entitieskilled == 1) {
                source.sendSuccess(() -> {
                    return Component.translatable("commands.dropkill.success",entityname2);
                }, true);
            } else if (entitieskilled == 0) {
                source.sendSuccess(() -> {
                    return Component.translatable("commands.dropkill.success.none");
                },true);
            }
            else {
                source.sendSuccess(() -> {
                    return Component.translatable("commands.dropkill.success", entitykillmessage);
                },true);
            }
        }
        else {
            source.sendSuccess(() -> {
                return Component.translatable("commands.itemkill.failure.nonplayerandempty");
            },true);
            System.out.println("Executor of command was not a player, but no argument was given.");
            System.out.println("/killdrops (from Nightmare mod) must either be used by a player or an argument of");
            System.out.println("target selector (eg \"/killdrops @e\") must be provided for the argument.");
        }
        return entitieskilled;
    }


    private static int killDropsAtSelectors(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        int entitieskilled = 0;
        int totalitemskilled = 0;

        CommandSourceStack source = context.getSource();

        String entityname = "";

        Collection<? extends Entity> targets = EntityArgument.getEntities(context, "targets");
        for(Entity entity:targets){
            if(entity instanceof ExperienceOrb || entity instanceof ItemEntity) {
                if (entity instanceof ItemEntity) {
                    totalitemskilled += ((ItemEntity) entity).getItem().getCount();
                }
                entityname = entity.getDisplayName().getString();
                entitieskilled += 1;
                entity.kill();
            }
        }
        System.out.println("Killed " + entitieskilled + " entit" + pluralize(entitieskilled,"ie") + ".");

        final String entitykillmessage = entitieskilled + " Entities (" + totalitemskilled + " item"
                + pluralize(totalitemskilled) + " total)";
        final String entityname2 = entityname + " (" + totalitemskilled + " item"
                + pluralize(totalitemskilled) + " total)";

        if(entitieskilled == 1) {
            source.sendSuccess(() -> {
                return Component.translatable("commands.dropkill.success",entityname2);
            }, true);
        } else if (entitieskilled == 0) {
            source.sendSuccess(() -> {
                return Component.translatable("commands.dropkill.success.none");
            },true);
        }
        else {
            source.sendSuccess(() -> {
                return Component.translatable("commands.dropkill.success", entitykillmessage);
            },true);
        }

        return entitieskilled;
    }
}
