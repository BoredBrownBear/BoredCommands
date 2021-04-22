package boredbrownbear.boredcommands.commands;


import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import boredbrownbear.boredcommands.helper.HomePoint;
import boredbrownbear.boredcommands.helper.MyStyle;
import boredbrownbear.boredcommands.helper.Permission;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

import java.util.UUID;


public class CommandDelHome {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("delhome");
        literal.requires((source) -> {
            return Permission.hasperm(source, literal);
        }).then(CommandManager.argument("HomeName", StringArgumentType.word())
                .executes(context -> execute(context)));
        dispatcher.register(literal);
    }

    public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String args = StringArgumentType.getString(context, "HomeName").toString();
        ServerPlayerEntity player = context.getSource().getPlayer();
        UUID playerUuid = player.getUuid();
        String homePointName = args;
        HomePoint warpPoint = HomePoint.getHome(player, homePointName);

        if (warpPoint == null) {
            player.sendSystemMessage(new TranslatableText("commands.delhome.failure", homePointName).setStyle(MyStyle.Red), playerUuid);
            player.sendSystemMessage(new TranslatableText("commands.home.list", HomePoint.gethomePoints(player)).setStyle(MyStyle.Aqua), playerUuid);

        } else {
            HomePoint.delHomePoint(player, homePointName);
            player.sendSystemMessage(new TranslatableText("commands.delhome.done", homePointName).setStyle(MyStyle.Green), playerUuid);

        }
        return Command.SINGLE_SUCCESS;
    }


}
