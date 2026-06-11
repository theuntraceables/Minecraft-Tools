package io.github.theuntraceables.setup;

import net.minecraft.world.level.GameRules;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static io.github.theuntraceables.setup.Tools.deleteFirstChar;
import static io.github.theuntraceables.setup.Tools.deleteLastChar;

public class TheConfigs {
    public static final boolean shouldsout = true;

    public static final Path installationDirectory = FMLPaths.GAMEDIR.get();
    public static final String installationPath = installationDirectory.toString();

    public static String saveFileAbsPath = "";
    public static Path saveFilePathObject;

    public static Path validDeathsPath;
    public static Path warpUseTimesPath;
    public static Path deathTimesPath;

    public static Path minecrafttoolsfolder;

//    public static boolean allowsuicide = true;
//    changing this to a gamerule instead of config so that
//    it can be world-specific and so that it'll (presumably) work multiplayer

    public static Map<String, Integer> playerDeathTimes = new HashMap<String, Integer>();
    public static Map<String, Integer> playerWarpDeathUseTimes = new HashMap<String, Integer>();
    public static Map<String, Boolean> validPlayerDeaths = new HashMap<String, Boolean>();

    public static Path configFolder;
    public static Path configFilePath;
    public static String configString;
    public static ArrayList<ArrayList<String>> configentries = new ArrayList<>();

    public static final GameRules.Key<GameRules.BooleanValue> RULE_ALLOW_SUICIDE = GameRules.register(
            "allowSuicide",
            GameRules.Category.PLAYER,
            GameRules.BooleanValue.create(true)
    );
    public static final GameRules.Key<GameRules.BooleanValue> RULE_ALLOW_FINDDEATH = GameRules.register(
            "allowFindingLastDeath",
            GameRules.Category.PLAYER,
            GameRules.BooleanValue.create(true)
    );
    public static final GameRules.Key<GameRules.BooleanValue> RULE_ALLOW_WARPDEATH = GameRules.register(
            "allowWarpingToLastDeath",
            GameRules.Category.PLAYER,
            GameRules.BooleanValue.create(true)
    );
    public static final GameRules.Key<GameRules.IntegerValue> RULE_WARPDEATH_COOLDOWN = GameRules.register(
            "deathTeleportCooldown",
            GameRules.Category.PLAYER,
            GameRules.IntegerValue.create(1200)
    );
    public static final GameRules.Key<GameRules.BooleanValue> RULE_ALLOW_MULTIWARP = GameRules.register(
            "allowMultipleWarpsPerDeath",
            GameRules.Category.PLAYER,
            GameRules.BooleanValue.create(false)
    );
    public static final GameRules.Key<GameRules.IntegerValue> RULE_DEATHWARP_TIME_WINDOW = GameRules.register(
            "deathWarpExpireTime",
            GameRules.Category.PLAYER,
            GameRules.IntegerValue.create(3600)
    );

    public static String defaultConfigValue() {
//        default value!!!!!!!!
        String returnvalue = "Literally nothing yet. There's no purpose for this right now.";

        return returnvalue;
    }

//    SAVING STRING VALUE CONSTRUCTION----------------------------------------------------------------------------------

