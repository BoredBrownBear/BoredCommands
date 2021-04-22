package boredbrownbear.boredcommands.helper;

import java.util.Collections;
import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;

public class Teleport {
    public static HashMap<ServerPlayerEntity, Location> playerBackMap = new HashMap<ServerPlayerEntity, Location>();

    /**
     * Send player to location.
     */
    public static void warp(Entity target, ServerWorld world, Location loc) {
        if (target instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) target;

            if (playerBackMap.containsKey(player)) {
                Teleport.playerBackMap.remove(player);
            }
            playerBackMap.put(player, new Location(player));

            ChunkPos chunkPos = new ChunkPos(new BlockPos(loc.x, loc.y, loc.z));
            world.getChunkManager().addTicket(ChunkTicketType.POST_TELEPORT, chunkPos, 1, target.getEntityId());
            player.stopRiding();
            if (player.isSleeping()) player.wakeUp(true, true);
            if (world == player.world)
                player.networkHandler.teleportRequest(loc.x, loc.y, loc.z, loc.yaw, loc.pitch, Collections.emptySet());
            else player.teleport(world, loc.x, loc.y, loc.z, loc.yaw, loc.pitch);
            player.setHeadYaw(loc.yaw);
        } else {
            float f = MathHelper.wrapDegrees(loc.yaw);
            float g = MathHelper.wrapDegrees(loc.pitch);
            g = MathHelper.clamp(g, -90.0F, 90.0F);
            if (world == target.world) {
                target.refreshPositionAndAngles(loc.x, loc.y, loc.z, f, g);
                target.setHeadYaw(f);
            } else {
                target.detach();
                Entity entity = target;
                target = target.getType().create(world);
                if (target == null) return;
                target.copyFrom(entity);
                target.refreshPositionAndAngles(loc.x, loc.y, loc.z, f, g);
                target.setHeadYaw(f);
                world.onDimensionChanged(target);
                entity.removed = true;
            }
        }
        if (!(target instanceof LivingEntity) || !((LivingEntity) target).isFallFlying()) {
            target.setVelocity(target.getVelocity().multiply(1.0D, 0.0D, 1.0D));
            target.setOnGround(true);
        }
        if (target instanceof PathAwareEntity) ((PathAwareEntity) target).getNavigation().stop();
    }

    /**
     * @param player: the player teleporting
     * @param target: the player name of the target
     */
    public static void warp(ServerPlayerEntity player, ServerPlayerEntity target) {
        warp(player, target.getServerWorld(), new Location(target));
    }

    /**
     * Send player to location he is looking at.
     *
     * @param player
     */
    public static void jump(ServerPlayerEntity player) {
        warp(player, player.getServerWorld(), new Location(player));
    }

    public static boolean goBack(ServerPlayerEntity player) {
        Location loc;
        if (playerBackMap.containsKey(player)) {
            loc = playerBackMap.get(player);
            warp(player, player.getServerWorld(), loc);
            return true;
        } else {
            return false;
        }
    }
}

