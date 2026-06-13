package io.github.theuntraceables.setup;

import io.github.theuntraceables.MinecraftTools;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import static io.github.theuntraceables.MinecraftTools.player;
import static io.github.theuntraceables.MinecraftTools.playerExists;

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

    public static String pluralize(String amount) {
        return pluralize(Integer.parseInt(amount));
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

    public static String deleteEndSpaces(String thestring) {
        if (thestring != null && !thestring.isEmpty()) {
            while (thestring.startsWith(" ")) {
                thestring = deleteFirstChar(thestring);
            }
            while (thestring.endsWith(" ")) {
                thestring = deleteLastChar(thestring);
            }
        }
        return thestring;
    }

//    chat string stuff-------------------------------------------------------------------------------------------------

    public static String dynamicString(String identifier) {
        return Component.translatable(identifier).getString();
    }

    public static void chatMessage(String themessage) {
        if (playerExists) {
            player.sendSystemMessage(Component.literal(themessage));
        }
        else {
            System.out.println("Method MinecraftTools.chatMessage(\"" + themessage + "\") was used but there was no player to send the message to.");
        }
    }
    public static void chatMessage(String themessage, String color) {
        if (playerExists) {
            ChatFormatting colordata = switch (color.toLowerCase()) {
                case "red" -> ChatFormatting.RED;
                case "yellow" -> ChatFormatting.YELLOW;
                case "green" -> ChatFormatting.GREEN;
                case "blue" -> ChatFormatting.BLUE;
                case "black" -> ChatFormatting.BLACK;
                case "italic" -> ChatFormatting.ITALIC;
                case "purple" -> ChatFormatting.LIGHT_PURPLE;
                case "dark purple", "dark_purple", "darkpurple" -> ChatFormatting.DARK_PURPLE;
                case "strikethrough" -> ChatFormatting.STRIKETHROUGH;
                case "obfuscate", "obfuscated" -> ChatFormatting.OBFUSCATED;
                case "gold" -> ChatFormatting.GOLD;
                case "aqua" -> ChatFormatting.AQUA;
                case "dark_aqua", "dark aqua", "darkaqua" -> ChatFormatting.DARK_AQUA;
                case "bold" -> ChatFormatting.BOLD;
//                NOTE THAT BOLD TEXT CANNOT USE LOWERCASE "M"!!!!!!!!!!
//                IT JUST LOOKS LIKE LOWERCASE "N"!
                case "dark_green", "dark green", "darkgreen" -> ChatFormatting.DARK_GREEN;
                case "underline", "underlined" -> ChatFormatting.UNDERLINE;
                case "dark blue", "dark_blue", "darkblue" -> ChatFormatting.DARK_BLUE;
                default -> ChatFormatting.WHITE;
            };
            player.sendSystemMessage(Component.literal(themessage).withStyle(colordata));
        }
        else {
            System.out.println("Method MinecraftTools.chatMessage(\"" + themessage + "\", + \"" + color + "\") was used but there was no player to send the message to.");
        }
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
