package boredbrownbear.boredcommands.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import boredbrownbear.boredcommands.commands.mycomm.TeleportRequests;
import boredbrownbear.boredcommands.helper.MyStyle;
import boredbrownbear.boredcommands.helper.Permission;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.util.UUID;


public class CommandTpa {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("tpa");
        literal.requires((source) -> {
            return Permission.hasperm(source, literal);
        })
                .then(CommandManager.argument("target", EntityArgumentType.player()).executes(context -> execute(context)));

        dispatcher.register(literal);

    }

    public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        ServerPlayerEntity requestedPlayer = EntityArgumentType.getPlayer(context, "target");
        UUID requestedPlayerUuid = requestedPlayer.getUuid();
        UUID playerUuid = player.getUuid();

        if (requestedPlayerUuid != playerUuid) {
            TeleportRequests.add(requestedPlayerUuid, playerUuid);
            requestedPlayer.sendSystemMessage(new TranslatableText("commands.tpa.request", (new LiteralText(player.getEntityName()).setStyle(MyStyle.Gold))).setStyle(MyStyle.Green), requestedPlayerUuid);
            requestedPlayer.sendSystemMessage(new TranslatableText("commands.tpa.info", false).setStyle(MyStyle.Aqua), requestedPlayerUuid);
        } else {
            requestedPlayer.sendSystemMessage(new TranslatableText("commands.tpa.error", false).setStyle(MyStyle.Red), requestedPlayerUuid);
        }
        return Command.SINGLE_SUCCESS;
    }

}