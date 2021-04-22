package boredbrownbear.boredcommands.commands;

import boredbrownbear.boredcommands.helper.MyStyle;
import boredbrownbear.boredcommands.helper.Permission;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;


public class CommandHeal {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("heal");
        literal.requires((source) -> {
            return Permission.hasperm(source, literal);
        }).executes(source -> execute(source))
                .then(CommandManager.argument("target", EntityArgumentType.players()).executes(context -> execut(context)));
        dispatcher.register(literal);
    }

    private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity playerEntity = context.getSource().getPlayer();
        playerEntity.setHealth(playerEntity.getMaxHealth());
        playerEntity.getHungerManager().add(20, 20);
        playerEntity.sendSystemMessage(new TranslatableText("commands.heal.self.done").setStyle(MyStyle.Green), playerEntity.getUuid());
        return Command.SINGLE_SUCCESS;
    }

    private static int execut(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity requestedPlayer = mycomm.getPlayer(context.getSource(), EntityArgumentType.getPlayers(context, "target"));
        ServerPlayerEntity healer = context.getSource().getPlayer();
        requestedPlayer.setHealth(requestedPlayer.getMaxHealth());
        requestedPlayer.getHungerManager().add(20, 20);
        requestedPlayer.sendSystemMessage(new TranslatableText("commands.heal.target.done", (new LiteralText(healer.getEntityName()).setStyle(MyStyle.Gold))).setStyle(MyStyle.Green), requestedPlayer.getUuid());
        return Command.SINGLE_SUCCESS;
    }
}