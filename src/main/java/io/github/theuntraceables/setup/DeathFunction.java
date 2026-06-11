package io.github.theuntraceables.setup;

import io.github.theuntraceables.MinecraftTools;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static io.github.theuntraceables.setup.TheConfigs.playerDeathTimes;
import static io.github.theuntraceables.setup.TheConfigs.validPlayerDeaths;

@Mod.EventBusSubscriber(modid = MinecraftTools.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DeathFunction {

    @SubscribeEvent
    public static void onTick(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            deathFunction((Player) event.getEntity());
        }
    }

    private static void deathFunction(Player player) {
        String playerName = player.getDisplayName().getString();
        playerDeathTimes.put(playerName,0);
        validPlayerDeaths.put(playerName,true);
    }
}
