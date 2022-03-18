package me.gerald.hack.module.modules.client;

import me.gerald.hack.module.Module;

public class HUDEditor extends Module {
    public HUDEditor() {
        super("HUDEditor", Category.CLIENT, "HUDEditor for the client.");
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new me.gerald.hack.gui.HUDEditor());
        toggle();
    }
}
