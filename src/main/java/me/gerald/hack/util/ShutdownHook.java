package me.gerald.hack.util;

import me.gerald.hack.GeraldHack;

import java.io.IOException;

public class ShutdownHook extends Thread {
    @Override
    public void run() {
        try {
            GeraldHack.INSTANCE.configManager.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

