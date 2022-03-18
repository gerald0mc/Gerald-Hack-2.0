package me.gerald.hack.module.modules.misc;

import me.gerald.hack.GeraldHack;
import me.gerald.hack.module.Module;
import me.gerald.hack.util.MessageUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;

public class MCF extends Module {
    public MCF() {
        super("MCF", Category.MISC, "Middle click friend.");
    }

    @SubscribeEvent
    public void onInput(InputEvent.MouseInputEvent event) {
        if(Mouse.isButtonDown(2)) {
            if (mc.objectMouseOver.typeOfHit.equals(RayTraceResult.Type.ENTITY) && mc.objectMouseOver.entityHit instanceof EntityPlayer) {
                if(GeraldHack.INSTANCE.friendManager.isFriend(((EntityPlayer) mc.objectMouseOver.entityHit).getDisplayNameString())) {
                    GeraldHack.INSTANCE.friendManager.delFriend(((EntityPlayer) mc.objectMouseOver.entityHit).getDisplayNameString());
                    MessageUtil.sendClientMessage("Removed " + ((EntityPlayer) mc.objectMouseOver.entityHit).getDisplayNameString() + " from friends list.");
                }else {
                    GeraldHack.INSTANCE.friendManager.addFriend(((EntityPlayer) mc.objectMouseOver.entityHit).getDisplayNameString());
                    MessageUtil.sendClientMessage("Added " + ((EntityPlayer) mc.objectMouseOver.entityHit).getDisplayNameString() + " to friends list.");
                }
            }
        }
    }
}
