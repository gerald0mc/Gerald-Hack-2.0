package me.gerald.hack.gui.ogGUI;

import me.gerald.hack.GeraldHack;
import me.gerald.hack.gui.api.AbstractContainer;
import me.gerald.hack.gui.ogGUI.oldComps.OldCategoryComponent;
import me.gerald.hack.gui.ogGUI.oldComps.OldModuleComponent;
import me.gerald.hack.module.Module;
import me.gerald.hack.module.modules.client.ClickGui;
import me.gerald.hack.module.modules.client.Description;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OldClickGUI extends GuiScreen {
    public static List<OldCategoryComponent> components = new ArrayList<>();
    public static Color copiedColor;

    public OldClickGUI() {
        int offset = 25;
        components.add(new OldCategoryComponent(Module.Category.DESCRIPTION, 25, 25, 125, 11));
        for(Module.Category category : Module.Category.values()) {
            if(category == Module.Category.HUD || category == Module.Category.DESCRIPTION) return;
            components.add(new OldCategoryComponent(category, offset, 50, 125, 11));
            offset += 130;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        for(AbstractContainer component : components) {
            component.drawScreen(mouseX, mouseY, partialTicks);
        }
        for(OldCategoryComponent component : components) {
            if(component.category != Module.Category.DESCRIPTION) {
                for (OldModuleComponent moduleComponent : component.components) {
                    final Module module = moduleComponent.module;
                    if (moduleComponent.isInside(mouseX, mouseY)) {
                        GeraldHack.INSTANCE.moduleManager.getModule(Description.class).setName(module.getDescription());
                        if (GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).descriptionMode.getValueEnum() == ClickGui.DescriptionMode.Text)
                            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(module.getDescription(), mouseX + 2, mouseY - 4, -1);
                    }
                }
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for(AbstractContainer component : components) {
            component.mouseClicked(mouseX, mouseY, mouseButton);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for(AbstractContainer component : components) {
            component.mouseReleased(mouseX, mouseY, state);
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        for(AbstractContainer component : components) {
            try {
                component.keyTyped(typedChar, keyCode);
            } catch (UnsupportedFlavorException ignored) { }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
