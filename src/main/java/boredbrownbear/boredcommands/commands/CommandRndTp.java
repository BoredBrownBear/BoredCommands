package boredbrownbear.boredcommands.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import boredbrownbear.boredcommands.helper.Location;
import boredbrownbear.boredcommands.helper.Permission;
import boredbrownbear.boredcommands.helper.Teleport;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public class CommandRndTp {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("rndtp");
        literal.requires((source) -> {
            return Permission.hasperm(source, literal);
        })
                .executes(context -> {
                    return execute(context);
                });

        dispatcher.register(literal);

        LiteralArgumentBuilder<ServerCommandSource> literal1 = CommandManager.literal("rtp");
        literal1.requires((source) -> {
            return Permission.hasperm(source, literal);
        })
                .executes(context -> {
                    return execute(context);
                });

        dispatcher.register(literal1);

        LiteralArgumentBuilder<ServerCommandSource> literal2 = CommandManager.literal("wild");
        literal2.requires((source) -> {
            return Permission.hasperm(source, literal);
        })
                .executes(context -> {
                    return execute(context);
                });

        dispatcher.register(literal2);
    }

    public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        BlockPos pos1 = findBlockPos(player.getEntityWorld()).getPosfrom();


        while (player.isInsideWall() || player.world.getBlockState(pos1).getBlock() == Blocks.WATER || player.world.getBlockState(pos1).getBlock() == Blocks.LAVA) {
            pos1 = findBlockPos(player.getEntityWorld()).getPosfrom();
            pos1 = player.getEntityWorld().getTopPosition(Heightmap.Type.WORLD_SURFACE, pos1);
        }

        Teleport.warp(player, player.getServerWorld(), new Location(pos1));
        return Command.SINGLE_SUCCESS;
    }

    public static Location findBlockPos(World world) {
        double dist = 150 + world.random.nextDouble() * (150 - 50);
        double angle = world.random.nextDouble() * Math.PI * 2D;

        int x = MathHelper.floor(Math.cos(angle) * dist);
        int y = 256;
        int z = MathHelper.floor(Math.sin(angle) * dist);


        //TODO: Find a better way to check for biome without loading the chunk
        Biome biome = world.getBiome(new BlockPos(x, y, z));
        if (biome.getCategory().getName().equals("ocean")) {
            return findBlockPos(world);
        }

        Chunk chunk = world.getChunk(x >> 4, z >> 4);

        while (y > 0) {
            y--;

            if (chunk.getBlockState(new BlockPos(x, y, z)).getMaterial() != Material.AIR) {
                return new Location(x + 0.5D, y + 2.5D, z + 0.5D, 0, 0);
            }
        }

        return findBlockPos(world);
    }

}