package boredbrownbear.boredcommands.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import boredbrownbear.boredcommands.helper.Permission;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class CommandEnderChest {


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("enderchest");
        literal.requires((source) -> {
            return Permission.hasperm(source, literal);
        }).executes(context -> {
            return execute(context);
        });

        dispatcher.register(literal);
        LiteralArgumentBuilder<ServerCommandSource> literal1 = CommandManager.literal("ec");
        literal1.requires((source) -> {
            return Permission.hasperm(source, literal);
        }).executes(context -> {
            return execute(context);
        });

        dispatcher.register(literal1);


    }

    public static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        player.openHandledScreen(new NamedScreenHandlerFactory() {
            @Override
            public Text getDisplayName() {
                return new TranslatableText("container.enderchest", new Object[0]);
            }

            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                return GenericContainerScreenHandler.createGeneric9x3(syncId, inv, player.getEnderChestInventory());
            }
        });
        return Command.SINGLE_SUCCESS;
    }
}