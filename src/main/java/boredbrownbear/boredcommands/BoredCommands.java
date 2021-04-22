package boredbrownbear.boredcommands;

import boredbrownbear.boredcommands.commands.*;
import boredbrownbear.boredcommands.helper.Config;
import boredbrownbear.boredcommands.helper.HomePoint;
import boredbrownbear.boredcommands.helper.WarpPoint;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BoredCommands implements ModInitializer {

    public static final String MODID = "boredcommands";
    private static File worlddir;
    public static Logger LOGGER = LogManager.getLogger("[BoredCommands]");
    public static File configdir = FabricLoader.getInstance().getConfigDir().toFile();
    public static File boredcommandsdir = new File(configdir, "boredcommands");
    public static Config config;
    public static HashMap<String, Boolean> perms = new HashMap<>();
    public static final String WALK_SPEED_MODIFIER = "BORED_WALK_SPEED_MODIFIER";
    public static final String FLY_SPEED_MODIFIER = "BORED_FLY_SPEED_MODIFIER";

    @Override
    public void onInitialize() {
        if (!boredcommandsdir.exists()) makedir(boredcommandsdir);

        config = new Config("boredcommands/config", this.loadConfig());

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
            if (server.isDedicated()) {
                worlddir = new File(server.getRunDirectory(), server.getName());
            } else {

                worlddir = server.getIconFile().getParentFile();
            }
        });

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            WarpPoint.loadAll();
            HomePoint.loadAll();
        });

    }

    public static File getWorldDir() {
        return worlddir;
    }

    public void makeFile(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void makedir(File file) {
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public Map<String, Object> loadConfig() {
        Map<String, Object> configOptions = new HashMap<>();
        configOptions.put("back", false);
        configOptions.put("day", true);
        configOptions.put("delhome", false);
        configOptions.put("delwarp", true);
        configOptions.put("enderchest", true);
        configOptions.put("fly", true);
        configOptions.put("flyspeed", true);
        //configOptions.put("gm", true);
        configOptions.put("god", true);
        configOptions.put("home", false);
        configOptions.put("heal", false);
        configOptions.put("night", true);
        configOptions.put("rain", true);
        configOptions.put("repair", true);
        configOptions.put("rndtp", false);
        configOptions.put("sethome", false);
        configOptions.put("setspawn", true);
        configOptions.put("setwarp", true);
        configOptions.put("spawn", false);
        configOptions.put("suicide", false);
        configOptions.put("sun", true);
        configOptions.put("tpa", false);
        configOptions.put("tpaccept", false);
        configOptions.put("tpdeny", false);
        configOptions.put("walkspeed", true);
        configOptions.put("warp", false);

        return configOptions;
    }

}
