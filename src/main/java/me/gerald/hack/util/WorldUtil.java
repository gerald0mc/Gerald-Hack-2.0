package me.gerald.hack.util;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class WorldUtil {
    public static List<BlockPos> getSphere(BlockPos pos, float r, float h, boolean hollow, boolean sphere, int plusY) {
        List<BlockPos> blocks = new ArrayList<>();
        for(int x = pos.getX() - (int) r; x <= pos.getX() + r; x++) {
            for(int y = (sphere ? pos.getY() - (int) r : pos.getY()); y < (sphere ? pos.getY() + r : pos.getY() + h); y++) {
                for(int z = pos.getZ() - (int) r; z <= pos.getZ() + r; z++) {
                    double dist = (pos.getX() - x) * (pos.getX() - x) + (pos.getZ() - z) * (pos.getZ() - z) + (sphere ? (pos.getY() - y) * (pos.getY() - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        blocks.add(new BlockPos(x, y + plusY, z));
                    }
                }
            }
        }
        return blocks;
    }

    public static List<BlockPos> getSphere(BlockPos pos, float r, boolean hollow) {
        return getSphere(pos, r, (int) r, hollow, true, 0);
    }
}
