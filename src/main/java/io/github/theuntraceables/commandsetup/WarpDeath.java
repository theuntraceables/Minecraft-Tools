package io.github.theuntraceables.commandsetup;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import io.github.theuntraceables.setup.TheConfigs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;

import java.awt.*;
import java.util.EnumSet;
import java.util.Optional;

import static io.github.theuntraceables.setup.TheConfigs.*;
import static io.github.theuntraceables.setup.Tools.deleteFirstChar;
import static io.github.theuntraceables.setup.Tools.deleteLastChar;

public class WarpDeath {
    private static final DynamicCommandExceptionType ERROR_COMMAND_DISABLED = new DynamicCommandExceptionType((entityName) -> {
        return Component.translatable("commands.warpdeath.failure.disabled", entityName);
    });
    private static final DynamicCommandExceptionType ERROR_NEVER_DIED = new DynamicCommandExceptionType((entityName) -> {
        return Component.translatable("commands.warpdeath.failure.neverdied", entityName);
    });
    private static final DynamicCommandExceptionType ERROR_NOT_PLAYER = new DynamicCommandExceptionType((entityName) -> {
        return Component.translatable("commands.warpdeath.failure.notplayer", entityName);
    });
    private static final Dynamic3CommandExceptionType ERROR_TOO_LATE = new Dynamic3CommandExceptionType((entityName, timeGiven, timeAllowed) -> {
        int timeGivenS = Integer.valueOf(timeGiven.toString())/20;
        int timeAllowedS = Integer.valueOf(timeAllowed.toString())/20;
        return Component.translatable("commands.warpdeath.failure.toolate", entityName, timeGivenS, timeGiven, timeAllowedS, timeAllowed);
    });
    private static final DynamicCommandExceptionType ERROR_NOT_AGAIN = new DynamicCommandExceptionType((entityName) -> {
        return Component.translatable("commands.warpdeath.failure.notagain",entityName);
    });
    private static final Dynamic3CommandExceptionType ERROR_TOO_EARLY = new Dynamic3CommandExceptionType((entityName, timeGiven, timeAllowed) -> {
        int timeGivenS = Integer.valueOf(timeGiven.toString())/20;
        int timeAllowedS = Integer.valueOf(timeAllowed.toString())/20;
        return Component.translatable("commands.warpdeath.failure.tooearly", entityName, timeGivenS, timeGiven, timeAllowedS, timeAllowed);
    });

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("warpdeath").requires((commandSourceStack) -> {
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
        if (contextEntity.level().getGameRules().getBoolean(RULE_ALLOW_WARPDEATH)) {
            if (contextEntity instanceof Player) {
                String playerName = contextEntity.getDisplayName().getString();
                int timeSinceDeath = playerDeathTimes.get(playerName);
                int maxTimeAllowed = contextEntity.level().getGameRules().getInt(TheConfigs.RULE_DEATHWARP_TIME_WINDOW);
                if (timeSinceDeath < maxTimeAllowed) {

                    if(contextEntity.level().getGameRules().getBoolean(TheConfigs.RULE_ALLOW_MULTIWARP) || validPlayerDeaths.get(playerName)) {
                        int timeSinceCommandUsed = contextEntity.level().getGameRules().getInt(TheConfigs.RULE_WARPDEATH_COOLDOWN);
                        int playerCooldownActual = playerWarpDeathUseTimes.get(playerName);
                        if (playerCooldownActual >= timeSinceCommandUsed) {

                            Optional<GlobalPos> lastDeathLocation = ((Player) contextEntity).getLastDeathLocation();
                            if (!lastDeathLocation.isEmpty()) {

                                GlobalPos trueLastDeath = lastDeathLocation.get();
                                String dimensionName = trueLastDeath.dimension().toString();
                                dimensionName = deleteLastChar(deleteFirstChar(dimensionName.split("/")[1]));
                                final String dimensionNameFinal = dimensionName;
//                        System.out.println(dimensionName);

//                        try {
//
////                            System.out.println(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(dimensionName)));
////                            System.out.println(contextEntity.level().getServer());
//
////                            ServerLevel serverlevel = ((ServerLevel)
////                                    contextEntity.level()
////                            ).getServer().getLevel(
////                                    ResourceKey.create(Registries.DIMENSION, new ResourceLocation(dimensionName))
////                            );
//
////                            if(serverlevel != null) {
////                                System.out.println(serverlevel);
//////                                contextEntity.changeDimension(serverlevel);
//////                              line above is underlined yellow "might be null" NOBODY CARES
////                            }
//                      }
//                      catch (Exception ignored) {
//                      }

                                ServerLevel serverLevel = context.getSource().getServer().getLevel(
                                        ResourceKey.create(
                                                Registries.DIMENSION,
                                                new ResourceLocation(dimensionName.split(":")[0], dimensionName.split(":")[1])
                                        )
                                );
//                        System.out.println(serverLevel);

                                contextEntity.teleportTo(
//                              (ServerLevel) contextEntity.level(),
                                        serverLevel,
                                        trueLastDeath.pos().getX(),
                                        trueLastDeath.pos().getY(),
                                        trueLastDeath.pos().getZ(),
                                        EnumSet.noneOf(RelativeMovement.class),
                                        contextEntity.getYRot(),
                                        contextEntity.getXRot()
                                );
                                validPlayerDeaths.put(playerName, false);
                                playerWarpDeathUseTimes.put(playerName,0);
                                context.getSource().sendSuccess(() -> {
                                    return Component.translatable("commands.warpdeath.success",
                                            playerName,
                                            trueLastDeath.pos().getX(),
                                            trueLastDeath.pos().getY(),
                                            trueLastDeath.pos().getZ(),
                                            dimensionNameFinal
                                    );
                                }, true);
                            } else {
                                throw ERROR_NEVER_DIED.create(context.getSource().getDisplayName().getString());
                            }
                        }
                        else {
                            throw ERROR_TOO_EARLY.create(playerName,playerCooldownActual,timeSinceCommandUsed);
                        }
                    }
                    else {
                        throw ERROR_NOT_AGAIN.create(playerName);
                    }
                }
                else {
                    throw ERROR_TOO_LATE.create(playerName,timeSinceDeath,maxTimeAllowed);
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
