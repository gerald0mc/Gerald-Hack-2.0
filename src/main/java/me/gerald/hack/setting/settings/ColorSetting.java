package me.gerald.hack.setting.settings;

import me.gerald.hack.setting.Setting;

import java.awt.*;

public class ColorSetting extends Setting {
    private float r;
    private float g;
    private float b;
    private float a;

    public ColorSetting(String name, float r, float g, float b, float a) {
        super(name);
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

    public float getG() {
        return g;
    }

    public void setG(float g) {
        this.g = g;
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
    }

    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = a;
    }

    public Color getColor() {
        return new Color((int) getR(), (int) getG(), (int) getB(), (int) getA());
    }
}
