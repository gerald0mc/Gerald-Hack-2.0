package me.gerald.hack.gui.newGUi.newComps;

import me.gerald.hack.gui.api.AbstractContainer;
import me.gerald.hack.module.Module;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class NewModuleComponent extends AbstractContainer {
    public NewModuleComponent(Module module, int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void keyTyped(char keyChar, int key) throws IOException, UnsupportedFlavorException {

    }

    @Override
    public int getHeight() {
        return 0;
    }
}
