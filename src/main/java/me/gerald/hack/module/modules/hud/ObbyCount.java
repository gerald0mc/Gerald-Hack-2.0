package me.gerald.hack.module.modules.hud;

import me.gerald.hack.gui.ogGUI.oldComps.hud.ObbyComponent;
import me.gerald.hack.module.HUDModule;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.NumSetting;

public class ObbyCount extends HUDModule {
    public ObbyCount() {
        super(new ObbyComponent(1, 51, 1, 1), "ObbyCount", Category.HUD, "Counts how much obsidian you have.");
    }

    public NumSetting x = register(new NumSetting("X", 1, 0,  mc.displayWidth));
    public NumSetting y = register(new NumSetting("Y", 1, 0,  mc.displayHeight));
    public BoolSetting stackCount = register(new BoolSetting("StackCount", true));
}
