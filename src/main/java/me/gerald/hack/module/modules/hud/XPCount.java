package me.gerald.hack.module.modules.hud;

import me.gerald.hack.gui.ogGUI.oldComps.hud.XPComponent;
import me.gerald.hack.module.HUDModule;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.NumSetting;

public class XPCount extends HUDModule {
    public XPCount() {
        super(new XPComponent(1, 71, 1, 1), "XPCount", Category.HUD, "Counts experience bottles in inventory.");
    }

    public NumSetting x = register(new NumSetting("X", 1, 0,  mc.displayWidth));
    public NumSetting y = register(new NumSetting("Y", 1, 0,  mc.displayHeight));
    public BoolSetting stackCount = register(new BoolSetting("StackCount", true));
}
