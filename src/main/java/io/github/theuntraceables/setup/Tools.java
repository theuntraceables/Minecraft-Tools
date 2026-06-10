package io.github.theuntraceables.setup;

import io.github.theuntraceables.MinecraftTools;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class Tools {
    public static String pluralize(int amount, String prefix) {
        if(amount == 1){
            if(prefix.equals("ie")){
                return "y";
            }
            else {
                return "";
            }
        }
        else {
            return prefix + "s";
        }
    }
    public static String pluralize(int amount) {
        return pluralize(amount, "");
    }

    public static String deleteLastChar(String thestring) {
        if(thestring != null && !thestring.isEmpty()) {
            thestring = thestring.substring(0,thestring.length()-1);
        }
        return thestring;
    }
    public static String deleteFirstChar(String thestring) {
        if(thestring != null && !thestring.isEmpty()) {
            thestring = thestring.substring(1,thestring.length());
//            don't care that the line above is highlighted yellow, saying that
//            "thestring.length()" argument is unnecessary, too bad for you
        }
        return thestring;
    }


    //    nullfunction : a function that takes the input and does nothing.
//    basically I just use it for ctrl-click references.
//    example:
    public static void main(String[] args) {
//        ctrl-click the "IRON_AXE" to quickly see the original definition of it
        nullfunction(Items.IRON_AXE);
    }

    //    I've listed a few argument types I think I may need to reference commonly in the future:
    public static void nullfunction(Item nullthing) {}
    public static void nullfunction(ResourceLocation nullthing) {}
    public static void nullfunction(Tag nullthing) {}
    public static void nullfunction(BlockTags nullthing) {}
    public static void nullfunction(CommandSourceStack nullthing) {}
    public static void nullfunction(Component nullthing) {}

}
