package io.github.theuntraceables.setup;


import io.github.theuntraceables.commandsetup.*;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static io.github.theuntraceables.setup.TheConfigs.allowsuicide;
import static io.github.theuntraceables.setup.TheConfigs.shouldsout;

@Mod.EventBusSubscriber
public class TheCommands {
    // I would call the class "Commands" but that's already taken by something in Minecraft
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        KillItems.register(event.getDispatcher());
        KillXp.register(event.getDispatcher());
        KillDrops.register(event.getDispatcher());

        Suicide.register(event.getDispatcher());

        ResetConfig.register(event.getDispatcher());

//        for testing purposes only, DON'T FORGET TO COMMENT IT OUT FOR RELEASES
        TestCommand.register(event.getDispatcher());
    }
}
