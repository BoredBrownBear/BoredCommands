package boredbrownbear.boredcommands.commands;

import boredbrownbear.boredcommands.helper.Permission;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class CommandSuicide {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("suicide");
        literal.requires((source) -> Permission.hasperm(source, literal)).executes(context -> execute(context));
        dispatcher.register(literal);
    }

    public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Entity entity = context.getSource().getEntityOrThrow();
        entity.damage(new EntityDamageSource("suicide", entity), Float.MAX_VALUE);
        entity.kill();
        return Command.SINGLE_SUCCESS;
    }

}