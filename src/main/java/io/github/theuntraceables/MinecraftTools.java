package io.github.theuntraceables;

import io.github.theuntraceables.setup.CreativeItemRegistry;
import io.github.theuntraceables.setup.TheConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;

import java.awt.*;

import static io.github.theuntraceables.setup.TheConfigs.shouldsout;

@Mod(MinecraftTools.MODID)
public class MinecraftTools {
    public static final String MODID = "minecraft_tools";

    public static boolean ide = !FMLLoader.isProduction();

    public static Player player;
    public static boolean playerExists;

    public static SystemTray tray;

    public static final TheConfigs thing = new TheConfigs();

    public MinecraftTools() throws AWTException {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        CreativeItemRegistry.TABS.register(bus);
    }
//    ignore the yellow underline, it seems to work anyways

}
