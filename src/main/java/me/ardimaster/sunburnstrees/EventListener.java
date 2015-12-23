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
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.StructureGrowEvent;

import java.util.List;

/**
 * Created by ArdiMaster on 23.12.2015.
 */
public class EventListener implements Listener {
    private SunBurnsTrees plugin;

    public EventListener(SunBurnsTrees mainClass) {
        this.plugin = mainClass;
    }

    public void onStructureGrow(StructureGrowEvent event) {
        for (BlockState blockState : event.getBlocks()) {
            plugin.needsCheck.add(blockState.getBlock());
        }
    }

    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Material blockType = block.getType();
        if (blockType == Material.LEAVES || blockType == Material.LEAVES_2) {
            plugin.needsCheck.add(event.getBlock());
        }
    }
}
