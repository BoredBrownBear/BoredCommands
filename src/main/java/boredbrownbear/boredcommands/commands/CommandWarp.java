package boredbrownbear.boredcommands.commands;

import boredbrownbear.boredcommands.helper.*;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.util.UUID;


public class CommandWarp {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("warp");
        literal.requires((source) -> {
            return Permission.hasperm(source, literal);
        }).then(CommandManager.argument("WarpPointName", StringArgumentType.word())
                .executes(context -> execute(context)));
        dispatcher.register(literal);
    }

    public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String par2ArrayOfStr = StringArgumentType.getString(context, "WarpPointName").toString();
        ServerPlayerEntity player = context.getSource().getPlayer();
        UUID playerUuid = player.getUuid();

        if (par2ArrayOfStr == "" || par2ArrayOfStr == null) {
            player.sendSystemMessage(new LiteralText("WarpPointNames: " + WarpPoint.getWarpPoints()).setStyle(MyStyle.Aqua), playerUuid);
        } else {
            final String warpPointName = par2ArrayOfStr;
            WarpPoint warpPoint = WarpPoint.getWarpPoint(warpPointName);

            if (warpPoint != null) {
                Location loc = WarpPoint.getWarpPoint(warpPointName).location;
                Teleport.warp(player, player.getServerWorld(), loc);
                player.sendSystemMessage(new TranslatableText("commands.warp.done", warpPointName).setStyle(MyStyle.Green), playerUuid);
            } else {
                player.sendSystemMessage(new TranslatableText("commands.warp.failure", warpPointName).setStyle(MyStyle.Red), playerUuid);
                player.sendSystemMessage(new LiteralText("WarpPointNames: " + WarpPoint.getWarpPoints()).setStyle(MyStyle.Aqua), playerUuid);
            }
        }
        return Command.SINGLE_SUCCESS;
    }

}
