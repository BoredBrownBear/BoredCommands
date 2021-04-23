package boredbrownbear.boredcommands.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import boredbrownbear.boredcommands.commands.mycomm.TeleportRequests;
import boredbrownbear.boredcommands.helper.Location;
import boredbrownbear.boredcommands.helper.Permission;
import boredbrownbear.boredcommands.helper.Teleport;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

import java.util.List;
import java.util.UUID;


public class CommandTpAccept {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("tpaccept");
        literal.requires((source) -> Permission.hasperm(source, literal)).executes(context -> execute(context));
        dispatcher.register(literal);

        LiteralArgumentBuilder<ServerCommandSource> literal1 = CommandManager.literal("tpyes");
        literal1.requires((source) -> Permission.hasperm(source, literal)).executes(context -> execute(context));
        dispatcher.register(literal1);
    }

    public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        UUID playerUuid = player.getUuid();

        if (TeleportRequests.pending(player.getUuid())) {
            List<ServerPlayerEntity> playerlist = context.getSource().getMinecraftServer().getPlayerManager().getPlayerList();
            Boolean playerFound = false;

            for (int i = 0; i < playerlist.size(); ++i) {
                if (playerlist.get(i).getUuid().equals(TeleportRequests.fromWho((player.getUuid())))) {
                    playerFound = true;
                    ServerPlayerEntity teleporter = playerlist.get(i);
                    ServerPlayerEntity teleportTo = player;

                    teleporter.sendSystemMessage(new TranslatableText("commands.tpa.gotaccepted"), teleporter.getUuid());
                    teleportTo.sendSystemMessage(new TranslatableText("commands.tpa.youaccepted"), teleportTo.getUuid());
                    Teleport.warp(teleporter, teleportTo.getServerWorld(), new Location(teleportTo));
                }
            }
            if (!playerFound) {
                player.sendSystemMessage(new TranslatableText("commands.tpa.notonline"), playerUuid);
            }
            TeleportRequests.remove(playerUuid);
        } else {
            player.sendSystemMessage(new TranslatableText("commands.tpa.nonetoaccept"), playerUuid);
        }
        return Command.SINGLE_SUCCESS;
    }

}
