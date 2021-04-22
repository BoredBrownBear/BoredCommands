package boredbrownbear.boredcommands.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import boredbrownbear.boredcommands.helper.Location;
import boredbrownbear.boredcommands.helper.Permission;
import boredbrownbear.boredcommands.helper.Teleport;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;


public class CommandSpawn 
{
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("spawn");
		literal.requires((source) -> {
			return Permission.hasperm(source, literal);
        }).executes(context -> execute(context));
           
		dispatcher.register(literal);
            
	}

	public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = context.getSource().getPlayer();

		ServerWorld world = context.getSource().getWorld().toServerWorld();

		double x = world.getLevelProperties().getSpawnX();
		double y = world.getLevelProperties().getSpawnY();
		double z = world.getLevelProperties().getSpawnZ();
		
		Teleport.warp(player, world, new Location(x, y, z, 0, 0));
		return Command.SINGLE_SUCCESS;
	}
}
