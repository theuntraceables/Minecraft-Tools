package io.github.theuntraceables.setup;

import io.github.theuntraceables.MinecraftTools;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static io.github.theuntraceables.MinecraftTools.*;
import static io.github.theuntraceables.setup.TheConfigs.*;
import static io.github.theuntraceables.setup.Tools.*;

@Mod.EventBusSubscriber(modid = MinecraftTools.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LoadWorldFunction {
    public static int timesdone = 0;

    @SubscribeEvent
    public static void onLoad(ServerStartingEvent event) {
        boolean clientSide = Objects.requireNonNull(event.getServer().getLevel(Level.OVERWORLD)).isClientSide();


        if (timesdone == 0 && !clientSide) {
//            determine if run with the Intellij or not (THIS IS IMPORTANT)

//            get world save folder-------------------------------------------------------------------------------------
            Path worldpath = Path.of(deleteLastChar(
                    Objects.requireNonNull(event.getServer().getWorldPath(LevelResource.ROOT).toString())
            ));
//            had to deleteLastChar because for some reason it puts a period at the end, ruining everything
//            System.out.println(installationPath);
//            System.out.println(worldpath);
//            System.out.println(Path.of(installationPath + worldpath));
//            worldpath returns ABSOLUTE PATH IN THE JAR FILE SO NO NEED TO CONCATENATE
//            IT INTO INSTALLATIONPATH
            if(ide) {
                saveFileAbsPath = installationPath + deleteFirstChar(worldpath.toString());
            }
            else {
                saveFileAbsPath = worldpath.toString();
            }
//            System.out.println(saveFileAbsPath);

            saveFilePathObject = Path.of(saveFileAbsPath);

//            get serverconfig folder and then minecrafttools folder----------------------------------------------------
            Path serverconfigfolder = Path.of(saveFileAbsPath + "/serverconfig");
//            System.out.println(serverconfigfolder);
            minecrafttoolsfolder = Path.of(serverconfigfolder + "/minecraft_tools/");


//            System.out.println(minecrafttoolsfolder);
//            should be safe to use, right? putting random files in serverconfig shouldn't break anything

//            create serverconfig/minecraft_tools folders if they don't exist-------------------------------------------
            try {
                if (!Files.isDirectory(serverconfigfolder)) {
                    Files.createDirectory(serverconfigfolder);
                    System.out.println("Created Folder at " + serverconfigfolder.toString());
//                    Don't get what it means by "unnecessary toString() call" when I hover over "toString"
                }
                if (!Files.isDirectory(minecrafttoolsfolder)) {
                    Files.createDirectory(minecrafttoolsfolder);
                    System.out.println("Created Folder at " + minecrafttoolsfolder.toString());
                }
            } catch (IOException theerror) {
                System.out.println("Something went wrong in minecraft_tools ./src/main/java/io.github.theuntraceables.setup.LoadWorldFunction while attempting to create " + minecrafttoolsfolder + ": " + theerror.toString());
            }

            String filecontents = "";
//            read/create valid_deaths.txt------------------------------------------------------------------------------
            validDeathsPath = Path.of(minecrafttoolsfolder + "/valid_deaths.txt");
            File configFile = new File(validDeathsPath.toUri());
            try {
                filecontents = "";
                if (configFile.exists()) {
                    filecontents = readAsConfigFile(configFile);
                    if (Objects.equals(filecontents, "")) {
                        configFile.createNewFile();
                    }
                } else {
                    configFile.createNewFile();
                }


                filecontents = readAsConfigFile(configFile);
//                System.out.println(filecontents);

                String[] dataentries = filecontents.split("\\r?\\n");

                for (int i = 0; i < dataentries.length; i++) {
//                    for some reason splitting gives a blank first entry
                    String dataentry = dataentries[i];
                    if (dataentry.split("=").length == 2) {
                        String playername = deleteEndSpaces(dataentry.split("=")[0]);
                        String validity = deleteEndSpaces(dataentry.split("=")[1]);
                        if (!playername.isEmpty() && !validity.isEmpty()) {
                            validPlayerDeaths.put(playername, Boolean.valueOf(validity));
                        }
                    }
                }

            } catch (IOException theerror) {
                System.out.println("Something went wrong in minecraft_tools ./src/main/java/io.github.theuntraceables.setup.LoadWorldFunction while attempting to read/create " + minecrafttoolsfolder + "/valid_deaths.txt: " + theerror.toString());
            }


//            read/create ide_gamerules.txt-----------------------------------------------------------------------------
            ideGameRulesPath = Path.of(minecrafttoolsfolder + "/ide_gamerules.txt");
            if (ide) {
                configFile = new File(ideGameRulesPath.toUri());

                allow_suicide = true;
                allow_finddeath = true;
                allow_multiwarp = false;
                allow_warpdeath = true;
                deathwarp_time_window = 3600;
                warpdeath_cooldown = 1200;

                try {
                    if (configFile.exists()) {
                        filecontents = readAsConfigFile(configFile);
                        if (Objects.equals(filecontents, "")) {
                            writeIDEGameRules();
                        }
                        else {
                            String[] dataentries = configString.split("\\r?\\n");
                            for (int i = 0; i < dataentries.length; i++) {
                                if (dataentries[i].split("=").length == 2 && !dataentries[i].startsWith("#") && !dataentries[i].startsWith("//")) {
                                    String rule = dataentries[i].split("=")[0];
                                    String value = dataentries[i].split("=")[1];
                                    value = deleteEndSpaces(value);
                                    if (!rule.isEmpty() && !value.isEmpty()) {
                                        try {
                                            switch (deleteEndSpaces(rule.toLowerCase())) {
                                                case "allow_suicide", "allowsuicide", "allow suicide":
                                                    allow_suicide = Boolean.parseBoolean(value);
                                                    break;
                                                case "allow_finddeath", "allowfinddeath", "allow finddeath":
                                                    allow_finddeath = Boolean.parseBoolean(value);
                                                    break;
                                                case "allow_warpdeath", "allowwarpdeath", "allow warpdeath":
                                                    allow_warpdeath = Boolean.parseBoolean(value);
                                                    break;
                                                case "allow_multiwarp", "allowmultiwarp", "allow multiwarp":
                                                    allow_multiwarp = Boolean.parseBoolean(value);
                                                    break;
                                                case "warpdeath_cooldown", "warpdeath cooldown", "warpdeathcooldown":
                                                    warpdeath_cooldown = Integer.parseInt(value);
                                                    break;
                                                case "deathwarp_time_window", "deathwarptimewindow", "deathwarp time window":
                                                    deathwarp_time_window = Integer.parseInt(value);
                                            }
                                        } catch (Exception ignored){
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        writeIDEGameRules();
                    }
                }
                catch (IOException theerror) {
                    System.out.println("Something went wrong in minecraft_tools ./src/main/java/io.github.theuntraceables.setup.LoadWorldFunction while attempting to read/create " + minecrafttoolsfolder + "/ide_gamerules.txt: " + theerror.toString());
                }
            }

//            read/create warpuse_times.txt-----------------------------------------------------------------------------
            warpUseTimesPath = Path.of(minecrafttoolsfolder + "/warpuse_times.txt");
            configFile = new File(warpUseTimesPath.toUri());
            try {
                filecontents = "";
                if (configFile.exists()) {
                    filecontents = readAsConfigFile(configFile);
                    if (Objects.equals(filecontents, "")) {
                        configFile.createNewFile();
                    }
                } else {
                    configFile.createNewFile();
                }


                filecontents = readAsConfigFile(configFile);
//                System.out.println(filecontents);

                String[] dataentries = filecontents.split("\\r?\\n");

                for (int i = 0; i < dataentries.length; i++) {
//                    for some reason splitting gives a blank first entry
                    String dataentry = dataentries[i];
                    if (dataentry.split("=").length == 2) {
                        String playername = deleteEndSpaces(dataentry.split("=")[0]);
                        String validity = deleteEndSpaces(dataentry.split("=")[1]);
                        if (!playername.isEmpty() && !validity.isEmpty()) {
                            playerWarpDeathUseTimes.put(playername, Integer.valueOf(validity));
                        }
                    }
                }

            } catch (IOException theerror) {
                System.out.println("Something went wrong in minecraft_tools ./src/main/java/io.github.theuntraceables.setup.LoadWorldFunction while attempting to read/create " + minecrafttoolsfolder + "/warpuse_times.txt: " + theerror.toString());
            }


//            read/create death_times.txt------------------------------------------------------------------------------
            deathTimesPath = Path.of(minecrafttoolsfolder + "/death_times.txt");
            configFile = new File(deathTimesPath.toUri());
            try {
                filecontents = "";
                if (configFile.exists()) {
                    filecontents = readAsConfigFile(configFile);
                    if (Objects.equals(filecontents, "")) {
                        configFile.createNewFile();
                    }
                } else {
                    configFile.createNewFile();
                }


                filecontents = readAsConfigFile(configFile);
//                System.out.println(filecontents);

                String[] dataentries = filecontents.split("\\r?\\n");

                for (int i = 0; i < dataentries.length; i++) {
//                    for some reason splitting gives a blank first entry
                    String dataentry = dataentries[i];
                    if (dataentry.split("=").length == 2) {
                        String playername = deleteEndSpaces(dataentry.split("=")[0]);
                        String validity = deleteEndSpaces(dataentry.split("=")[1]);
                        if (!playername.isEmpty() && !validity.isEmpty()) {
                            playerDeathTimes.put(playername, Integer.valueOf(validity));
                        }
                    }
                }

            } catch (IOException theerror) {
                System.out.println("Something went wrong in minecraft_tools ./src/main/java/io.github.theuntraceables.setup.LoadWorldFunction while attempting to read/create " + minecrafttoolsfolder + "/death_times.txt: " + theerror.toString());
            }


            timesdone++;
        }
    }
}
