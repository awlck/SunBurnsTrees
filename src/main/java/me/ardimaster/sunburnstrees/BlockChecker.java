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

/**
 * Created by ArdiMaster on 24.12.15.
 */
public class BlockChecker extends BukkitRunnable {
    SunBurnsTrees plugin;

    public BlockChecker(SunBurnsTrees mainClass) {
        plugin = mainClass;
    }

    @Override
    public void run() {
        HashSet<Block> remove = new HashSet<>();
        for (Block block : plugin.needsCheck) {
            // if (plugin.burningMaterials.contains(block.getType())) {
                if (block.getLightFromSky() >= plugin.burnLightLevel) {
                    block.getRelative(BlockFace.UP).setType(Material.FIRE);
                } else {
                    plugin.monitorBlocks.add(block);
                }
            // }

            remove.add(block);
        }

        if (!remove.isEmpty()) {
            for (Block removeBlock : remove) {
                plugin.needsCheck.remove(removeBlock);
            }

            remove.clear();
        }
    }
}
