package me.gerald.hack.event;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.GeraldHack;
import me.gerald.hack.command.Command;
import me.gerald.hack.event.events.PacketEvent;
import me.gerald.hack.gui.ogGUI.OldClickGUI;
import me.gerald.hack.module.Module;
import me.gerald.hack.module.modules.client.Client;
import me.gerald.hack.module.modules.client.Description;
import me.gerald.hack.util.ProjectionUtil;
import me.gerald.hack.util.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.*;

public class EventListener {
    public EventListener() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public int brokenCrystals = 0;
    public List<Entity> confirmedHitList = new ArrayList<>();
    public TimerUtil timer = new TimerUtil();

    @SubscribeEvent
    public void onRenderLast(RenderWorldLastEvent event) {
        ProjectionUtil.updateMatrix();
    }

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            int key = Keyboard.getEventKey();
            for(Module module : GeraldHack.INSTANCE.moduleManager.getModules()) {
                if(module.getKeybind() == key) {
                    System.out.println("Attempting to toggle a module called " + ChatFormatting.GREEN + module.getName());
                    module.toggle();
                }
            }
        }
    }

    @SubscribeEvent
    public void onChatSend(ClientChatEvent event) throws IOException {
        String[] args = event.getMessage().split(" ");
        if(event.getMessage().startsWith(GeraldHack.INSTANCE.moduleManager.getModule(Client.class).prefix.getString())) {
            event.setCanceled(true);
            for(Command command : GeraldHack.INSTANCE.commandManager.getCommands()) {
                if(args[0].equalsIgnoreCase(GeraldHack.INSTANCE.moduleManager.getModule(Client.class).prefix.getString() + command.getName())) {
                    command.onCommand(args);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketUseEntity) {
            final CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();
            if (packet.getEntityFromWorld(Minecraft.getMinecraft().world) instanceof EntityEnderCrystal && !this.confirmedHitList.contains(packet.getEntityFromWorld(Minecraft.getMinecraft().world))) {
                this.confirmedHitList.add(packet.getEntityFromWorld(Minecraft.getMinecraft().world));
            }
        }
    }

    @SubscribeEvent
    public void onBreak(PacketEvent.Receive event) {
        //busted atm so im gonna use the skidded one
        if(event.getPacket() instanceof SPacketDestroyEntities) {
            SPacketDestroyEntities packet = (SPacketDestroyEntities) event.getPacket();
            for(Entity entity : Minecraft.getMinecraft().world.loadedEntityList) {
                if(entity instanceof EntityEnderCrystal) {
                    for (int array : packet.getEntityIDs()) {
                        if (array == entity.getEntityId()) {
                            if(confirmedHitList.contains(entity)) {
                                brokenCrystals++;
                                confirmedHitList.remove(entity);
                            }
                        }
                    }
                }
            }
        }
//        if (event.getPacket() instanceof SPacketSoundEffect) {
//            final SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
//            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
//                for (Entity crystal : this.confirmedHitList) {
//                    final EntityEnderCrystal enderCrystal = (EntityEnderCrystal) crystal;
//                    if (enderCrystal != null && enderCrystal.getPosition().equals(new BlockPos(packet.getX(), packet.getY(), packet.getZ()))) {
//                        confirmedHitList.remove(crystal);
//                        brokenCrystals++;
//                    }
//                }
//            }
//        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null) {
            GeraldHack.INSTANCE.time += 1;
            GeraldHack.INSTANCE.moduleManager.sortedModules.sort(Comparator.comparing(module -> -Minecraft.getMinecraft().fontRenderer.getStringWidth(module.getName() + " " + module.getMetaData())));
            if(Minecraft.getMinecraft().currentScreen instanceof OldClickGUI) {
                if(Minecraft.getMinecraft().fontRenderer.getStringWidth(GeraldHack.INSTANCE.moduleManager.getModule(Description.class).getName()) > 125) {
                    GeraldHack.INSTANCE.moduleManager.getModule(Description.class).getParent().parent.width = Minecraft.getMinecraft().fontRenderer.getStringWidth(GeraldHack.INSTANCE.moduleManager.getModule(Description.class).getName()) + 3;
                    GeraldHack.INSTANCE.moduleManager.getModule(Description.class).getParent().width = Minecraft.getMinecraft().fontRenderer.getStringWidth(GeraldHack.INSTANCE.moduleManager.getModule(Description.class).getName()) + 3;
                }else {
                    GeraldHack.INSTANCE.moduleManager.getModule(Description.class).getParent().parent.width = 125;
                    GeraldHack.INSTANCE.moduleManager.getModule(Description.class).getParent().width = 125;
                }
            }
            if(timer.passedMs(1000)) {
                brokenCrystals = 0;
                timer.reset();
            }
        }
    }

    @SubscribeEvent
    public void onServerDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        if(GeraldHack.INSTANCE.time != 0)
            GeraldHack.INSTANCE.time = 0;
    }
}
