package boredbrownbear.boredcommands.commands;

import java.util.List;
import java.util.UUID;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import boredbrownbear.boredcommands.helper.Permission;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

public class CommandTpDeny {
	
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("tpdeny");
		literal.requires((source) -> {
			return Permission.hasperm(source, literal);
        }).executes(context -> execute(context));
            
		dispatcher.register(literal);
		
		LiteralArgumentBuilder<ServerCommandSource> literal1 = CommandManager.literal("tpno");
		literal1.requires((source) -> {
			return Permission.hasperm(source, literal);
        }).executes(context -> execute(context));
            
		dispatcher.register(literal1);
	}

	public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = context.getSource().getPlayer();
		UUID playerUuid = player.getUuid();

		if (mycomm.TeleportRequests.pending(player.getUuid())) {
			player.sendSystemMessage(new TranslatableText("commands.tpa.youdenied"), playerUuid);
			List<ServerPlayerEntity> playerlist = context.getSource().getMinecraftServer().getPlayerManager().getPlayerList();
			for (int i = 0; i < playerlist.size(); ++ i) {
				if (playerlist.get(i).getUuid().equals(mycomm.TeleportRequests.fromWho(player.getUuid()))) {
					playerlist.get(i).sendSystemMessage(new TranslatableText("commands.tpa.gotdenied"), playerlist.get(i).getUuid());
				}
			}
			mycomm.TeleportRequests.remove(player.getUuid());

		} else {
			player.sendSystemMessage(new TranslatableText("commands.tpa.nonetodeny"), playerUuid);
		}
		return Command.SINGLE_SUCCESS;
	}
}