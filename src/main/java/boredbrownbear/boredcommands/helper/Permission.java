package boredbrownbear.boredcommands.helper;

import boredbrownbear.boredcommands.BoredCommands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.luckperms.api.cacheddata.CachedPermissionData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import net.luckperms.api.util.Tristate;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class Permission {

    public static boolean hasperm(ServerCommandSource source, LiteralArgumentBuilder<ServerCommandSource> literal) {
        ServerPlayerEntity player;
        try {
            player = source.getPlayer();
        } catch (CommandSyntaxException e) {
            BoredCommands.LOGGER.error(e.getMessage());
            return false;
        }

        User user = BoredCommands.luckperms.getUserManager().getUser(player.getUuid());
        QueryOptions queryOptions = user.getQueryOptions();
        CachedPermissionData permissionData = user.getCachedData().getPermissionData(queryOptions);

        Tristate checkResult = permissionData.checkPermission("boredcommands." + literal.getLiteral());
        return checkResult.asBoolean();

    }

}