    public static String createWarpDeathUsesString() {
        String returnvalue = "";
        for (String playername:playerWarpDeathUseTimes.keySet()) {
            returnvalue = returnvalue + playername + " = " + playerWarpDeathUseTimes.get(playername) + "\n";
        }
        if (returnvalue.length() > 0) {
            returnvalue = deleteLastChar(returnvalue);
        }
        return returnvalue;
    }
    public static String createDeathTimesString() {
        String returnvalue = "";
        for (String playername:playerDeathTimes.keySet()) {
            returnvalue = returnvalue + playername + " = " + playerDeathTimes.get(playername) + "\n";
        }
        if (returnvalue.length() > 0) {
            returnvalue = deleteLastChar(returnvalue);
        }
        return returnvalue;
    }
    public static String createValidDeathsString() {
        String returnvalue = "";
        for (String playername:validPlayerDeaths.keySet()) {
            returnvalue = returnvalue + playername + " = " + validPlayerDeaths.get(playername) + "\n";
        }
        if (returnvalue.length() > 0) {
            returnvalue = deleteLastChar(returnvalue);
        }
        return returnvalue;
    }

//    CONSTRUCTOR-------------------------------------------------------------------------------------------------------
    public TheConfigs() {
        int loopcount = 50;
        if (shouldsout) {
            for (int i = 0; i < loopcount; i++) {
                System.out.println();
            }
        }

        try {
            configFolder = Path.of(installationPath + "/config");
            if(!Files.isDirectory(configFolder)) {
                Files.createDirectory(configFolder);
                System.out.println("Created Folder at " + configFolder.toString());
            }

            configFilePath = Path.of(configFolder + "/minecraft_tools_config.txt");

//            System.out.println(configFilePath);
            File configFile = new File(configFilePath.toUri());
            if (!configFile.exists()) {
                resetConfigs();
            }


            configString = readAsConfigFile(configFile);
            if (Objects.equals(configString, "")) {
                resetConfigs();
            }

            configString = readAsConfigFile(configFile);
//            System.out.println(configString);

//            USING THE CONFIG FILE-------------------------------------------------------------------------------------
            configentries = new ArrayList<>();
            String[] configarray = configString.split("\\r?\\n");
            for(int i = 1; i < configarray.length; i++) {
//                System.out.println(configarray[i]);
                String configtempstring = configarray[i];
                if (!configtempstring.startsWith("#") && !configtempstring.startsWith("//")) {
                    ArrayList<String> temparray = new ArrayList<>();
                    String[] configtemparray2 = configtempstring.split("=");
                    for (int e = 0; e < 2 && e < configtemparray2.length; e++) {
                        String configtempstring2 = configtemparray2[e];
//                        System.out.println(configtempstring2);
                        if (configtempstring2.endsWith(" ")) {
                            configtempstring2 = deleteLastChar(configtempstring2);
                        }
                        if (configtempstring2.startsWith(" ")) {
                            configtempstring2 = deleteFirstChar(configtempstring2);
                        }

                        temparray.add(configtempstring2);
                    }
                    configentries.add(temparray);
                }
            }
//            System.out.println(configentries.toString());

//            CONFIG EXECUTION------------------------------------------------------------------------------------------

            for (int i = 0; i < configentries.size(); i++) {
                ArrayList<String> configentry = configentries.get(i);
                if(configentry.size() == 2) {
                    String section1 = configentry.get(0);
                    String section2 = configentry.get(1);
                    switch (section1) {
//                        case "/suicide":
//                            allowsuicide = Boolean.valueOf(section2);
//                            break;
//                        case "suicide":
//                            allowsuicide = Boolean.valueOf(section2);
//                            break;
                    }
                }

            }

//            System.out.println(configFolder);
//            System.out.println(configFolder + " exists: " + String.valueOf(Files.isDirectory(configFolder)));

        } catch (IOException theerror) {
            if (shouldsout) {
                System.out.println(theerror.toString());
            }
        }




//        String tester = String.valueOf(allowsuicide);
//        System.out.printf("Suicide => %s%n", allowsuicide);

        loopcount = 10;
        if (shouldsout) {
            for (int i = 0; i < loopcount; i++) {
                System.out.println();
            }
        }
    }

    public static String readAsConfigFile(File thefile) throws FileNotFoundException {
        Scanner reader = new Scanner(thefile);
        String returnvalue = "";
        while (reader.hasNextLine()) {
            returnvalue = returnvalue.concat("\n").concat(reader.nextLine());
//                intellij kept underlining the + symbol in yellow so I changed it to concat instead.
        }
        returnvalue = returnvalue.replaceAll("(?i)False","false");
        returnvalue = returnvalue.replaceAll("(?i)True","true");
//        that seems to take care of caps lock, since likely some people will capitalize true and false

//        System.out.println("File at " + thefile.toPath() + " contained:\n" + returnvalue);
        return returnvalue;
    }

//    RESET FUNCTIONS---------------------------------------------------------------------------------------------------
    public static void resetConfigs() {
        try {
            File configFile = new File(configFilePath.toUri());
            configFile.delete();
            configFile.createNewFile();
            FileWriter configFileWriter = new FileWriter(configFile);

            configFileWriter.write(defaultConfigValue());

            configFileWriter.close();
//            BE SURE TO CLOSE THE WRITER OR MEMORY LEAKS OR SOMETHING
        } catch (IOException theerror) {
            if (shouldsout) {
                System.out.println(theerror.toString());
            }
        }
    }

    public static void writeValidDeaths() {
        File configFile = new File(validDeathsPath.toUri());
        try {
            configFile.delete();
            configFile.createNewFile();
            FileWriter configFileWriter = new FileWriter(configFile);

            configFileWriter.write(createValidDeathsString());



            configFileWriter.close();
//            BE SURE TO CLOSE THE WRITER OR MEMORY LEAKS OR SOMETHING
        } catch (IOException theerror) {
            System.out.println("A mysterious error occured: " + theerror.toString());
        }
    }


    public static void writeDeathTimes() {
        File configFile = new File(deathTimesPath.toUri());
        try {
            configFile.delete();
            configFile.createNewFile();
            FileWriter configFileWriter = new FileWriter(configFile);

            configFileWriter.write(createDeathTimesString());



            configFileWriter.close();
//            BE SURE TO CLOSE THE WRITER OR MEMORY LEAKS OR SOMETHING
        } catch (IOException theerror) {
            System.out.println("A mysterious error occured: " + theerror.toString());
        }
    }


    public static void writeWarpTimes() {
        File configFile = new File(warpUseTimesPath.toUri());
        try {
            configFile.delete();
            configFile.createNewFile();
            FileWriter configFileWriter = new FileWriter(configFile);

            configFileWriter.write(createWarpDeathUsesString());



            configFileWriter.close();
//            BE SURE TO CLOSE THE WRITER OR MEMORY LEAKS OR SOMETHING
        } catch (IOException theerror) {
            System.out.println("A mysterious error occured: " + theerror.toString());
        }
    }
}
