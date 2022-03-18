package me.gerald.hack.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.GeraldHack;
import me.gerald.hack.command.Command;
import me.gerald.hack.module.Module;
import me.gerald.hack.setting.Setting;
import me.gerald.hack.setting.settings.*;
import me.gerald.hack.util.MessageUtil;

public class ListSettings extends Command {
    public ListSettings() {
        super("ListSettings", "Lists all settings for a module.", new String[]{"settings", "[module]"});
    }

    @Override
    public void onCommand(String[] args) {
        if(args.length != 2) {
            MessageUtil.sendErrorMessage("Please specify what module you wish to see settings for.");
            return;
        }
        String moduleName = args[1];
        for(Module module : GeraldHack.INSTANCE.moduleManager.getModules()) {
            if(module.getName().equalsIgnoreCase(moduleName)) {
                MessageUtil.sendClientMessage(ChatFormatting.GREEN + module.getName() + ChatFormatting.GRAY + "()" + ChatFormatting.RESET + " {");
                for(Setting setting : module.getSettings()) {
                    if(setting instanceof BoolSetting)
                        MessageUtil.sendClientMessage("    " + ChatFormatting.GREEN + setting.getName() + ChatFormatting.GRAY + "(" + ((BoolSetting) setting).getValue() + ")");
                    else if(setting instanceof NumSetting)
                        MessageUtil.sendClientMessage("    " + ChatFormatting.GREEN + setting.getName() + ChatFormatting.GRAY + "(" + ((NumSetting) setting).getValue() + ")");
                    else if(setting instanceof StringSetting)
                        MessageUtil.sendClientMessage("    " + ChatFormatting.GREEN + setting.getName() + ChatFormatting.GRAY + "(" + ((StringSetting) setting).getString() + ")");
                    else if(setting instanceof ModeSetting)
                        MessageUtil.sendClientMessage("    " + ChatFormatting.GREEN + setting.getName() + ChatFormatting.GRAY + "(" + ((ModeSetting) setting).getValueEnum().toString() + ")");
                    else if(setting instanceof ColorSetting) {
                        int thing = rgbaToHex((int) ((ColorSetting) setting).getR(), (int) ((ColorSetting) setting).getG(),(int) ((ColorSetting) setting).getB(), (int) ((ColorSetting) setting).getA());
                        MessageUtil.sendClientMessage("    " + ChatFormatting.GREEN + setting.getName() + ChatFormatting.GRAY + "(" + thing + ((ColorSetting) setting).getR() + ChatFormatting.GRAY + ", " + thing + ((ColorSetting) setting).getG() + ChatFormatting.GRAY + ", " + thing + ((ColorSetting) setting).getB() + ChatFormatting.GRAY + ", " + thing + ((ColorSetting) setting).getA() + ChatFormatting.GRAY + ")");
                    }
                }
                MessageUtil.sendClientMessage("}");
            }
        }
    }

    public int rgbaToHex(int red, int green, int blue, int alpha) {
        int rgb = (alpha << 24);
        rgb |= (red << 16);
        rgb |= (green << 8);
        rgb |= (blue);

        return rgb;
    }
}
