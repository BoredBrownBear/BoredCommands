package boredbrownbear.boredcommands.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import boredbrownbear.boredcommands.helper.MyStyle;
import boredbrownbear.boredcommands.helper.Permission;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

public class CommandSetSpawn {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("setspawn");
        literal.requires((source) -> {
            return Permission.hasperm(source, literal);
        }).executes(context -> execute(context));

        dispatcher.register(literal);

    }

    public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        context.getSource().getWorld().setSpawnPos(player.getBlockPos().up(), player.yaw);
        player.sendSystemMessage(new TranslatableText("commands.setspawn.done").setStyle(MyStyle.Green), player.getUuid());
        return Command.SINGLE_SUCCESS;
    }
}
