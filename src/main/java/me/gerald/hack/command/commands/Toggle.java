package me.gerald.hack.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.GeraldHack;
import me.gerald.hack.command.Command;
import me.gerald.hack.module.Module;
import me.gerald.hack.util.MessageUtil;

public class Toggle extends Command {
    public Toggle() {
        super("Toggle", "Toggles a module.", new String[]{"toggle", "[module]"});
    }

    @Override
    public void onCommand(String[] args) {
        if(args.length < 2) {
            MessageUtil.sendErrorMessage("Please specify a module to toggle.");
            return;
        }
        String moduleName = args[1];
        for(Module module : GeraldHack.INSTANCE.moduleManager.getModules()) {
            if(module.getName().equalsIgnoreCase(moduleName)) {
                module.toggle();
                MessageUtil.sendClientMessage("Toggled " + (module.isEnabled() ? ChatFormatting.GREEN : ChatFormatting.RED) + module.getName());
            }
        }
    }
}
