package io.github.theuntraceables.setup;

import io.github.theuntraceables.MinecraftTools;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Objects;

import static io.github.theuntraceables.MinecraftTools.ide;
import static io.github.theuntraceables.setup.TheConfigs.*;

@Mod.EventBusSubscriber(modid = MinecraftTools.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class OffLoadWorldFunction {
    @SubscribeEvent
    public static void onUnload(ServerStoppingEvent event) {
        if (!Objects.requireNonNull(event.getServer().getLevel(Level.OVERWORLD)).isClientSide()) {

            writeDeathTimes();
            writeWarpTimes();
            writeValidDeaths();

            playerDeathTimes = new HashMap<String, Integer>();
            playerWarpDeathUseTimes = new HashMap<String, Integer>();
            validPlayerDeaths = new HashMap<String, Boolean>();


            LoadWorldFunction.timesdone = 0;

            TickFunction.firsttime = true;

            if (ide) {
                writeIDEGameRules();
            }
        }
    }
}
