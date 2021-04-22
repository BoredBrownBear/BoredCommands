package boredbrownbear.boredcommands.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import boredbrownbear.boredcommands.helper.MyStyle;
import boredbrownbear.boredcommands.helper.Permission;
import boredbrownbear.boredcommands.helper.Teleport;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

import java.util.UUID;

public class CommandBack {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("back");
        literal.requires((source) -> {
            return Permission.hasperm(source, literal);
        }).executes(context -> execute(context));
        dispatcher.register(literal);

    }

    public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        UUID playerUuid = player.getUuid();

        if (Teleport.goBack(player)) {
            player.sendSystemMessage(new TranslatableText("commands.back.done").setStyle(MyStyle.Green), playerUuid);
        } else {
            player.sendSystemMessage(new TranslatableText("commands.back.failure").setStyle(MyStyle.Red), playerUuid);
        }
        return Command.SINGLE_SUCCESS;
    }
}