package me.gerald.hack.module.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.StringSetting;
import me.gerald.hack.util.MessageUtil;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Chat extends Module {
    public Chat() {
        super("Chat", Category.MISC, "Change things with chat.");
    }

    public BoolSetting suffix = register(new BoolSetting("Suffix", true));
    public BoolSetting customSuffix = register(new BoolSetting("CustomSuffix", false));
    public StringSetting customSuffixString = register(new StringSetting("CustomSuffix",  "Fuck.You"));
    public BoolSetting timeStamp = register(new BoolSetting("Timestamp", true));
    public BoolSetting greenText = register(new BoolSetting("GreenText", false));
    public BoolSetting nameHighlight = register(new BoolSetting("NameHighlight", true));

    public String suffixString = " ";

    @SubscribeEvent
    public void onChat(ClientChatEvent event) {
        if(event.getOriginalMessage().startsWith("/") || event.getOriginalMessage().startsWith("!")) return;
        if(suffix.getValue()) {
            if(!customSuffix.getValue()) {
                suffixString = " \u0262\u1d07\u0280\u1d00\u029f\u1d05\u0028\u029c\u1d00\u1d04\u1d0b\u0029";
            }else {
                for (int i = 0; i < customSuffixString.getString().length(); i++) {
                    if (getUnicodeLetter(customSuffixString.getString().charAt(i)) != null) {
                        suffixString += getUnicodeLetter(customSuffixString.getString().charAt(i));
                    } else {
                        suffixString += customSuffixString.getString().charAt(i);
                    }
                }
            }
            event.setMessage(event.getOriginalMessage() + suffixString);
        }
        suffixString = " ";
        if(greenText.getValue()) event.setMessage("> " + event.getMessage());
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        String timeString = time.format(new Date());
        if(timeStamp.getValue()) event.setMessage(new TextComponentString(ChatFormatting.GRAY + "<" + ChatFormatting.GREEN + timeString + ChatFormatting.GRAY + "> " + ChatFormatting.RESET).appendSibling(event.getMessage()));
        if(nameHighlight.getValue()) {
            if(event.getMessage().getUnformattedText().contains(mc.player.getDisplayNameString())) {
                event.setMessage(event.getMessage().appendText(mc.player.getDisplayNameString()));
            }
        }
    }

    public String getUnicodeLetter(char letter) {
        if(Character.valueOf(letter).toString().equalsIgnoreCase("A")) {
            return "\u1D00";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("B")) {
            return "\u0299";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("C")) {
            return "\u1D04";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("D")) {
            return "\u1D05";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("E")) {
            return "\u1D07";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("F")) {
            return "\uA730";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("G")) {
            return "\u0262";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("H")) {
            return "\u029C";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("I")) {
            return "\u026A";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("J")) {
            return "\u1D0A";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("K")) {
            return "\u1D0B";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("L")) {
            return "\u029F";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("M")) {
            return "\u1D0D";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("N")) {
            return "\u0274";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("O")) {
            return "\u1D0F";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("P")) {
            return "\u1D18";
        }else if(Character.valueOf(letter).toString().equals("Q")) {
            return "\uFF31";
        }else if(Character.valueOf(letter).toString().equals("q")) {
            return "\uFF51";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("R")) {
            return "\u0280";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("S")) {
            return "\uA731";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("T")) {
            return "\u1D1B";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("U")) {
            return "\u1D1C";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("V")) {
            return "\u1D20";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("W")) {
            return "\u1D21";
        }else if(Character.valueOf(letter).toString().equals("X")) {
            return "\uFF38";
        }else if(Character.valueOf(letter).toString().equals("x")) {
            return "\uFF58";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("Y")) {
            return "\u028F";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("Z")) {
            return "\u1D22";
        }
        return null;
    }
}
