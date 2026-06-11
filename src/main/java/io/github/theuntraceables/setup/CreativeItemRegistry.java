package io.github.theuntraceables.setup;

import io.github.theuntraceables.MinecraftTools;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Iterator;
import java.util.List;
import java.util.Set;


@Mod.EventBusSubscriber(modid = MinecraftTools.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CreativeItemRegistry {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MinecraftTools.MODID);

    public static boolean ready = false;
    public static Set itemlistset;
    public static Set<String> itemlist;

    public static final RegistryObject<CreativeModeTab> HIDDEN_TAB = TABS.register(
            "oncehidden_items",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.oncehidden_items"))
                    .icon(() -> new ItemStack(Items.SPYGLASS))
                    .displayItems((displayArgs, output) -> {
                    })
                    .build()
    );

    public static List<CreativeModeTab> tablist;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void RegisterItems(BuildCreativeModeTabContentsEvent event) {




        if (event.getTabKey() != CreativeModeTabs.SEARCH &&
                event.getTabKey() != CreativeModeTabs.HOTBAR &&
                event.getTabKey() != CreativeModeTabs.INVENTORY &&
                event.getTabKey() != HIDDEN_TAB.getKey()) {

            event.getEntries().forEach((entry) -> {
//            System.out.println();
                try {
                    final String itemname = BuiltInRegistries.ITEM.getKey(entry.getKey().getItem()).toString();
//                System.out.println(itemname);
                    System.out.println("ITEMNAME " + itemname);
                    boolean removedit = itemlist.remove(itemname);
                    System.out.println(removedit);
                    if (itemname.contains("armor")) {
                        System.out.println(itemname);
                    }
                } catch (Exception ignored) {
                }
//            System.out.println();
            });
        }

//            System.out.println("REMOVING " + event.getTab().getDisplayName().getString());
            tablist.remove(event.getTab());

            for (CreativeModeTab tab : tablist) {
//                System.out.println(tab.getDisplayName().getString());
            }

            if (event.getTab() == HIDDEN_TAB.get()) {
                itemlist.remove("minecraft:air");
//                game seems to just crash when air attempts to enter the inventory
//                so unfortunately I have to remove that one
//                I think it's probably used as the base for an empty slot


                Iterator<String> theiterator = itemlist.iterator();
                String item;
                while(theiterator.hasNext()) {
                    item = theiterator.next();
                    event.getEntries().putFirst(
                            new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse(item)),1),
                            CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
                    );
//                    theiterator.remove();
                }
            }


            if (tablist.isEmpty()) {
                ready = true;
                for (String item : itemlist) {


                }
            }




    }
}
