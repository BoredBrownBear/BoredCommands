package boredbrownbear.boredcommands.commands;

import boredbrownbear.boredcommands.BoredCommands;
import boredbrownbear.boredcommands.helper.MyStyle;
import boredbrownbear.boredcommands.helper.Permission;
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

import java.util.Set;
import java.util.UUID;


public class CommandWalkSpeed {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("walkspeed");
        literal.requires((source) -> Permission.hasperm(source, literal)).executes(context -> resetWalkSpeed(context))
                .then(CommandManager.argument("speed", FloatArgumentType.floatArg()).
                        executes(context -> setWalkSpeed(context)));
        dispatcher.register(literal);
    }

    public static int setWalkSpeed(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Float walkSpeed = FloatArgumentType.getFloat(context, "speed");
        ServerPlayerEntity player = context.getSource().getPlayer();
        UUID playerUuid = player.getUuid();
        EntityAttributeInstance speed_attribute = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);

        if (walkSpeed >= 0.01f) {
            // Ensure there are no current speed modifiers
            removeWalkSpeedModifier(speed_attribute);
            speed_attribute.addTemporaryModifier(new EntityAttributeModifier(BoredCommands.WALK_SPEED_MODIFIER, walkSpeed-1, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
            player.sendSystemMessage(new TranslatableText("commands.walkspeed.done", (new LiteralText(walkSpeed.toString()).setStyle(MyStyle.Gold))).setStyle(MyStyle.Green), playerUuid);
        } else {
            player.sendSystemMessage(new TranslatableText("commands.walkspeed.error", (new LiteralText("0.01").setStyle(MyStyle.Gold))).setStyle(MyStyle.Red), playerUuid);
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int resetWalkSpeed(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        UUID playerUuid = player.getUuid();
        EntityAttributeInstance speed_attribute = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        removeWalkSpeedModifier(speed_attribute);
        player.sendSystemMessage(new TranslatableText("commands.walkspeed.reset").setStyle(MyStyle.Green), playerUuid);
        return Command.SINGLE_SUCCESS;
    }

    private static void removeWalkSpeedModifier(EntityAttributeInstance attribute) {
        for (EntityAttributeModifier mod : attribute.getModifiers()) {
            if (mod.getName().equals(BoredCommands.WALK_SPEED_MODIFIER)) {
                attribute.removeModifier(mod.getId());
            }
        }
    }

}



