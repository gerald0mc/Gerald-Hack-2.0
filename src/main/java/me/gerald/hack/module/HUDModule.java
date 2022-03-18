package me.gerald.hack.module;

import me.gerald.hack.gui.api.HUDComponent;

public class HUDModule extends Module{
    public final HUDComponent component;

    public HUDModule(HUDComponent component, String name, Category category, String description) {
        super(name, category, description);
        this.component = component;
    }

    public HUDComponent getComponent() {
        return component;
    }
}
