package me.gerald.hack.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.GeraldHack;
import me.gerald.hack.command.Command;
import me.gerald.hack.module.modules.client.Client;
import me.gerald.hack.util.MessageUtil;
import org.lwjgl.input.Keyboard;

public class Prefix extends Command {
    public Prefix() {
        super("Prefix", "Set the prefix.", new String[]{"prefix", "[key]"});
    }

    @Override
    public void onCommand(String[] args) {
        if(args.length < 2) {
            MessageUtil.sendErrorMessage("Please input a bind.");
            return;
        }
        String key = args[1];
        GeraldHack.INSTANCE.moduleManager.getModule(Client.class).prefix.setString(key.toUpperCase());
        MessageUtil.sendClientMessage("Set prefix to " + ChatFormatting.GRAY + "[" + ChatFormatting.GREEN + key.toUpperCase() + ChatFormatting.GRAY + "]");
    }
}
