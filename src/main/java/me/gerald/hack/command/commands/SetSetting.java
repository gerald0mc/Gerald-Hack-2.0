package me.gerald.hack.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.GeraldHack;
import me.gerald.hack.command.Command;
import me.gerald.hack.module.Module;
import me.gerald.hack.setting.Setting;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.ModeSetting;
import me.gerald.hack.setting.settings.NumSetting;
import me.gerald.hack.setting.settings.StringSetting;
import me.gerald.hack.util.MessageUtil;

public class SetSetting extends Command {
    public SetSetting() {
        super("SetSetting", "Sets a modules setting value.", new String[]{"setsetting", "[module]", "[setting]", "[value]"});
    }

    @Override
    public void onCommand(String[] args) {
        if(args.length < 2) {
            MessageUtil.sendErrorMessage("Please specify a module.");
            return;
        }
        if(args.length < 3) {
            MessageUtil.sendErrorMessage("Please specify a setting.");
            return;
        }
        if(args.length < 4) {
            MessageUtil.sendErrorMessage("Please specify value you wish to set setting to.");
            return;
        }
        String moduleName = args[1];
        String settingName = args[2];
        String settingValue = args[3];
        for(Module module : GeraldHack.INSTANCE.moduleManager.getModules()) {
            if(module.getName().equalsIgnoreCase(moduleName)) {
                for(Setting setting : module.getSettings()) {
                    if(setting.getName().equalsIgnoreCase(settingName)) {
                        if(setting instanceof BoolSetting) {
                            if(((BoolSetting) setting).value == Boolean.parseBoolean(settingValue)) {
                                MessageUtil.sendClientMessage("Value of " + settingName + " is already " + Boolean.parseBoolean(settingValue));
                                return;
                            }
                            ((BoolSetting) setting).value = Boolean.parseBoolean(settingValue);
                            MessageUtil.sendClientMessage("Set " + ChatFormatting.GRAY + setting.getName() + ChatFormatting.RESET + " to " + ChatFormatting.GREEN + Boolean.parseBoolean(settingValue));
                        }else if(setting instanceof NumSetting) {
                            if(Float.parseFloat(settingValue) > ((NumSetting) setting).getMax() || Float.parseFloat(settingValue) < ((NumSetting) setting).getMin()) {
                                MessageUtil.sendClientMessage("Value you have put is smaller/larger than the settings min/max please try again.");
                                return;
                            }
                            if(((NumSetting) setting).getValue() == Float.parseFloat(settingValue)) {
                                MessageUtil.sendClientMessage("Value you have put is already what the setting is set to.");
                                return;
                            }
                            ((NumSetting) setting).setValue(Float.parseFloat(settingValue));
                            MessageUtil.sendClientMessage("Set " + ChatFormatting.GRAY + setting.getName() + ChatFormatting.RESET + " to " + ChatFormatting.GREEN + settingValue);
                        }else if(setting instanceof ModeSetting) {
                            if(((ModeSetting) setting).getValueIndex() == Integer.parseInt(settingValue)) {
                                MessageUtil.sendClientMessage("Value of " + settingName + " is already " + Integer.parseInt(settingValue));
                                return;
                            }
                            ((ModeSetting) setting).setValueIndex(Integer.parseInt(settingValue));
                            MessageUtil.sendClientMessage("Set " + ChatFormatting.GRAY + setting.getName() + ChatFormatting.RESET + " to " + ChatFormatting.GREEN + ((ModeSetting) setting).getValueEnum().toString());
                        }else if(setting instanceof StringSetting) {
                            if(((StringSetting) setting).getString().equalsIgnoreCase(settingValue)) {
                                MessageUtil.sendClientMessage("Value of " + settingName + " is already " + settingValue);
                                return;
                            }
                            ((StringSetting) setting).setString(settingValue);
                            MessageUtil.sendClientMessage("Set " + ChatFormatting.GRAY + setting.getName() + ChatFormatting.RESET + " to " + ChatFormatting.GREEN + settingValue);
                        }
                    }
                }
            }
        }
    }
}
