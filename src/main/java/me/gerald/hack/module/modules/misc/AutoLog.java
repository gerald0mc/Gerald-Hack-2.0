package me.gerald.hack.module.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.NumSetting;
import me.gerald.hack.util.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Objects;

public class AutoLog extends Module {
    public AutoLog() {
        super("AutoLog", Category.MISC, "Automatically logs for you.");
    }

    public NumSetting healthToLog = register(new NumSetting("HealthToLog", 10, 0, 36));
    public BoolSetting totemLog = register(new BoolSetting("TotemLog", true));
    public NumSetting totemsToLog = register(new NumSetting("TotemsToLog", 1, 0, 10));
    public BoolSetting weakness = register(new BoolSetting("Weakness", true));

    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        if(mc.player.isCreative()) return;
        int totalTotems = InventoryUtil.getTotalAmountOfItem(Items.TOTEM_OF_UNDYING);
        if(mc.player.getHealth() + mc.player.getAbsorptionAmount() <= healthToLog.getValue() && !totemLog.getValue() || totemLog.getValue() && totalTotems <= totemsToLog.getValue() && mc.player.getHealth() + mc.player.getAbsorptionAmount() <= healthToLog.getValue()) {
            Objects.requireNonNull(mc.getConnection()).handleDisconnect(new SPacketDisconnect(new TextComponentString("Logged at " + ChatFormatting.GRAY + (mc.player.getHealth() + mc.player.getAbsorptionAmount()) + ChatFormatting.RESET + " with " + ChatFormatting.GRAY + totalTotems + ChatFormatting.RESET + " Totems")));
            toggle();
        }
        if(mc.player.isPotionActive(MobEffects.WEAKNESS) && weakness.getValue()) {
            Objects.requireNonNull(mc.getConnection()).handleDisconnect(new SPacketDisconnect(new TextComponentString("Logged because you got " + ChatFormatting.GRAY + "WEAKNESSED" + ChatFormatting.RESET + " ew!")));
            toggle();
        }
    }
}
