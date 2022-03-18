package me.gerald.hack.setting.settings;

import me.gerald.hack.setting.Setting;

public class ModeSetting extends Setting {
    private final Enum<?>[] constants;
    private int modeIndex;
    private final int defaultValueIndex;

    public ModeSetting(String name, Enum<?> defaultValue) {
        super(name);
        this.constants = defaultValue.getDeclaringClass().getEnumConstants();
        this.defaultValueIndex = this.modeIndex = indexOf(defaultValue);
    }

    public void increase() {
        if (modeIndex == constants.length - 1) {
            modeIndex = 0;
        } else {
            modeIndex++;
        }
    }

    public void decrease() {
        if (modeIndex == 0) {
            modeIndex = constants.length - 1;
        } else {
            modeIndex--;
        }
    }

    public Enum<?> getValueEnum() {
        return constants[modeIndex];
    }

    public int getValueIndex() {
        return modeIndex;
    }

    public void setValueIndex(int value) {
        modeIndex = value;
    }

    public int getDefaultValueIndex() {
        return defaultValueIndex;
    }

    private int indexOf(Enum<?> value) {
        for (int i = 0; i < constants.length; i++) {
            if (constants[i] == value)
                return i;
        }

        return -1;
    }
}
