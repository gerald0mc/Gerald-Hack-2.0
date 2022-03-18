package me.gerald.hack;

import me.gerald.hack.command.CommandManager;
import me.gerald.hack.event.EventListener;
import me.gerald.hack.event.listeners.TotemPopListener;
import me.gerald.hack.gui.ogGUI.OldClickGUI;
import me.gerald.hack.gui.HUD;
import me.gerald.hack.module.ModuleManager;
import me.gerald.hack.util.ConfigManager;
import me.gerald.hack.util.ShutdownHook;
import me.gerald.hack.util.friend.FriendManager;
import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Mod(
        modid = GeraldHack.MOD_ID,
        name = GeraldHack.MOD_NAME,
        version = GeraldHack.VERSION
)
public class GeraldHack {

    public static final String MOD_ID = "geraldhack";
    public static final String MOD_NAME = "Gerald(Hack)";
    public static final String VERSION = "1.69.4";

    @Mod.Instance(MOD_ID)
    public static GeraldHack INSTANCE;

    public ModuleManager moduleManager;
    public CommandManager commandManager;
    public ConfigManager configManager;
    public FriendManager friendManager;
    public EventListener eventListener;
    public TotemPopListener totemPopListener;
    public HUD hud;
    public OldClickGUI gui;

    public List<Block> searchBlocks = new ArrayList<>();
    public long time;

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event) throws IOException {
        MinecraftForge.EVENT_BUS.register(this);
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        configManager = new ConfigManager();
        friendManager = new FriendManager();
        eventListener = new EventListener();
        totemPopListener = new TotemPopListener();
        hud = new HUD();
        gui = new OldClickGUI();
        configManager.load();
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
    }
}
