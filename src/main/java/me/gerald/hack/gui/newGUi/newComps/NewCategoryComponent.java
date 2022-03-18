package me.gerald.hack.gui.newGUi.newComps;

import me.gerald.hack.gui.api.AbstractContainer;
import me.gerald.hack.module.Module;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewCategoryComponent extends AbstractContainer {
    public NewCategoryComponent(Module.Category category, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Module.Category category;
    public List<NewModuleComponent> moduleComponents = new ArrayList<>();

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
