package me.gerald.hack.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.GeraldHack;
import me.gerald.hack.command.Command;
import me.gerald.hack.module.modules.client.ClickGui;
import me.gerald.hack.module.modules.client.Client;
import me.gerald.hack.util.MessageUtil;
import org.lwjgl.input.Keyboard;

public class Help extends Command {
    public Help() {
        super("Help", "Help command for the client.", new String[]{"help"});
    }

    @Override
    public void onCommand(String[] args) {
        MessageUtil.sendClientMessage(ChatFormatting.GREEN + GeraldHack.MOD_NAME + ChatFormatting.GRAY + "()" + ChatFormatting.RESET + " {");
        MessageUtil.sendClientMessage("    Creator: " + ChatFormatting.GREEN + "Guess?");
        MessageUtil.sendClientMessage("    Version: " + ChatFormatting.GREEN + GeraldHack.VERSION);
        MessageUtil.sendClientMessage("    Command Prefix: " + ChatFormatting.GREEN + GeraldHack.INSTANCE.moduleManager.getModule(Client.class).prefix.getString());
        MessageUtil.sendClientMessage("    OldClickGUI Bind: " + ChatFormatting.GREEN + Keyboard.getKeyName(GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).getKeybind()));
        MessageUtil.sendClientMessage("    " + ChatFormatting.GREEN + "Commands" + ChatFormatting.GRAY + "()" + ChatFormatting.RESET + " {");
        for(Command command : GeraldHack.INSTANCE.commandManager.getCommands()) {
            String delimiter = " ";
            MessageUtil.sendClientMessage("        " + ChatFormatting.GREEN + command.getName() + ChatFormatting.RESET + ": " + command.getDescription() + " Usage: " + String.join(delimiter, command.getUsage()));
        }
        MessageUtil.sendClientMessage("    }");
        MessageUtil.sendClientMessage("}");
    }
}
