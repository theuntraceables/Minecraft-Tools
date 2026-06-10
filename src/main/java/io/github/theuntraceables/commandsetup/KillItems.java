package io.github.theuntraceables.commandsetup;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.KillCommand;
// reminder to ctrl-click above to see typical command syntax
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;

import java.util.Collection;
import java.util.List;

import static io.github.theuntraceables.setup.Tools.pluralize;

public class KillItems {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("itemkill").requires((commandSourceStack) -> {
                    return commandSourceStack.hasPermission(2);
                }).executes(
                        (context) -> {
                            return killAllItems(context);
                        }
                ).then(Commands.argument("targets", EntityArgument.entities()).executes(
                        (context) -> {
                            return killItemsAtSelectors(context);
                        }
                ))
        );
    }

    private static int killAllItems(CommandContext<CommandSourceStack> context) throws CommandSyntaxException{
        int itemskilled = 0;
        int totalitemskilled = 0;
//        System.out.println("NOARGSGIVEN");
        CommandSourceStack source = context.getSource();
        Entity theplayer = source.getEntity();
//        System.out.println(theplayer.toString());
        if(theplayer instanceof ServerPlayer){
//            System.out.println("player used the command");
            AABB boundbox = theplayer.getBoundingBox().inflate(1000.0D,1000.0D,1000.0D);
            List<Entity> targets = theplayer.level().getEntities(theplayer, boundbox, entity -> true);
            String itemname = "";
            for(Entity entity:targets){
                if(entity instanceof ItemEntity) {
                    itemname = entity.getDisplayName().getString();
                    totalitemskilled += ((ItemEntity) entity).getItem().getCount();
//                    System.out.println(itemname);
                    itemskilled += 1;
                    entity.kill();
                }
            }
            System.out.println("Killed " + itemskilled + " item entit" + pluralize(itemskilled,"ie") + ".");

            final String totalitemskilled2 = totalitemskilled + " Total Item" + pluralize(totalitemskilled) + " killed";

            if(itemskilled == 1) {
                final String itemname1 = itemname;
//                dunno why the variable has to be final in the lambda but this works
                source.sendSuccess(() -> {
                    return Component.translatable("commands.itemkill.success.single", itemname1, totalitemskilled2);
                }, true);
            } else if (itemskilled == 0) {
                source.sendSuccess(() -> {
                    return Component.translatable("commands.itemkill.success.none");
                },true);
            }
            else {
                final Integer itemskilled2 = itemskilled;
//                seriously lambdas requiring final variables is just annoying
                source.sendSuccess(() -> {
                    return Component.translatable("commands.itemkill.success.multiple", itemskilled2, totalitemskilled2);
                },true);
            }
        }
        else {
            source.sendSuccess(() -> {
                return Component.translatable("commands.itemkill.failure.nonplayerandempty");
            },true);
            System.out.println("Executor of command was not a player, but no argument was given.");
            System.out.println("/itemkill (from Nightmare mod) must either be used by a player or an argument of");
            System.out.println("target selector (eg \"/itemkill @e\") must be provided for the argument.");
        }
        return itemskilled;
    }


    private static int killItemsAtSelectors(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        int itemskilled = 0;
        int totalitemskilled = 0;

        CommandSourceStack source = context.getSource();


        String itemname = "";
        Collection<? extends Entity> targets = EntityArgument.getEntities(context, "targets");
        for(Entity entity:targets){
            if(entity instanceof ItemEntity) {
                itemname = entity.getDisplayName().getString();
                totalitemskilled += ((ItemEntity) entity).getItem().getCount();
                itemskilled += 1;
                entity.kill();
            }
        }
//        targets.forEach(entity -> {
//            if (entity instanceof ItemEntity) {
//                itemskilled  = itemskilled + 1
//                entity.kill();
//            }
//        });
        System.out.println("Killed " + itemskilled + " item entit" + pluralize(itemskilled,"ie") + ".");

        final String totalitemskilled2 = totalitemskilled + " Total Item" + pluralize(totalitemskilled) + " killed";

        if(itemskilled == 1) {
            final String itemname1 = itemname;
//                dunno why the variable has to be final in the lambda but this works
            source.sendSuccess(() -> {
                return Component.translatable("commands.itemkill.success.single", itemname1, totalitemskilled2);
            }, true);
        } else if (itemskilled == 0) {
            source.sendSuccess(() -> {
                return Component.translatable("commands.itemkill.success.none");
            },true);
        }
        else {
            final Integer itemskilled2 = itemskilled;
//                seriously lambdas requiring final variables is just annoying
            source.sendSuccess(() -> {
                return Component.translatable("commands.itemkill.success.multiple", itemskilled2, totalitemskilled2);
            },true);
        }

        return itemskilled;
    }
}
