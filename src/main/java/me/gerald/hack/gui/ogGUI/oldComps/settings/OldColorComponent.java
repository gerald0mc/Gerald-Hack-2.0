package me.gerald.hack.gui.ogGUI.oldComps.settings;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.GeraldHack;
import me.gerald.hack.gui.ogGUI.OldClickGUI;
import me.gerald.hack.gui.api.SettingComponent;
import me.gerald.hack.module.Module;
import me.gerald.hack.module.modules.client.ClickGui;
import me.gerald.hack.module.modules.client.Description;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.ColorSetting;
import me.gerald.hack.setting.settings.NumSetting;
import me.gerald.hack.util.MessageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OldColorComponent extends SettingComponent {
    public boolean open = false;
    public ColorSetting setting;
    public Module module;
    public List<OldNumComponent> components;
    public List<OldBoolComponent> boolComponents;

    public OldColorComponent(Module module, ColorSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.module = module;
        this.setting = setting;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        components = new ArrayList<>();
        components.add(new OldNumComponent(module, new NumSetting("Red", setting.getR(), 0, 255), x, y, width, height));
        components.add(new OldNumComponent(module, new NumSetting("Green",  setting.getG(), 0, 255), x, y, width, height));
        components.add(new OldNumComponent(module, new NumSetting("Blue",  setting.getB(), 0, 255), x, y, width, height));
        components.add(new OldNumComponent(module, new NumSetting("Alpha",  setting.getA(), 0, 255), x, y, width, height));
        boolComponents = new ArrayList<>();
        boolComponents.add(new OldBoolComponent(module, new BoolSetting("Copy", false), x, y, width, height));
        boolComponents.add(new OldBoolComponent(module, new BoolSetting("Paste", false), x, y, width, height));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height + 1, new Color((int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getR(), (int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getG(), (int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getB()).getRGB());
        Gui.drawRect(x + 1, y, x + width - 1, y + height, isInside(mouseX, mouseY) ? new Color(30, 30, 30, 240).getRGB() : new Color(15, 15, 15, 240).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(setting.getName(), x + 5, y + 2, module.isEnabled() ? -1 : isInside(mouseX, mouseY) ? new Color(160, 160, 160).getRGB() : new Color(130, 130, 130).getRGB());
        Gui.drawRect(x + width - 12, y + 1, x + width - 2, y + height - 1, new Color((int) setting.getR(), (int) setting.getG(), (int) setting.getB(), 255).getRGB());
        if(open) {
            int yOffset = height;
            for (SettingComponent component : components) {
                component.x = x;
                component.y = y + yOffset;
                yOffset += component.getHeight();
                component.drawScreen(mouseX, mouseY, partialTicks);
            }
            for (SettingComponent component : boolComponents) {
                component.x = x;
                component.y = y + yOffset;
                yOffset += component.getHeight();
                component.drawScreen(mouseX, mouseY, partialTicks);
            }
            setting.setR(components.get(0).setting.getValue());
            setting.setG(components.get(1).setting.getValue());
            setting.setB(components.get(2).setting.getValue());
            setting.setA(components.get(3).setting.getValue());
        }
        if(isInside(mouseX, mouseY)) {
            GeraldHack.INSTANCE.moduleManager.getModule(Description.class).setName("A color setting.");
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isInside(mouseX, mouseY)) {
            if(mouseButton == 1) {
                open = !open;
            }
        }
        if(open) {
            for (SettingComponent component : components) {
                component.mouseClicked(mouseX, mouseY, mouseButton);
            }
            for (SettingComponent component : boolComponents) {
                component.mouseClicked(mouseX, mouseY, mouseButton);
            }
            if(boolComponents.get(0).setting.getValue()) {
                OldClickGUI.copiedColor = new Color((int) components.get(0).setting.getValue(), (int) components.get(1).setting.getValue(), (int) components.get(2).setting.getValue(), (int) components.get(3).setting.getValue());
                MessageUtil.sendClientMessage(ChatFormatting.GRAY + "Copied color [" + ChatFormatting.GREEN + components.get(0).setting.getValue() + ", " + components.get(1).setting.getValue() + ", " + components.get(2).setting.getValue() + ", " + components.get(3).setting.getValue() + ChatFormatting.GRAY + "]");
                boolComponents.get(0).setting.cycle();
            }
            if(boolComponents.get(1).setting.getValue()) {
                if(OldClickGUI.copiedColor == null) {
                    MessageUtil.sendErrorMessage("You have no color copied.");
                    boolComponents.get(1).setting.cycle();
                    return;
                }
                components.get(0).setting.setValue(OldClickGUI.copiedColor.getRed());
                components.get(1).setting.setValue(OldClickGUI.copiedColor.getGreen());
                components.get(2).setting.setValue(OldClickGUI.copiedColor.getBlue());
                components.get(3).setting.setValue(OldClickGUI.copiedColor.getAlpha());
                MessageUtil.sendClientMessage(ChatFormatting.GRAY + "Pasted color [" + ChatFormatting.GREEN + components.get(0).setting.getValue() + ", " + components.get(1).setting.getValue() + ", " + components.get(2).setting.getValue() + ", " + components.get(3).setting.getValue() + ChatFormatting.GRAY + "]");
                boolComponents.get(1).setting.cycle();
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if(open) {
            for (SettingComponent component : components) {
                component.mouseReleased(mouseX, mouseY, mouseButton);
            }
            for(SettingComponent component : boolComponents) {
                component.mouseReleased(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void keyTyped(char keyChar, int key) throws IOException, UnsupportedFlavorException {
        if(open) {
            for (SettingComponent component : components) {
                component.keyTyped(keyChar, key);
            }
            for(SettingComponent component : boolComponents) {
                component.keyTyped(keyChar, key);
            }
        }
    }

    @Override
    public int getHeight() {
        if(open) {
            int h = height;
            for(SettingComponent component : components) {
                h += component.getHeight();
            }
            for(SettingComponent component : boolComponents) {
                h += component.getHeight();
            }
            return h;
        }
        return height;
    }
}
