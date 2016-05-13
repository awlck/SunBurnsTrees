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
    private int id;

    ChunkPrechecker(SunBurnsTrees mainClass, ChunkSnapshot checkChunk, int runnerId) {
        plugin = mainClass;
        chunk = checkChunk;
        id = runnerId;
    }

    @Override
    public void run() {
        /* String chunkWorldName = chunk.getWorldName();
        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();
        while (plugin.currentDone < id) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                plugin.checkChunkColumn(chunkWorldName, chunkX, chunkZ, x, z, chunk.getHighestBlockYAt(x, z));
            }
        }

        plugin.addCleanChunk(chunk.getWorldName(), chunk.getX(), chunk.getZ()); */
    }
}
