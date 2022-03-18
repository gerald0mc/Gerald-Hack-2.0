package me.gerald.hack.module.modules.misc;

import me.gerald.hack.module.Module;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FancyText extends Module {
    public FancyText() {
        super("FancyText", Category.MISC, "Allows you to type fancy.");
    }

    @SubscribeEvent
    public void onChat(ClientChatEvent event) {
        String message = "";
        for(int i = 0; i < event.getOriginalMessage().length(); i++) {
            if(getUnicodeLetter(event.getOriginalMessage().charAt(i)) != null) {
                message += getUnicodeLetter(event.getOriginalMessage().charAt(i));
            }
        }
        event.setMessage(message);
    }

    public String getUnicodeLetter(char letter) {
        if(Character.valueOf(letter).toString().equalsIgnoreCase("A")) {
            return "\uD83C\uDDE6\u200C";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("B")) {
            return "\uD83C\uDDE7";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("C")) {
            return "\u200C\uD83C\uDDE8";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("D")) {
            return "\u200C\u200C\uD83C\uDDE9\u200C";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("E")) {
            return "\u200C\u200C\uD83C\uDDEA";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("F")) {
            return "\u200C\u200C\u200C\uD83C\uDDEB";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("G")) {
            return "\uD83C\uDDEC";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("H")) {
            return "\u200C\u200C\u200C\u200C\uD83C\uDDED";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("I")) {
            return "\u200C\u200C\u200C\u200C\u200C\uD83C\uDDEE";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("J")) {
            return "\u200C\uD83C\uDDEF\u200C";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("K")) {
            return "\u200C\u200C\uD83C\uDDF0";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("L")) {
            return "\u200C\uD83C\uDDF1\u200C";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("M")) {
            return "\uD83C\uDDF2";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("N")) {
            return "\uD83C\uDDF3\u200C";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("O")) {
            return "\uD83C\uDDF4\u200C";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("P")) {
            return "\u200C\u200C\uD83C\uDDF5\u200C";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("Q")) {
            return "\uD83C\uDDF6";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("R")) {
            return "\u200C\uD83C\uDDF7\u200C";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("S")) {
            return "\uD83C\uDDF8\u200C";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("T")) {
            return "\uD83C\uDDF9\u200C";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("U")) {
            return "\uD83C\uDDFA";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("V")) {
            return "\u200C\uD83C\uDDFB";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("W")) {
            return "\u200C\uD83C\uDDFC\u200C";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("X")) {
            return "\uD83C\uDDFD\u200C";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("Y")) {
            return "\uD83C\uDDFE\u200C";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase("Z")) {
            return "\uD83C\uDDFF\u200C";
        }else if(Character.valueOf(letter).toString().equalsIgnoreCase(" ")) {
            return " ";
        }
        return null;
    }
}
