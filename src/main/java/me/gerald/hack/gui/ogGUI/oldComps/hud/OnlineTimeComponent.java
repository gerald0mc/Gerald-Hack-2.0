package me.gerald.hack.gui.ogGUI.oldComps.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.GeraldHack;
import me.gerald.hack.gui.api.HUDComponent;
import me.gerald.hack.module.modules.client.ClickGui;
import me.gerald.hack.module.modules.hud.OnlineTime;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class OnlineTimeComponent extends HUDComponent {
    public OnlineTimeComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateDragPosition(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
        int timeSeconds = (int) (GeraldHack.INSTANCE.time / 20);
        int timeMinutes = timeSeconds / 60;
        int timeHours = timeMinutes / 60;
        timeSeconds -= timeMinutes * 60;
        String onlineTime = ChatFormatting.GRAY.toString() + timeHours + ChatFormatting.RESET + " Hours, " + ChatFormatting.GRAY + timeMinutes + ChatFormatting.RESET + " Minutes, " + ChatFormatting.GRAY + timeSeconds + ChatFormatting.RESET + " Seconds.";
        this.width = 75;
        this.height = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
        if(Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null) {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(onlineTime, x, y, new Color((int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getR(), (int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getG(), (int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getB()).getRGB());
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isInside(mouseX, mouseY)) {
            if(mouseButton == 0)
                beginDragging(mouseX, mouseY);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        GeraldHack.INSTANCE.moduleManager.getModule(OnlineTime.class).x.setValue(x);
        GeraldHack.INSTANCE.moduleManager.getModule(OnlineTime.class).y.setValue(y);
        stopDragging();
    }

    @Override
    public void keyTyped(char keyChar, int key) { }

    @Override
    public int getHeight() {
        return height;
    }
}
