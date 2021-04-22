package boredbrownbear.boredcommands.helper;

import boredbrownbear.boredcommands.BoredCommands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.server.command.ServerCommandSource;

public class Permission {

    public static boolean hasperm(ServerCommandSource source, LiteralArgumentBuilder<ServerCommandSource> literal) {
        if (BoredCommands.config.getBoolean(literal.getLiteral())) {
            return source.hasPermissionLevel(4);
        } else {
            return true;
        }
    }

}
