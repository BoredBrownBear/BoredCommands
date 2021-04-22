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

public class CommandSetHome {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("sethome");
        literal.requires((source) -> {
            return Permission.hasperm(source, literal);
        }).executes(context -> execut(context))

                .then(CommandManager.argument("HomeName", StringArgumentType.word()).
                        executes(context -> execute(context)));

        dispatcher.register(literal);
    }

    public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String args = StringArgumentType.getString(context, "HomeName").toString();
        ServerPlayerEntity player = context.getSource().getPlayer();
        UUID playerUuid = player.getUuid();
        int homes = HomePoint.getHomecounts(player);

        if (homes < 5) {
            HomePoint home = HomePoint.getHome(player, args);

            if (home == null) {
                HomePoint.setHome(player, args);
                player.sendSystemMessage(new TranslatableText("commands.sethome.done", HomePoint.getHome(player, args).homename).setStyle(MyStyle.Green), playerUuid);
            } else {
                player.sendSystemMessage(new TranslatableText("commands.sethome.failure", args).setStyle(MyStyle.Red), playerUuid);
                player.sendSystemMessage(new TranslatableText("commands.home.list", HomePoint.gethomePoints(player)).setStyle(MyStyle.Aqua), playerUuid);
            }
        } else {
            player.sendSystemMessage(new TranslatableText("commands.sethome.maximum").setStyle(MyStyle.Red), playerUuid);
            player.sendSystemMessage(new TranslatableText("commands.home.list", HomePoint.gethomePoints(player)).setStyle(MyStyle.Aqua), playerUuid);
        }

        return 1;
    }

    public static int execut(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        UUID playerUuid = player.getUuid();
        int homes = HomePoint.getHomecounts(player);

        if (homes < 5) {
            HomePoint home = HomePoint.getHome(player, "home");
            if (home == null) {
                HomePoint.setHome(player, "home");
                player.sendSystemMessage(new TranslatableText("commands.sethome.done", HomePoint.getHome(player, "home").homename).setStyle(MyStyle.Green), playerUuid);
            } else {
                player.sendSystemMessage(new TranslatableText("commands.sethome.failure", "home").setStyle(MyStyle.Red), playerUuid);
                player.sendSystemMessage(new TranslatableText("commands.home.list", HomePoint.gethomePoints(player)).setStyle(MyStyle.Aqua), playerUuid);
            }
        } else {
            player.sendSystemMessage(new TranslatableText("commands.sethome.maximum").setStyle(MyStyle.Red), playerUuid);
            player.sendSystemMessage(new TranslatableText("commands.home.list", HomePoint.gethomePoints(player)).setStyle(MyStyle.Aqua), playerUuid);
        }
        return Command.SINGLE_SUCCESS;
    }
}
