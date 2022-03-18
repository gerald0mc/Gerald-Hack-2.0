package me.gerald.hack.gui.ogGUI.oldComps;

import me.gerald.hack.GeraldHack;
import me.gerald.hack.gui.api.DragComponent;
import me.gerald.hack.module.Module;
import me.gerald.hack.module.modules.client.ClickGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.Color;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OldCategoryComponent extends DragComponent {
    public Module.Category category;
    public List<OldModuleComponent> components = new ArrayList<>();

    int yOffset = height;

    public OldCategoryComponent(Module.Category category, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        for(Module module : GeraldHack.INSTANCE.moduleManager.getModulesByCategory(category)) {
            components.add(new OldModuleComponent(module, x, y + yOffset, width, height));
            for(OldModuleComponent component : components) {
                if(component.module == module) {
                    component.parent = this;
                }
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateDragPosition(mouseX, mouseY);
        int yOffset = height;
        Gui.drawRect(x, y, x + width, y + height, new Color((int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getR(), (int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getG(), (int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getB()).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(category.name(), x + 2, y + 2, -1);
        for (OldModuleComponent component : components) {
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
        for(OldModuleComponent component : components) {
            component.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        stopDragging();
        for(OldModuleComponent component : components) {
            component.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void keyTyped(char keyChar, int key) throws IOException, UnsupportedFlavorException {
        for(OldModuleComponent component : components) {
            component.keyTyped(keyChar, key);
        }
    }

    @Override
    public int getHeight() {
        return height;
    }
}
