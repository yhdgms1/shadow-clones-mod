package dev.yhdgms1.shadow_clones;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class Helpers {
    private Helpers() {}

    public static int getClonesSpawnCount(float health, float maxHealth) {
        if (health <= 1) {
            return 0;
        } else if (health <= maxHealth * 0.5f) {
            return 1;
        } else if (health <= maxHealth * 0.75f) {
            return 2;
        } else {
            return 3;
        }
    }

    private static double getAndleStep(int clonesSpawnCount) {
        if (clonesSpawnCount == 1) {
            return 0;
        } else if (clonesSpawnCount == 2) {
            return 45;
        } else {
            return 60;
        }
    }

    private static Vec3d getSpawnPosition(double x, double y, double z, double yawRadians, double angleStep) {
        float radius = 2.0F;

        return new Vec3d(x - radius * Math.sin(yawRadians - angleStep), y, z + radius * Math.cos(yawRadians - angleStep));
    }

    public static List<Vec3d> getSpawnPositions(int clonesCount, ServerPlayerEntity player) {
        List<Vec3d> spawnPositions = new ArrayList<>();

        double playerX = player.getX();
        double playerY = player.getY();
        double playerZ = player.getZ();

        double yawRadians = Math.toRadians(player.getYaw());
        double angleStep = Math.toRadians(getAndleStep(clonesCount));

        if (clonesCount == 1) {
            spawnPositions.add(getSpawnPosition(playerX, playerY, playerZ, yawRadians, 0));
        } else if (clonesCount == 2) {
            spawnPositions.add(getSpawnPosition(playerX, playerY, playerZ, yawRadians, -angleStep));
            spawnPositions.add(getSpawnPosition(playerX, playerY, playerZ, yawRadians, angleStep));
        } else {
            spawnPositions.add(getSpawnPosition(playerX, playerY, playerZ, yawRadians, -angleStep));
            spawnPositions.add(getSpawnPosition(playerX, playerY, playerZ, yawRadians, 0));
            spawnPositions.add(getSpawnPosition(playerX, playerY, playerZ, yawRadians, angleStep));
        }

        return spawnPositions;
    }
}
