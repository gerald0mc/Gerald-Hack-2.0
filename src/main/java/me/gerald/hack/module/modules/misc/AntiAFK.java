package me.gerald.hack.module.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.NumSetting;
import me.gerald.hack.util.MessageUtil;
import me.gerald.hack.util.TimerUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class AntiAFK extends Module {
    public AntiAFK() {
        super("AntiAFK", Category.MISC, "Thing.");
    }

    public NumSetting timeToGoAFK = register(new NumSetting("TimeToGoAFK", 20, 1, 60));
    public NumSetting jumpTime = register(new NumSetting("JumpTimer", 10, 1, 30));

    public boolean isAFK = false;
    public TimerUtil afkTimer = new TimerUtil();
    public TimerUtil jumpTimer = new TimerUtil();

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        if(Keyboard.getEventKeyState()) {
            if(Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()) || Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()) || Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()) || Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode())) {
                if(isAFK) {
                    MessageUtil.sendClientMessage(ChatFormatting.GRAY + "Not in " + ChatFormatting.RED + "AntiAFK" + ChatFormatting.GRAY + " mode anymore!");
                    isAFK = false;
                    afkTimer.reset();
                    jumpTimer.reset();
                }else {
                    afkTimer.reset();
                }
            }
        }
        if(!Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()) && !Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()) && !Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()) && !Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode())) {
            if(isAFK) {
                if (jumpTimer.passedMs((long) (1000 * jumpTime.getValue()))) {
                    mc.player.jump();
                    jumpTimer.reset();
                }
            }else {
                if(afkTimer.passedMs((long) (1000 * timeToGoAFK.getValue()))) {
                    MessageUtil.sendClientMessage(ChatFormatting.GRAY + "Now in " + ChatFormatting.GREEN + "AntiAFK" + ChatFormatting.GRAY + " mode!");
                    isAFK = true;
                }
            }
        }
    }
}
