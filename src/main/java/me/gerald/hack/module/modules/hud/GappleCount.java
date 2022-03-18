package me.gerald.hack.module.modules.hud;

import me.gerald.hack.gui.ogGUI.oldComps.hud.GappleComponent;
import me.gerald.hack.module.HUDModule;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.NumSetting;

public class GappleCount extends HUDModule {
    public GappleCount() {
        super(new GappleComponent(1, 41, 1, 1), "GappleCount", Category.HUD, "Counts how many gapples you have.");
    }

    public NumSetting x = register(new NumSetting("X", 1, 0,  mc.displayWidth));
    public NumSetting y = register(new NumSetting("Y", 1, 0,  mc.displayHeight));
    public BoolSetting stackCount = register(new BoolSetting("StackCount", true));
}
