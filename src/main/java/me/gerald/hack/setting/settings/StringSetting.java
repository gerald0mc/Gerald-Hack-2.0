package me.gerald.hack.setting.settings;

import me.gerald.hack.setting.Setting;

public class StringSetting extends Setting {
    public String string;

    public StringSetting(String name, String string) {
        super(name);
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
