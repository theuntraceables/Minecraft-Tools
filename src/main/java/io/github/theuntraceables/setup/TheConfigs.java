package io.github.theuntraceables.setup;

import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

import static io.github.theuntraceables.setup.Tools.deleteFirstChar;
import static io.github.theuntraceables.setup.Tools.deleteLastChar;

public class TheConfigs {
    public static final boolean shouldsout = true;

    public static final Path installationDirectory = FMLPaths.GAMEDIR.get();
    public static final String installationPath = installationDirectory.toString();

    public static boolean allowsuicide = true;
    public static Path configFolder;
    public static Path configFilePath;
    public static String configString;
    public static ArrayList<ArrayList<String>> configentries = new ArrayList<>();


    public static String defaultConfigValue() {
//        default value!!!!!!!!
        String returnvalue = "/suicide = true";

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
                        case "/suicide":
                            allowsuicide = Boolean.valueOf(section2);
                            break;
                        case "suicide":
                            allowsuicide = Boolean.valueOf(section2);
                            break;
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




        String tester = String.valueOf(allowsuicide);
        System.out.printf("Suicide => %s%n", allowsuicide);

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

//    RESET FUNCTION----------------------------------------------------------------------------------------------------
    public static void resetConfigs() {
        try {
            File configFile = new File(configFilePath.toUri());
            configFile.delete();
            configFile.createNewFile();
            FileWriter configFileWriter = new FileWriter(configFile);

            configFileWriter.write(defaultConfigValue());

            configFileWriter.close();
        } catch (IOException theerror) {
            if (shouldsout) {
                System.out.println(theerror.toString());
            }
        }
    }
}
