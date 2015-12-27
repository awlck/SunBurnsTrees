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

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
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
        // HashSet<Block> remove = new HashSet<>();

        for (Iterator<Block> iterator = plugin.monitorBlocks.iterator(); iterator.hasNext();) {
            Block  block = iterator.next();

            plugin.getServer().broadcastMessage("Checking block at x=" + block.getX() + ", y=" +
                    block.getY() + ", z=" + block.getZ() + " in world " + block.getWorld().getName() +
                    ".\n" + "LightFromSky is " + block.getLightFromSky() + ", total light level is " +
                    block.getLightLevel() + ".\n" + "Time is " + block.getWorld().getTime() + ".");
            if (!plugin.burningMaterials.contains(block.getType())) {
                // remove.add(block);
                iterator.remove();
                continue;
            }

            long time = block.getWorld().getTime();
            if (block.getLightFromSky() == plugin.burnLightLevel-1 && time > 4284 && time < 7698) {
                block.getRelative(BlockFace.UP).setType(Material.FIRE);
                // remove.add(block);
                iterator.remove();
            }
        }

        /* if (!remove.isEmpty()) {
            for (Block removeBlock : remove) {
                plugin.monitorBlocks.remove(removeBlock);
            }

            remove.clear();
        } */
    }
}
