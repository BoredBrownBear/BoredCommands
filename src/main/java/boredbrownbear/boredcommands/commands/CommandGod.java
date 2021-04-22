package boredbrownbear.boredcommands.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import boredbrownbear.boredcommands.helper.MyStyle;
import boredbrownbear.boredcommands.helper.Permission;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.GameMode;

import java.util.UUID;


public class CommandGod {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("god");
        literal.requires((source) -> {
            return Permission.hasperm(source, literal);
        }).executes(source -> execute(source))
                .then(CommandManager.argument("target", EntityArgumentType.players()).executes(context -> execut(context)));

        dispatcher.register(literal);
    }

    public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity playerEntity = context.getSource().getPlayer();
        UUID playerUuid = playerEntity.getUuid();

        if (playerEntity.interactionManager.getGameMode() == GameMode.SURVIVAL || playerEntity.interactionManager.getGameMode() == GameMode.ADVENTURE) {

            if (!playerEntity.abilities.invulnerable) {
                playerEntity.abilities.invulnerable = true;
                playerEntity.sendAbilitiesUpdate();
                playerEntity.sendSystemMessage(new TranslatableText("commands.god.enabled").setStyle(MyStyle.Green), playerUuid);
            } else {
                playerEntity.abilities.invulnerable = false;
                playerEntity.sendAbilitiesUpdate();
                playerEntity.sendSystemMessage(new TranslatableText("commands.god.disabled").setStyle(MyStyle.Green), playerUuid);
            }
        } else {
            playerEntity.sendSystemMessage(new TranslatableText("commands.god.error").setStyle(MyStyle.Red), playerUuid);
        }

        return 1;
    }

    private static int execut(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity playerEntity = context.getSource().getPlayer();
        ServerPlayerEntity requestedPlayer = mycomm.getPlayer(context.getSource(), EntityArgumentType.getPlayers(context, "target"));
        UUID requestedPlayerUuid = requestedPlayer.getUuid();

        if (playerEntity.interactionManager.getGameMode() == GameMode.SURVIVAL || playerEntity.interactionManager.getGameMode() == GameMode.ADVENTURE) {
            if (!requestedPlayer.abilities.invulnerable) {
                requestedPlayer.abilities.invulnerable = true;
                requestedPlayer.sendAbilitiesUpdate();
                requestedPlayer.sendSystemMessage(new TranslatableText("commands.god.enabled").setStyle(MyStyle.Green), requestedPlayerUuid);
            } else {
                requestedPlayer.abilities.invulnerable = false;
                requestedPlayer.sendAbilitiesUpdate();
                requestedPlayer.sendSystemMessage(new TranslatableText("commands.god.disabled").setStyle(MyStyle.Green), requestedPlayerUuid);
            }
        } else {
            playerEntity.sendSystemMessage(new TranslatableText("commands.god.error").setStyle(MyStyle.Red), requestedPlayerUuid);
        }
        return Command.SINGLE_SUCCESS;
    }
}