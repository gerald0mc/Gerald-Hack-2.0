package me.gerald.hack.module;

import me.gerald.hack.event.events.ModuleToggleEvent;
import me.gerald.hack.gui.ogGUI.oldComps.OldModuleComponent;
import me.gerald.hack.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class Module {
    private String name;
    private Category category;
    private String description;
    private int keybind;
    private boolean isEnabled = false;
    public boolean isVisible = true;
    private boolean needsKeybind = true;
    private OldModuleComponent parent;

    public static Minecraft mc = Minecraft.getMinecraft();
    private List<Setting> settings = new ArrayList<>();

    public Module(String name, Category category, String description) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.keybind = getName().equalsIgnoreCase("clickgui") ? Keyboard.KEY_U : Keyboard.KEY_NONE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getKeybind() {
        return keybind;
    }

    public void setKeybind(int value) {
        this.keybind = value;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void toggle() {
        isEnabled = !isEnabled;
        if(isEnabled)
            enable();
        else
            disable();
    }

    public void onEnable() {}

    public void enable() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.post(new ModuleToggleEvent.Pre(this));
        onEnable();
    }

    public void onDisable() {}

    public void disable() {
        MinecraftForge.EVENT_BUS.unregister(this);
        MinecraftForge.EVENT_BUS.post(new ModuleToggleEvent.Post(this));
        onDisable();
    }

    public <T extends Setting> T register(T setting) {
        settings.add(setting);
        return setting;
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public boolean hasSetting() {
        return !getSettings().isEmpty();
    }

    public boolean needsKeybind() {
        return needsKeybind;
    }

    public void setNeedsKeybind(boolean value) {
        needsKeybind = value;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public String getMetaData() {
        return "";
    }

    public void setParent(OldModuleComponent parent) {
        this.parent = parent;
    }

    public OldModuleComponent getParent() {
        return parent;
    }

    public boolean nullCheck() {
        return mc == null || mc.player == null || mc.world == null;
    }

    public enum Category {COMBAT, MOVEMENT, PLAYER, RENDER, WORLD, MISC, CLIENT, HUD, DESCRIPTION}
}
