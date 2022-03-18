package me.gerald.hack.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.command.Command;
import me.gerald.hack.util.MessageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Objects;

public class Ping extends Command {
    public Ping() {
        super("Ping", "Says a players ping in chat.", new String[]{"ping", "[name]"});
    }

    @Override
    public void onCommand(String[] args) {
        if(args.length < 2) {
            MessageUtil.sendErrorMessage("Please specify player you wish to see.");
            return;
        }
        String playerName = args[1];
        for(EntityPlayer entity : Minecraft.getMinecraft().world.playerEntities) {
            if(entity.getDisplayNameString().equalsIgnoreCase(playerName)) {
                MessageUtil.sendClientMessage("Ping for " + ChatFormatting.AQUA + playerName + ChatFormatting.RESET + ": " + Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfo(entity.getUniqueID()).getResponseTime());
                return;
            }
        }
    }
}
