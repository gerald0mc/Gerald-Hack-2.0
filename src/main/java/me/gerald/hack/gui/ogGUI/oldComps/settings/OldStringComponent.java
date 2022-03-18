package me.gerald.hack.gui.ogGUI.oldComps.settings;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.GeraldHack;
import me.gerald.hack.gui.api.SettingComponent;
import me.gerald.hack.module.Module;
import me.gerald.hack.module.modules.client.ClickGui;
import me.gerald.hack.module.modules.client.Description;
import me.gerald.hack.setting.settings.StringSetting;
import me.gerald.hack.util.MessageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class OldStringComponent extends SettingComponent {
    public StringSetting setting;
    public Module module;
    public String typingString = "";
    public boolean typing = false;

    public OldStringComponent(Module module, StringSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.module = module;
        this.setting = setting;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height + 1, new Color((int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getR(), (int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getG(), (int) GeraldHack.INSTANCE.moduleManager.getModule(ClickGui.class).color.getB()).getRGB());
        Gui.drawRect(x + 1, y, x + width - 1, y + height, isInside(mouseX, mouseY) ? new Color(30, 30, 30, 240).getRGB() : new Color(15, 15, 15, 240).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(setting.getName() + " " + (typing ? ChatFormatting.WHITE + typingString : ChatFormatting.GRAY + setting.getString()), x + 5, y + 2, module.isEnabled() ? -1 : isInside(mouseX, mouseY) ? new Color(160, 160, 160).getRGB() : new Color(130, 130, 130).getRGB());
        if(isInside(mouseX, mouseY)) {
            GeraldHack.INSTANCE.moduleManager.getModule(Description.class).setName("A string setting.");
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isInside(mouseX, mouseY)) {
            if(mouseButton == 0) {
                typing = !typing;
                typingString = "";
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) { }

    @Override
    public void keyTyped(char keyChar, int key) throws UnsupportedFlavorException, IOException {
        if(typing) {
            switch (key) {
                case Keyboard.KEY_BACK:
                    typingString = removeLastLetter(typingString);
                    break;
                case Keyboard.KEY_RETURN:
                    if(typingString.length() > 0) {
                        setting.setString(typingString);
                    }
                    typing = false;
                    break;
                case Keyboard.KEY_V:
                    if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))
                        typingString += Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                    break;
                case Keyboard.KEY_C:
                    if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                        if(typingString.length() == 0) {
                            MessageUtil.sendErrorMessage("Nothing to copy.");
                            return;
                        }
                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(typingString), null);
                        MessageUtil.sendClientMessage(ChatFormatting.GRAY + "Copied text in string box to clipboard.");
                    }
                    break;
            }
            if(ChatAllowedCharacters.isAllowedCharacter(keyChar)) {
                typingString = typingString + keyChar;
            }
        }
    }

    public String removeLastLetter(String string) {
        String out = "";
        if(string != null && string.length() > 0) {
            out = string.substring(0, string.length() - 1);
        }
        return out;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
