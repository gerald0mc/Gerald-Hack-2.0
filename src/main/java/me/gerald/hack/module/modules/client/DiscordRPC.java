package me.gerald.hack.module.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.module.Module;
import me.gerald.hack.util.DiscordUtil;
import me.gerald.hack.util.MessageUtil;

public class DiscordRPC extends Module {
    public DiscordRPC() {
        super("DiscordRPC", Category.CLIENT, "Client RPC.");
        setNeedsKeybind(false);
    }

    @Override
    public void onEnable() {
        MessageUtil.sendClientMessage(ChatFormatting.GRAY + "Starting DiscordRPC...");
        DiscordUtil.start();
    }

    @Override
    public void onDisable() {
        MessageUtil.sendClientMessage(ChatFormatting.GRAY + "Stopping DiscordRPC...");
        DiscordUtil.stop();
    }
}
