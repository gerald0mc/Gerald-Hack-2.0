package me.gerald.hack.gui.ogGUI.oldComps;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.GeraldHack;
import me.gerald.hack.gui.api.AbstractContainer;
import me.gerald.hack.gui.api.SettingComponent;
import me.gerald.hack.gui.ogGUI.oldComps.settings.*;
import me.gerald.hack.module.Module;
import me.gerald.hack.module.modules.client.ClickGui;
import me.gerald.hack.setting.Setting;
import me.gerald.hack.setting.settings.*;
import me.gerald.hack.util.MessageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OldModuleComponent extends AbstractContainer {
    public Module module;
    public boolean open;
    public OldCategoryComponent parent;
    public List<SettingComponent> settingComponents;

    public OldModuleComponent(Module module, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.module = module;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        settingComponents = new ArrayList<>();
        module.setParent(this);
        if(module.needsKeybind())
            settingComponents.add(new OldBindComponent(module, x, y, width, height));
        for (Setting setting : module.getSettings()) {
            if (setting instanceof BoolSetting) {
                settingComponents.add(new OldBoolComponent(module, (BoolSetting) setting, x, y, width, height));
            } else if (setting instanceof NumSetting) {
                settingComponents.add(new OldNumComponent(module, (NumSetting) setting, x, y, width, height));
            } else if (setting instanceof ColorSetting) {
                settingComponents.add(new OldColorComponent(module, (ColorSetting) setting, x, y, width, height));
            } else if (setting instanceof ModeSetting) {
                settingComponents.add(new OldModeComponent(module, (ModeSetting) setting, x, y, width, height));
            } else if (setting instanceof StringSetting) {
                settingComponents.add(new OldStringComponent(module, (StringSetting) setting, x, y, width, height));
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int yOffset = height;
        Gui.drawRect(x, y, x + width, y + height + 1, new Color((int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getR(), (int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getG(), (int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getB()).getRGB());
        Gui.drawRect(x + 1, y, x + width - 1, y + height, module.isEnabled() ? new Color((int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getR(), (int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getG(), (int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getB(), (int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getA()).getRGB() : (isInside(mouseX, mouseY) ? new Color(30, 30, 30, 240).getRGB() : new Color(15, 15, 15, 240).getRGB()));
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(module.getName(), x + 2, y + 2, module.isEnabled() ? -1 : isInside(mouseX, mouseY) ? new Color(160, 160, 160).getRGB() : new Color(130, 130, 130).getRGB());
        if(module.getCategory() != Module.Category.DESCRIPTION) {
            if(open && module.hasSetting() || open && module.needsKeybind()) {
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("-", x + width - Minecraft.getMinecraft().fontRenderer.getStringWidth("-") - 1, y + 2, module.isEnabled() ? -1 : new Color(130, 130, 130).getRGB());
            }else if(!open && module.hasSetting() || !open && module.needsKeybind()) {
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("+", x + width - Minecraft.getMinecraft().fontRenderer.getStringWidth("-") - 1, y + 2, module.isEnabled() ? -1 : new Color(130, 130, 130).getRGB());
            }
        }
        if(open) {
            for (SettingComponent component : settingComponents) {
                component.x = x;
                component.y = y + yOffset;
                yOffset += component.getHeight();
                component.drawScreen(mouseX, mouseY, partialTicks);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(open) {
            for(SettingComponent component : settingComponents) {
                component.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
        if(isInside(mouseX, mouseY)) {
            if(mouseButton == 0) {
                module.toggle();
            }else if(mouseButton == 1) {
                open = !open;
            }else if(mouseButton == 2) {
                module.isVisible = !module.isVisible;
                MessageUtil.sendClientMessage(ChatFormatting.GRAY + module.getName() + "'s " + ChatFormatting.RESET + " visibility has been set to " + (module.isVisible ? ChatFormatting.GREEN + "TRUE" : ChatFormatting.RED + "FALSE"));
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if(open) {
            for(SettingComponent component : settingComponents) {
                component.mouseReleased(mouseX, mouseY, mouseButton);
            }
        }
//        if(mouseButton == 0)
//            GeraldHack.INSTANCE.configManager.saveModule(module);
    }

    @Override
    public void keyTyped(char keyChar, int key) throws IOException, UnsupportedFlavorException {
        if(open) {
            for(SettingComponent component : settingComponents) {
                component.keyTyped(keyChar, key);
            }
        }
    }

    @Override
    public int getHeight() {
        if(open) {
            int h = height;
            for(SettingComponent component : settingComponents) {
                h += component.getHeight();
            }
            return h;
        }
        return height;
    }
}
