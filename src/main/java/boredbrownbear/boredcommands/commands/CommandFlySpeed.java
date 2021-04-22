package boredbrownbear.boredcommands.commands;

import boredbrownbear.boredcommands.BoredCommands;
import boredbrownbear.boredcommands.helper.MyStyle;
import boredbrownbear.boredcommands.helper.Permission;
import boredbrownbear.boredcommands.mixins.MixinPlayerAbilitiesAccessor;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.util.UUID;


public class CommandFlySpeed {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("flyspeed");
        literal.requires((source) -> Permission.hasperm(source, literal)).executes(context -> resetFlySpeed(context))
                .then(CommandManager.argument("speed", FloatArgumentType.floatArg()).
                        executes(context -> setFlySpeed(context)));
        dispatcher.register(literal);
    }

    public static int setFlySpeed(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Float flySpeed = FloatArgumentType.getFloat(context, "speed");
        ServerPlayerEntity player = context.getSource().getPlayer();
        UUID playerUuid = player.getUuid();

        if (player.abilities.allowFlying) {
            ((MixinPlayerAbilitiesAccessor) player.abilities).setFlySpeed(flySpeed / 20f);
            player.sendAbilitiesUpdate();
            player.sendSystemMessage(new TranslatableText("commands.flyspeed.done", (new LiteralText(flySpeed.toString()).setStyle(MyStyle.Gold))).setStyle(MyStyle.Green), playerUuid);
        } else {
            player.sendSystemMessage(new TranslatableText("commands.flyspeed.error").setStyle(MyStyle.Red), playerUuid);
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int resetFlySpeed(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        UUID playerUuid = player.getUuid();

        if (player.abilities.allowFlying) {
            ((MixinPlayerAbilitiesAccessor) player.abilities).setFlySpeed(0.05f);
            player.sendAbilitiesUpdate();
            player.sendSystemMessage(new TranslatableText("commands.flyspeed.reset").setStyle(MyStyle.Green), playerUuid);
        } else {
            player.sendSystemMessage(new TranslatableText("commands.flyspeed.error").setStyle(MyStyle.Red), playerUuid);
        }
        return Command.SINGLE_SUCCESS;
    }

}
