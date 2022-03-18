package me.gerald.hack.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.GeraldHack;
import me.gerald.hack.command.Command;
import me.gerald.hack.module.Module;
import me.gerald.hack.util.MessageUtil;
import org.lwjgl.input.Keyboard;

public class Bind extends Command {
    public Bind() {
        super("Bind", "Binds a module to a key.", new String[]{"bind", "[module]", "[bind]"});
    }

    @Override
    public void onCommand(String[] args) {
        if(args.length < 2) {
            MessageUtil.sendErrorMessage("Please specify a module.");
            return;
        }
        if(args.length < 3) {
            MessageUtil.sendErrorMessage("Please specify a bind.");
            return;
        }
        String moduleName = args[1];
        String key = args[2];
        for(Module module : GeraldHack.INSTANCE.moduleManager.getModules()) {
            if(module.getName().equalsIgnoreCase(moduleName)) {
                try {
                    module.setKeybind(Keyboard.getKeyIndex(key.toUpperCase()));
                    MessageUtil.sendClientMessage("Bound " + ChatFormatting.GRAY + module.getName() + ChatFormatting.RESET + " to " + ChatFormatting.GRAY + "[" + ChatFormatting.GREEN + Keyboard.getKeyName(Keyboard.getKeyIndex(key.toUpperCase())) + ChatFormatting.GRAY + "]");
                }catch (Exception e) {
                    e.printStackTrace();
                    MessageUtil.sendErrorMessage("Failed to bind " + module.getName() + " to " + ChatFormatting.GRAY + "[" + ChatFormatting.GREEN + Keyboard.getKeyName(Keyboard.getKeyIndex(key.toUpperCase())) + ChatFormatting.GRAY + "]" + ChatFormatting.RESET + " please make sure everything is correct.");
                }
            }
        }
    }
}
