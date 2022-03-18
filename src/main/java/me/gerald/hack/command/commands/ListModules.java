package me.gerald.hack.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.GeraldHack;
import me.gerald.hack.command.Command;
import me.gerald.hack.module.Module;
import me.gerald.hack.util.MessageUtil;

public class ListModules extends Command {
    public ListModules() {
        super("ListModules", "Lists all modules in the client.", new String[]{"modules"});
    }

    @Override
    public void onCommand(String[] args) {
        String moduleList = "    ";
        MessageUtil.sendClientMessage(ChatFormatting.GREEN + "Modules" + ChatFormatting.GRAY + "()" + ChatFormatting.RESET + "{");
        for(Module module : GeraldHack.INSTANCE.moduleManager.getModules()) {
            moduleList += module.isEnabled() ? ChatFormatting.GREEN : ChatFormatting.RED + "[" + module.getName() + "] ";
        }
        MessageUtil.sendClientMessage(moduleList);
        MessageUtil.sendClientMessage("}");
    }
}
