package boredbrownbear.boredcommands;

import boredbrownbear.boredcommands.commands.*;
import boredbrownbear.boredcommands.helper.HomePoint;
import boredbrownbear.boredcommands.helper.WarpPoint;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.HashMap;

public class BoredCommands implements ModInitializer {

    public static final String MODID = "boredcommands";
    private static File worlddir;
    public static Logger LOGGER = LogManager.getLogger("[BoredCommands]");
    public static HashMap<String, Boolean> perms = new HashMap<>();
    public static final String WALK_SPEED_MODIFIER = "BORED_WALK_SPEED_MODIFIER";
    public static LuckPerms luckperms;

    @Override
    public void onInitialize() {
        initWorlds();

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> CommandBack.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> CommandDay.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> CommandDelHome.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> CommandDelWarp.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> CommandEnderChest.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> CommandFly.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> CommandFlySpeed.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> CommandGod.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> CommandHeal.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> CommandHome.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> CommandNight.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> CommandRain.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> CommandRepair.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> CommandRndTp.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> CommandSetHome.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> CommandSetSpawn.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> CommandSetWarp.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> CommandSpawn.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> CommandSuicide.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> CommandSun.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> CommandTpa.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> CommandTpAccept.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> CommandTpDeny.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> CommandWalkSpeed.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> CommandWarp.register(dispatcher));
    }

    private static void initWorlds() {

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            luckperms = LuckPermsProvider.get();

            if (server.isDedicated()) {
                worlddir = new File(server.getRunDirectory(), server.getName());
            } else {
                worlddir = server.getIconFile().getParentFile();
            }

            WarpPoint.loadAll();
            HomePoint.loadAll();
        });

    }

    public static File getWorldDir() {
        return worlddir;
    }

}
