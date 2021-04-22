package boredbrownbear.boredcommands.commands;

import boredbrownbear.boredcommands.BoredCommands;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import boredbrownbear.boredcommands.helper.MyStyle;
import boredbrownbear.boredcommands.helper.Permission;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.GameMode;

import java.util.UUID;


public class CommandFly {

    public static PlayerAbilities pla = new PlayerAbilities();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("fly");
        literal.requires((source) -> Permission.hasperm(source, literal)).executes(source -> execute(source))
                .then(CommandManager.argument("target", EntityArgumentType.players()).executes(context -> execut(context)));
        dispatcher.register(literal);
    }

    private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity playerEntity = context.getSource().getPlayer();
        UUID playerUuid = playerEntity.getUuid();

        if (playerEntity.interactionManager.getGameMode() == GameMode.SURVIVAL || playerEntity.interactionManager.getGameMode() == GameMode.ADVENTURE) {
            toggleFlying(playerEntity, playerUuid);
        } else {
            playerEntity.sendSystemMessage(new TranslatableText("commands.fly.error").setStyle(MyStyle.Red), playerUuid);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int execut(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity playerEntity = context.getSource().getPlayer();
        ServerPlayerEntity requestedPlayer = mycomm.getPlayer(context.getSource(), EntityArgumentType.getPlayers(context, "target"));
        UUID requestedPlayerUuid = playerEntity.getUuid();

        if (playerEntity.interactionManager.getGameMode() == GameMode.SURVIVAL || playerEntity.interactionManager.getGameMode() == GameMode.ADVENTURE) {
            toggleFlying(requestedPlayer, requestedPlayerUuid);
        } else {
            playerEntity.sendSystemMessage(new TranslatableText("commands.fly.error").setStyle(MyStyle.Red), playerEntity.getUuid());
        }
        return Command.SINGLE_SUCCESS;
    }

    private static void toggleFlying(ServerPlayerEntity player, UUID playerUuid) {
        if (!player.abilities.allowFlying) {
            player.abilities.allowFlying = true;
            player.sendAbilitiesUpdate();
            player.sendSystemMessage(new TranslatableText("commands.fly.enabled").setStyle(MyStyle.Green), playerUuid);
        } else {
            player.abilities.allowFlying = false;
            player.abilities.flying = false;
            player.sendAbilitiesUpdate();
            player.sendSystemMessage(new TranslatableText("commands.fly.disabled").setStyle(MyStyle.Green), playerUuid);
        }
    }

}