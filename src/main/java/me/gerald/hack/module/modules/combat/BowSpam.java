package me.gerald.hack.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.NumSetting;
import me.gerald.hack.util.NumberUtil;
import me.gerald.hack.util.TimerUtil;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class BowSpam extends Module {
    public BowSpam() {
        super("BowSpam", Category.COMBAT, "Spams arrows.");
    }

    public NumSetting delay = register(new NumSetting("Delay", 0.1f, 0, 6));

    public NumberUtil shotsFired = new NumberUtil(0);
    public TimerUtil timeFromLastShow = new TimerUtil();

    @Override
    public String getMetaData() {
        return shotsFired.getValue() != 0 ? "[" + ChatFormatting.GRAY + shotsFired.getValue() + ChatFormatting.RESET + "]" : "";
    }

    @SubscribeEvent
    public void onUseItem(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        if(mc.player.getHeldItemMainhand().getItem() instanceof ItemBow) {
            if(mc.player.isHandActive() && mc.player.getItemInUseMaxCount() >= (3 + (int) delay.getValue())) {
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
                mc.player.stopActiveHand();
                shotsFired.increase(1);
                timeFromLastShow.reset();
            }
        }
        if(timeFromLastShow.passedMs(1000)) {
            shotsFired.zero();
            timeFromLastShow.reset();
        }
    }
}
