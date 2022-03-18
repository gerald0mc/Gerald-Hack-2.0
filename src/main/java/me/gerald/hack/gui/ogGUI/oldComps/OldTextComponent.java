package me.gerald.hack.gui.ogGUI.oldComps;

import me.gerald.hack.GeraldHack;
import me.gerald.hack.gui.api.AbstractContainer;
import me.gerald.hack.gui.api.DragComponent;
import me.gerald.hack.module.modules.client.ClickGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OldTextComponent extends DragComponent {
    public static List<AbstractContainer> components = new ArrayList<>();
    public String text;

    public OldTextComponent(String text, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.text = text;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateDragPosition(mouseX, mouseY);
        int yOffset = height;
        Gui.drawRect(x, y, x + width, y + height, new Color((int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getR(), (int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getG(), (int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getB()).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, x + 2, y + 2, -1);
        for (AbstractContainer component : components) {
            component.x = this.x;
            component.y = this.y + yOffset;
            yOffset += component.getHeight();
            component.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isInside(mouseX, mouseY)) {
            if(mouseButton == 0) {
                beginDragging(mouseX, mouseY);
            }
        }
        for(AbstractContainer component : components) {
            component.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        stopDragging();
        for(AbstractContainer component : components) {
            component.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void keyTyped(char keyChar, int key) throws IOException, UnsupportedFlavorException {
        for(AbstractContainer component : components) {
            component.keyTyped(keyChar, key);
        }
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setText(String text){
        this.text = text;
    }

    public void addComponent(AbstractContainer component) {
        components.add(component);
    }
}
