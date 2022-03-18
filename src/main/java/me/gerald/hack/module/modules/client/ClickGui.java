package me.gerald.hack.module.modules.client;

import me.gerald.hack.GeraldHack;
import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.ColorSetting;
import me.gerald.hack.setting.settings.ModeSetting;

public class ClickGui extends Module {
    public ClickGui() {
        super("ClickGui", Category.CLIENT, "Renders client clickGUI.");
    }

    public ModeSetting guiMode = register(new ModeSetting("GuiMode", GuiMode.Old));
    public enum GuiMode {New, Old}
    public ColorSetting color = register(new ColorSetting("Color", 0, 125, 255, 0));
    public BoolSetting thinSliders = register(new BoolSetting("ThinSliders", true));
    public ModeSetting descriptionMode = register(new ModeSetting("DescriptionMode", DescriptionMode.Category));
    public enum DescriptionMode {Category, Text}

    @Override
    public void onEnable() {
        if(guiMode.getValueEnum() == GuiMode.Old)
            mc.displayGuiScreen(GeraldHack.INSTANCE.gui);
        toggle();
    }
}
