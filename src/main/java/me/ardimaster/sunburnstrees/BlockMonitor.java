/*
 * Copyright 2016 ArdiMaster
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

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;

/**
 * Created by ArdiMaster on 24.12.15.
 */
public class BlockMonitor extends BukkitRunnable {
    private SunBurnsTrees plugin;
    private HashSet<Block> blocks, removeBlocks;

    BlockMonitor(SunBurnsTrees mainClass) {
        plugin = mainClass;
    }

    @Override
    public void run() {
        blocks = (HashSet<Block>) plugin.monitorBlocks.clone();
        removeBlocks = new HashSet<>();
        for (Block block : blocks) {
            if (!plugin.burningMaterials.contains(block.getType())) {
                removeBlocks.add(block);
                continue;
            }

            long time = block.getWorld().getTime();
            if (block.getLightFromSky() == 14 && time >= plugin.minTime && time <= plugin.maxTime) {
                block.getRelative(BlockFace.UP).setType(Material.FIRE);
            }
        }

        plugin.isUpdatingChecks = true;
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        plugin.monitorBlocks.removeAll(removeBlocks);
        plugin.isUpdatingChecks = false;
    }
}
