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

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;

/**
 * Created by ArdiMaster on 23.12.15.
 */
public class SunBurnsTrees extends JavaPlugin {
    protected int burnLightLevel;

    @Override
    public void onEnable() {
        loadCfg();
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
    }

    @Override
    public void onDisable() {
        saveCfg();
    }

    void log(Level level, String message) {
        getLogger().log(level, message);
    }

    void loadCfg() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            log(Level.INFO, "No configuration file found, using defaults.");
            burnLightLevel = 15;
            return;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        burnLightLevel = config.getInt("burnlightlevel");
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
        try {
            config.save(configFile);
        } catch (IOException e) {
            log(Level.WARNING, "Unable to save config file!");
        }
    }
}
