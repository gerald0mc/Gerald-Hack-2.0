package me.gerald.hack.module.modules.hud;

import me.gerald.hack.gui.ogGUI.oldComps.hud.CrystalComponent;
import me.gerald.hack.module.HUDModule;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.NumSetting;

public class CrystalCount extends HUDModule {
    public CrystalCount() {
        super(new CrystalComponent(1, 31, 1, 1), "CrystalCount", Category.HUD, "Renders how many crystals you have.");
    }

    public NumSetting x = register(new NumSetting("X", 1, 0,  mc.displayWidth));
    public NumSetting y = register(new NumSetting("Y", 1, 0,  mc.displayHeight));
    public BoolSetting stackCount = register(new BoolSetting("StackCount", true));
}
