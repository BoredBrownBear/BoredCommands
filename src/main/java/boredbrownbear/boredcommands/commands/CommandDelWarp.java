package boredbrownbear.boredcommands.commands;


import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import boredbrownbear.boredcommands.helper.MyStyle;
import boredbrownbear.boredcommands.helper.Permission;
import boredbrownbear.boredcommands.helper.WarpPoint;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

import java.util.UUID;


public class CommandDelWarp {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("delwarp");
        literal.requires((source) -> {
            return Permission.hasperm(source, literal);
        })
                .then(CommandManager.argument("WarpPointName", StringArgumentType.word()).
                        executes(context -> execute(context)));
        dispatcher.register(literal);
    }

    public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String args = StringArgumentType.getString(context, "WarpPointName").toString();
        ServerPlayerEntity player = context.getSource().getPlayer();
        UUID playerUuid = player.getUuid();

        if (args == null || args == "") {

        } else {
            final String warpPointName = args;

            WarpPoint warpPoint = WarpPoint.getWarpPoint(warpPointName);

            if (warpPoint == null) {
                player.sendSystemMessage(new TranslatableText("commands.delwarp.failure", warpPointName).setStyle(MyStyle.Red), playerUuid);

            } else {
                WarpPoint.delWarpPoint(warpPointName);
                player.sendSystemMessage(new TranslatableText("commands.delwarp.done", warpPointName).setStyle(MyStyle.Green), playerUuid);
            }
        }
        return Command.SINGLE_SUCCESS;
    }

}
