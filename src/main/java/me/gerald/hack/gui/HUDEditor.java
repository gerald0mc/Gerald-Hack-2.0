package me.gerald.hack.gui;

import me.gerald.hack.GeraldHack;
import me.gerald.hack.gui.api.AbstractContainer;
import me.gerald.hack.gui.ogGUI.oldComps.OldCategoryComponent;
import me.gerald.hack.module.HUDModule;
import me.gerald.hack.module.Module;
import net.minecraft.client.gui.GuiScreen;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HUDEditor extends GuiScreen {
    public List<AbstractContainer> components = new ArrayList<>();

    public HUDEditor() {
        components.add(new OldCategoryComponent(Module.Category.HUD, 25, 50, 125, 11));
        for(Module module : GeraldHack.INSTANCE.moduleManager.getModulesByCategory(Module.Category.HUD)) {
            HUDModule hudModule = (HUDModule) module;
            components.add(hudModule.getComponent());
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        for(AbstractContainer component : components) {
            component.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for(AbstractContainer component : components) {
            component.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        for(AbstractContainer component : components) {
            component.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        for(AbstractContainer component : components) {
            try {
                component.keyTyped(typedChar, keyCode);
            } catch (UnsupportedFlavorException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
