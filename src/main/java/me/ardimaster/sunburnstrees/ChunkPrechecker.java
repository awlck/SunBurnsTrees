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

import org.bukkit.ChunkSnapshot;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by adrianwelcker on 11.05.16.
 */
public class ChunkPrechecker extends BukkitRunnable {
    private SunBurnsTrees plugin;
    private ChunkSnapshot chunk;

    ChunkPrechecker(SunBurnsTrees mainClass, ChunkSnapshot checkChunk) {
        plugin = mainClass;
        chunk = checkChunk;
    }

    @Override
    public void run() {
        for (int sy = 0; sy < 16; sy++) {
            if (!chunk.isSectionEmpty(sy)) {
                plugin.addChunkSectionToNeedsCheck(chunk.getWorldName(), chunk.getX(), sy, chunk.getZ());
            }
        }

        plugin.addCleanChunk(chunk.getWorldName(), chunk.getX(), chunk.getZ());
    }
}
