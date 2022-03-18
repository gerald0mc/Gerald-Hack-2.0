package me.gerald.hack.module;

import me.gerald.hack.module.modules.client.*;
import me.gerald.hack.module.modules.combat.*;
import me.gerald.hack.module.modules.hud.*;
import me.gerald.hack.module.modules.misc.*;
import me.gerald.hack.module.modules.movement.*;
import me.gerald.hack.module.modules.player.*;
import me.gerald.hack.module.modules.render.*;
import me.gerald.hack.module.modules.world.*;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public List<Module> modules = new ArrayList<>();
    public List<Module> sortedModules = new ArrayList<>();

    public ModuleManager() {
        modules.add(new Description());
        //client
        modules.add(new ClickGui());
        modules.add(new Client());
        modules.add(new DiscordRPC());
        modules.add(new HUDEditor());
        modules.add(new Messages());
        //combat
        modules.add(new AutoCrystal());
        modules.add(new AutoTotem());
        modules.add(new AutoWeb());
        modules.add(new BowSpam());
        modules.add(new Criticals());
        modules.add(new CrystalAura());
        modules.add(new HoleFill());
        modules.add(new KillAura());
        modules.add(new XPFast());
        //hud
        modules.add(new me.gerald.hack.module.modules.hud.ArrayList());
        modules.add(new CrystalCount());
        modules.add(new CrystalSecond());
        modules.add(new GappleCount());
        modules.add(new Name());
        modules.add(new ObbyCount());
        modules.add(new OnlineFriends());
        modules.add(new OnlineTime());
        modules.add(new PotionEffects());
        modules.add(new TextRadar());
        modules.add(new TotemCount());
        modules.add(new Watermark());
        modules.add(new XPCount());
        //misc
        modules.add(new AntiAFK());
        modules.add(new AntiNarrator());
        modules.add(new AutoKit());
        modules.add(new AutoLog());
        modules.add(new AutoPorn());
        modules.add(new Chat());
        modules.add(new FakePlayer());
        modules.add(new FancyText());
        modules.add(new MCF());
        modules.add(new Spammer());
        //movement
        modules.add(new AutoSex());
        modules.add(new HoleTP());
        modules.add(new ReverseStep());
        modules.add(new Sprint());
        modules.add(new Step());
        modules.add(new Velocity());
        //player
        modules.add(new AutoTool());
        modules.add(new PacketMine());
        modules.add(new Pearl());
        modules.add(new Respawn());
        modules.add(new Suicide());
        modules.add(new WeaknessNotify());
        //render
        modules.add(new ChildESP());
        modules.add(new ESP());
        modules.add(new ExplosionChams());
        modules.add(new FullBright());
        modules.add(new HoleESP());
        modules.add(new Nametags());
        modules.add(new PopChams());
        modules.add(new Search());
        modules.add(new StorageESP());
        modules.add(new VoidESP());
        //world
        modules.add(new AntiRegear());
        modules.add(new ShulkerLogger());
        modules.add(new TimeChanger());
        modules.add(new TotemPopCounter());

        modules.sort(this::sortABC);
        sortedModules.addAll(modules);
    }

    public List<Module> getModules() {
        return modules;
    }

    public List<Module> getSortedModules() {
        return sortedModules;
    }

    public <T extends Module> T getModule(Class<T> clazz) {
        for(Module module : getModules()) {
            if(module.getClass() == clazz) {
                return (T) module;
            }
        }
        return null;
    }

    public Module getModuleByName(String name) {
        for(Module module : getModules()) {
            if(module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }

    public List<Module> getModulesByCategory(Module.Category category) {
        List<Module> modules = new ArrayList<>();
        for(Module module : getModules()) {
            if(module.getCategory() == category) {
                modules.add(module);
            }
        }
        return modules;
    }

    private int sortABC(Module hack1, Module hack2) {
        return hack1.getName().compareTo(hack2.getName());
    }
}
