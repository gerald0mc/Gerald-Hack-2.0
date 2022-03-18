package me.gerald.hack.setting.settings;

import me.gerald.hack.setting.Setting;

public class BoolSetting extends Setting {
    public boolean value;

    public BoolSetting(String name, boolean value) {
        super(name);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public void cycle() {
        value = !value;
    }
}
