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

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.StructureGrowEvent;

/**
 * Created by ArdiMaster on 23.12.2015.
 */
public class EventListener implements Listener {
    private SunBurnsTrees plugin;
    private boolean disabling = false;

    EventListener(SunBurnsTrees mainClass) {
        this.plugin = mainClass;
    }

    @EventHandler
    public void onStructureGrow(StructureGrowEvent event) {
        if (disabling) { return; }
        for (BlockState blockState : event.getBlocks()) {
            plugin.needsCheck.add(blockState.getBlock());
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (disabling) { return; }
        Block block = event.getBlock();
        Material blockType = block.getType();
        if (plugin.burningMaterials.contains(blockType)) {
            plugin.needsCheck.add(event.getBlock());
        }
    }

    @EventHandler
    public void onBlockMine(BlockBreakEvent event) {
        if (disabling) { return; }
        Block block = event.getBlock();

        if (plugin.needsCheck.contains(block)) {
            plugin.needsCheck.remove(block);
        }

        if (plugin.monitorBlocks.contains(block)) {
            plugin.monitorBlocks.remove(block);
        }
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        if (disabling) { return; }
        Block block = event.getBlock();

        if (plugin.needsCheck.contains(block)) {
            plugin.needsCheck.remove(block);
        }

        if (plugin.monitorBlocks.contains(block)) {
            plugin.monitorBlocks.remove(block);
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if (disabling) { return; }

        Block block = event.getBlock();

        if (plugin.needsCheck.contains(block)) {
            plugin.needsCheck.remove(block);
        }

        if (plugin.monitorBlocks.contains(block)) {
            plugin.monitorBlocks.remove(block);
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) throws InterruptedException {
        if (!plugin.checkChunksCompletely || disabling) {
            return;
        }
        // new ChunkPrechecker(plugin, event.getChunk().getChunkSnapshot(), plugin.currentMax).runTaskAsynchronously(plugin);
        // plugin.currentMax++;

        Chunk chunk = event.getChunk();
        if (plugin.cleanChunks.contains(chunk)) {
            return;
        }
        while (plugin.isUpdatingChecks) {
            Thread.sleep(25);
        }

        ChunkSnapshot snapshot = chunk.getChunkSnapshot();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int highest = snapshot.getHighestBlockYAt(x, z);
                for (int y = 1; y <= highest; y++) {
                    Block block = chunk.getBlock(x, y, z);
                    if (plugin.burningMaterials.contains(block.getType())) {
                        plugin.monitorBlocks.add(block);
                    }
                }
            }
        }
        Bukkit.broadcastMessage("Finished checking chunk at X=" + chunk.getX() + " Z=" + chunk.getZ());
        plugin.cleanChunks.add(chunk);
    }

    public void setDisabling(boolean setTo) {
        this.disabling = setTo;
    }
}
