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

import java.util.Iterator;

/**
 * Created by ArdiMaster on 24.12.15.
 */
public class BlockMonitor extends BukkitRunnable {
    SunBurnsTrees plugin;

    public BlockMonitor(SunBurnsTrees mainClass) {
        plugin = mainClass;
    }

    @Override
    public void run() {
        for (Iterator<Block> iterator = plugin.monitorBlocks.iterator(); iterator.hasNext();) {
            Block block = iterator.next();
            Material material = block.getType();

            if (!plugin.burningMaterials.contains(material)) {
                iterator.remove();
                continue;
            }

            long time = block.getWorld().getTime();
            if (time > plugin.minTime && time < plugin.maxTime) {
                continue;
            }

            if ((material == Material.LEAVES || material == Material.LEAVES_2)) {
                if (block.getLightFromSky() == 14) {
                    block.getRelative(BlockFace.UP).setType(Material.FIRE);
                }
            } else {
                if (block.getWorld().getHighestBlockAt(block.getLocation()) == block) {
                    block.getRelative(BlockFace.UP).setType(Material.FIRE);
                }
            }
        }
    }
}
