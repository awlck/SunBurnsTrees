/*
 * Copyright 2015 ArdiMaster
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package me.ardimaster.sunburnstrees;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by ArdiMaster on 23.12.15.
 */
public class SunBurnsTrees extends JavaPlugin {
    protected int burnLightLevel;
    protected HashSet<Block> needsCheck = new HashSet<>();
    protected HashSet<Block> monitorBlocks = new HashSet<>();
    protected HashSet<Material> burningMaterials = new HashSet<>();
    private BukkitTask blockMonitor, blockChecker, blocksSaver;
    private EventListener listener;


    @Override
    public void onEnable() {
        loadCfg();
        loadBlocks();
        listener = new EventListener(this);
        getServer().getPluginManager().registerEvents(listener, this);

        blockMonitor = new BlockMonitor(this).runTaskTimer(this, 16 * 20, 5 * 20);
        blockChecker = new BlockChecker(this).runTaskTimer(this, 20 * 20, 5 * 20);
        blocksSaver = new BlocksSaver(this).runTaskTimer(this, 31 * 20, 30 * 20);
    }

    @Override
    public void onDisable() {
        listener.setDisabling(true);
        blockChecker.cancel();
        blockMonitor.cancel();
        blocksSaver.cancel();
        saveCfg();
        saveBlocks();
    }

    void log(Level level, String message) {
        getLogger().log(level, message);
    }

    void loadCfg() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            log(Level.INFO, "No configuration file found, using defaults.");
            burnLightLevel = 14;
            burningMaterials.add(Material.LEAVES);
            burningMaterials.add(Material.LEAVES_2);
            return;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        burnLightLevel = config.getInt("burnlightlevel");

        List<String> loadingMaterials = config.getStringList("materials");
        for (String mat : loadingMaterials) {
            burningMaterials.add(Material.getMaterial(mat));
        }
    }

    void saveCfg() {
        File configFile = new File(getDataFolder(), "config.yml");

        if (!Files.exists(getDataFolder().toPath())) {
            try {
                Files.createDirectory(getDataFolder().toPath());
            } catch (IOException e) {
                log(Level.WARNING, "Unable to create plugin data folder! Not saving");
                e.printStackTrace();
                return;
            }
        }

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                log(Level.WARNING, "Unable to create empty config file! Not saving.");
                e.printStackTrace();
                return;
            }
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        config.set("burnlightlevel", burnLightLevel);

        ArrayList<String> materialSave = new ArrayList<>();
        for (Material mat : burningMaterials) {
            materialSave.add(mat.toString());
        }
        config.set("materials", materialSave);

        try {
            config.save(configFile);
        } catch (IOException e) {
            log(Level.WARNING, "Unable to save config file!");
        }
    }

    void loadBlocks() {
        File blockFile = new File(getDataFolder(), "blocks.yml");
        if (!blockFile.exists()) {
            log(Level.INFO, "No blocks file found, using empty.");
            return;
        }

        FileConfiguration blocks = YamlConfiguration.loadConfiguration(blockFile);

        int monitorCount = blocks.getInt("monitor.count");
        int checkCount = blocks.getInt("needcheck.count");

        for (int i = 0; i < monitorCount; i++) {
            String world = blocks.getString("monitor." + i + ".world");
            int x = blocks.getInt("monitor." + i + ".x");
            int y = blocks.getInt("monitor." + i + ".y");
            int z = blocks.getInt("monitor." + i + ".z");
            monitorBlocks.add(new Location(getServer().getWorld(world), x, y, z).getBlock());
        }

        for (int j = 0; j < checkCount; j++) {
            String world = blocks.getString("needcheck." + j + ".world");
            int x = blocks.getInt("needcheck." + j + ".x");
            int y = blocks.getInt("needcheck." + j + ".y");
            int z = blocks.getInt("needcheck." + j + ".z");
            needsCheck.add(new Location(getServer().getWorld(world), x, y, z).getBlock());
        }
    }

    void saveBlocks() {
        File blocksFile = new File(getDataFolder(), "blocks.yml");

        if (!Files.exists(getDataFolder().toPath())) {
            try {
                Files.createDirectory(getDataFolder().toPath());
            } catch (IOException e) {
                log(Level.WARNING, "Unable to create plugin data folder! Not saving");
                e.printStackTrace();
                return;
            }
        }

        try {
            blocksFile.createNewFile();
        } catch (IOException e) {
            log(Level.WARNING, "Unable to create empty blocks file! Not saving.");
            e.printStackTrace();
            return;
        }


        FileConfiguration blocks = YamlConfiguration.loadConfiguration(blocksFile);

        HashMap<Integer, Block> monitorSave = new HashMap<>();
        int i = 0;
        for (Block block : monitorBlocks) {
            monitorSave.put(i, block);
            i++;
        }

        HashMap<Integer, Block> checkSave = new HashMap<>();
        int j = 0;
        for (Block block : needsCheck) {
            checkSave.put(j, block);
            j++;
        }

        for (int k = 0; k < i; k++) {
            Location loc = monitorSave.get(k).getLocation();
            blocks.set("monitor." + k + ".world", loc.getWorld().getName());
            blocks.set("monitor." + k + ".x", loc.getBlockX());
            blocks.set("monitor." + k + ".y", loc.getBlockY());
            blocks.set("monitor." + k + ".z", loc.getBlockZ());
        }

        for (int l = 0; l < j; l++) {
            Location loc = checkSave.get(l).getLocation();
            blocks.set("needcheck." + l + ".world", loc.getWorld().getName());
            blocks.set("needcheck." + l + ".x", loc.getBlockX());
            blocks.set("needcheck." + l + ".y", loc.getBlockY());
            blocks.set("needcheck." + l + ".z", loc.getBlockZ());
        }

        blocks.set("monitor.count", i);
        blocks.set("needcheck.count", j);

        try {
            blocks.save(blocksFile);
        } catch (IOException e) {
            log(Level.WARNING, "Unable to save blocks file!");
        }
    }
}
