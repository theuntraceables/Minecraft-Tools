package io.github.theuntraceables;

import io.github.theuntraceables.setup.CreativeItemRegistry;
import io.github.theuntraceables.setup.TheConfigs;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MinecraftTools.MODID)
public class MinecraftTools {
    public static final String MODID = "minecraft_tools";


    public static final TheConfigs thing = new TheConfigs();

    public MinecraftTools() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        CreativeItemRegistry.TABS.register(bus);
    }
//    ignore the yellow underline, it seems to work anyways

}
