package me.gerald.hack.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.GeraldHack;
import me.gerald.hack.command.Command;
import me.gerald.hack.util.MessageUtil;

import java.io.IOException;

public class Config extends Command {
    public Config() {
        super("Config", "Idek", new String[]{"config", "[save/load]", "[configName]"});
    }

    @Override
    public void onCommand(String[] args) throws IOException {
        if(args.length < 2) {
            MessageUtil.sendErrorMessage("Please specify save or load.");
            return;
        }
        if(args.length < 3) {
            MessageUtil.sendErrorMessage("Please specify config name.");
            return;
        }
        String saveOrLoad = args[1];
        String configName = args[2];
        if(saveOrLoad.equalsIgnoreCase("save")) {
            GeraldHack.INSTANCE.configManager.registerNewConfig(configName);
            MessageUtil.sendClientMessage("Saved new config called " + ChatFormatting.AQUA + configName);
        }else if(saveOrLoad.equalsIgnoreCase("load")) {
            GeraldHack.INSTANCE.configManager.loadConfig(configName);
            MessageUtil.sendClientMessage("Loaded config called " + ChatFormatting.AQUA + configName);
        }
    }
}
