package me.gerald.hack.util;

public class NumberUtil {
    public float value;
    public float defaultValue;

    public NumberUtil(float value) {
        this.value = defaultValue = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void increase(float count) {
        for (int i = 0; i < count; i++) {
            this.value++;
        }
    }

    public void decrease(float count) {
        for (int i = 0; i < count; i++) {
            this.value--;
        }
    }

    public void reset() {
        this.value = defaultValue;
    }

    public void zero() {
        this.value = 0;
    }
}