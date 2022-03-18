package me.gerald.hack.module.modules.hud;

import me.gerald.hack.GeraldHack;
import me.gerald.hack.gui.ogGUI.oldComps.hud.WatermarkComponent;
import me.gerald.hack.module.HUDModule;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.NumSetting;
import me.gerald.hack.setting.settings.StringSetting;

public class Watermark extends HUDModule {
    public Watermark() {
        super(new WatermarkComponent(1, 11, 1, 1), "Watermark", Category.HUD, "Renders the watermark.");
    }

    public NumSetting x = register(new NumSetting("X", 1, 0,  mc.displayWidth));
    public NumSetting y = register(new NumSetting("Y", 1, 0,  mc.displayHeight));
    public StringSetting clientName = register(new StringSetting("ClientName", GeraldHack.MOD_NAME));
    public BoolSetting version = register(new BoolSetting("Version", true));
    public StringSetting versionString = register(new StringSetting("Version", GeraldHack.VERSION));
}
