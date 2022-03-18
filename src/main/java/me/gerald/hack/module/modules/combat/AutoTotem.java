package me.gerald.hack.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.event.events.TotemPopEvent;
import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.ModeSetting;
import me.gerald.hack.setting.settings.NumSetting;
import me.gerald.hack.util.InventoryUtil;
import me.gerald.hack.util.MessageUtil;
import me.gerald.hack.util.TimerUtil;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoTotem extends Module {
    public AutoTotem() {
        super("AutoTotem", Category.COMBAT, "Puts a totem in your offhand.");
    }

    public NumSetting delay = register(new NumSetting("Delay", 0, 0, 250));
    public BoolSetting message = register(new BoolSetting("Message", true));

    public boolean needsTotem;
    public TimerUtil timer = new TimerUtil();

    @Override
    public String getMetaData() {
        return "[" + ChatFormatting.GRAY + InventoryUtil.getTotalAmountOfItem(Items.TOTEM_OF_UNDYING) + ChatFormatting.RESET + "]";
    }

    @SubscribeEvent
    public void onPop(TotemPopEvent event) {
        if(event.getEntity() == mc.player) {
            needsTotem = true;
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        if(mc.player.isCreative()) return;
        int slot = InventoryUtil.getItemInventory(Items.TOTEM_OF_UNDYING, false);
        if(mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING && !needsTotem) {
            needsTotem = true;
            timer.reset();
        }
        if(needsTotem) {
            if(timer.passedMs((long) delay.getValue())) {
                if(slot != -1) {
                    InventoryUtil.moveItemToSlot(slot, 45);
                    if(message.getValue())
                        MessageUtil.sendClientMessage(ChatFormatting.GRAY + "Moved " + ChatFormatting.GREEN + "Totem " + ChatFormatting.GRAY + "to offhand.");
                    needsTotem = false;
                    timer.reset();
                }
            }
        }
    }
}
