package io.github.theuntraceables.setup;

import io.github.theuntraceables.MinecraftTools;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;

import static io.github.theuntraceables.setup.CreativeItemRegistry.*;
import static io.github.theuntraceables.setup.TheConfigs.*;

@Mod.EventBusSubscriber(modid = MinecraftTools.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TickFunction {
    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.END) {
            tickFunction(event.player);
        }
    }

    public static boolean firsttime = true;
    private static void tickFunction(Player player) {
        String playerName = player.getDisplayName().getString();

        if(firsttime){
            tablist = new java.util.ArrayList<>(BuiltInRegistries.CREATIVE_MODE_TAB.stream().toList());
            tablist.remove(HIDDEN_TAB.get());
//            System.out.println(tablist);
            itemlistset = ForgeRegistries.ITEMS.getKeys();
            itemlist = new HashSet<>();
            for (Object item:itemlistset) {
                itemlist.add(item.toString());
            }
//            System.out.println("done");
//            seems fast enough to avoid any errors

            firsttime = false;
        }

        if(ready) {
            for(String item:itemlist){
//                System.out.println(item);
//                sout all items that aren't in a creative tab
            }
            ready = false;
        }

//        death cooldown checks
        for (String name:playerDeathTimes.keySet()) {
           playerDeathTimes.put(name, playerDeathTimes.get(name)+1);
        }
        if (!playerDeathTimes.containsKey(playerName)) {
            playerDeathTimes.put(playerName,0);
        }
//        death command cooldown checks
        for (String name:playerWarpDeathUseTimes.keySet()) {
            playerWarpDeathUseTimes.put(name, playerWarpDeathUseTimes.get(name)+1);
        }
        if (!playerWarpDeathUseTimes.containsKey(playerName)) {
            playerWarpDeathUseTimes.put(playerName,0);
        }

//        death used checks
        if (!validPlayerDeaths.containsKey(playerName)) {
            validPlayerDeaths.put(playerName, true);
        }

    }
}
