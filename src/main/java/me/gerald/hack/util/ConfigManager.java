package me.gerald.hack.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import me.gerald.hack.GeraldHack;
import me.gerald.hack.module.Module;
import me.gerald.hack.setting.Setting;
import me.gerald.hack.setting.settings.*;
import me.gerald.hack.util.friend.Friend;
import me.gerald.hack.util.friend.FriendManager;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class ConfigManager {
    public void save() throws IOException {
        registerFolders();
        for(Module module : GeraldHack.INSTANCE.moduleManager.getModules()) {
            if(module.getDescription().equalsIgnoreCase("Description will appear here.")) continue;
            if (Files.exists(Paths.get("Gerald(Hack)/Modules/" + module.getName() + ".json")))
                new File("Gerald(Hack)/Modules/" + module.getName() + ".json").delete();
            Files.createFile(Paths.get("Gerald(Hack)/Modules/" + module.getName() + ".json"));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            OutputStreamWriter stream = new OutputStreamWriter(new FileOutputStream("Gerald(Hack)/Modules" + "/" + module.getName() + ".json"), StandardCharsets.UTF_8);
            JsonObject moduleObject = new JsonObject();
            JsonObject settingObject = new JsonObject();
            moduleObject.add("Module", new JsonPrimitive(module.getName()));
            for(Setting setting : module.getSettings()) {
                if(setting instanceof BoolSetting) {
                    settingObject.add(setting.getName(), new JsonPrimitive(((BoolSetting) setting).getValue()));
                }else if(setting instanceof NumSetting) {
                    settingObject.add(setting.getName(), new JsonPrimitive(((NumSetting) setting).getValue()));
                }else if(setting instanceof ModeSetting) {
                    settingObject.add(setting.getName(), new JsonPrimitive(((ModeSetting) setting).getValueIndex()));
                }else if(setting instanceof ColorSetting) {
                    JsonObject colorObject = new JsonObject();
                    colorObject.add("Red", new JsonPrimitive(((ColorSetting) setting).getR()));
                    colorObject.add("Green", new JsonPrimitive(((ColorSetting) setting).getG()));
                    colorObject.add("Blue", new JsonPrimitive(((ColorSetting) setting).getB()));
                    colorObject.add("Alpha", new JsonPrimitive(((ColorSetting) setting).getA()));
                    settingObject.add(setting.getName(), colorObject);
                }else if(setting instanceof StringSetting) {
                    settingObject.add(setting.getName(), new JsonPrimitive(((StringSetting) setting).getString()));
                }
            }
            moduleObject.add("Bind", new JsonPrimitive(Keyboard.getKeyName(module.getKeybind())));
            moduleObject.add("Enabled", new JsonPrimitive(module.isEnabled()));
            moduleObject.add("Visible", new JsonPrimitive(module.isVisible));
            moduleObject.add("Settings", settingObject);
            stream.write(gson.toJson(new JsonParser().parse(moduleObject.toString())));
            stream.close();
        }
        //save friends
        if (Files.exists(Paths.get("Gerald(Hack)/Client/Friends.json")));
            new File("Gerald(Hack)/Client/Friends.json").delete();
        Files.createFile(Paths.get("Gerald(Hack)/Client/Friends.json"));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter stream = new OutputStreamWriter(new FileOutputStream("Gerald(Hack)/Client/Friends.json"), StandardCharsets.UTF_8);
        stream.write(gson.toJson(GeraldHack.INSTANCE.friendManager.getFriends()));
        stream.close();
    }

    public void load() throws IOException {
        for(Module module : GeraldHack.INSTANCE.moduleManager.getModules()) {
            if(module.getDescription().equalsIgnoreCase("DESCRIPTION")) continue;
            System.out.println("Attempting to load config for. (" + module.getName() + ")");
            if (!Files.exists(Paths.get("Gerald(Hack)/Modules/" + module.getName() + ".json")))
                return;
            InputStream stream = Files.newInputStream(Paths.get("Gerald(Hack)/Modules" + "/" + module.getName() + ".json"));
            JsonObject moduleObject = new JsonParser().parse(new InputStreamReader(stream)).getAsJsonObject();
            if(moduleObject.get("Module") == null) return;
            //load settings
            JsonObject settingObject = moduleObject.get("Settings").getAsJsonObject();
            for(Setting setting : module.getSettings()) {
                JsonElement settingDataObject = settingObject.get(setting.getName());
                try {
                    if(settingDataObject != null && (settingDataObject.isJsonPrimitive() || settingDataObject.isJsonObject())) {
                        if(setting instanceof BoolSetting) {
                            ((BoolSetting) setting).setValue(settingDataObject.getAsBoolean());
                        }else if(setting instanceof NumSetting) {
                            ((NumSetting) setting).setValue(settingDataObject.getAsFloat());
                        }else if(setting instanceof ModeSetting) {
                            ((ModeSetting) setting).setValueIndex(settingDataObject.getAsInt());
                        }else if(setting instanceof ColorSetting) {
                            JsonObject colorObject = settingObject.get(setting.getName()).getAsJsonObject();
                            JsonElement redElement = colorObject.get("Red");
                            JsonElement greenElement = colorObject.get("Green");
                            JsonElement blueElement = colorObject.get("Blue");
                            JsonElement alphaElement = colorObject.get("Alpha");
                            try {
                                ((ColorSetting) setting).setR(redElement.getAsInt());
                                ((ColorSetting) setting).setG(greenElement.getAsInt());
                                ((ColorSetting) setting).setB(blueElement.getAsInt());
                                ((ColorSetting) setting).setA(alphaElement.getAsInt());
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else if(setting instanceof StringSetting) {
                            ((StringSetting) setting).setString(settingDataObject.getAsString());
                        }
                    }
                }catch (Exception e) {
                    System.out.println("Faulty setting found (" + module.getName() + ", " + setting.getName() + ")");
                    e.printStackTrace();
                }
            }
            //load bind
            JsonElement bindElement = moduleObject.get("Bind");
            String bindS = bindElement.getAsString();
            System.out.println(bindS);
            try {
                module.setKeybind(Keyboard.getKeyIndex(bindS));
            }catch (Exception e) {
                e.printStackTrace();
            }
            //load toggled
            JsonElement enabledElement = moduleObject.get("Enabled");
            String enabledS = enabledElement.getAsString();
            if(enabledS.contains("true")) {
                try {
                    module.toggle();
                    System.out.println("Toggled (" + module.getName() + ")");
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //load visibility
            JsonElement visibleElement = moduleObject.get("Visible");
            String visibleS = visibleElement.getAsString();
            if(visibleS.contains("false")) {
                try {
                    module.setVisible(false);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Loaded config for module. (" + module.getName() + ")");
            stream.close();
        }
        InputStream stream = Files.newInputStream(Paths.get("Gerald(Hack)/Client/Friends.json"));
        JsonArray friendElement = new JsonParser().parse(new InputStreamReader(stream)).getAsJsonArray();
        for(JsonElement object : friendElement) {
            JsonObject obj = object.getAsJsonObject();
            GeraldHack.INSTANCE.friendManager.addFriend(obj.get("name").getAsString());
        }
        stream.close();
    }

    public void registerFolders() throws IOException {
        if (!Files.exists(Paths.get("Gerald(Hack)/"))) Files.createDirectories(Paths.get("Gerald(Hack)/"));
        if (!Files.exists(Paths.get("Gerald(Hack)/Modules/"))) Files.createDirectories(Paths.get("Gerald(Hack)/Modules/"));
        if (!Files.exists(Paths.get("Gerald(Hack)/Client/"))) Files.createDirectories(Paths.get("Gerald(Hack)/Client/"));
    }

    public void registerNewConfig(String configName) throws IOException {
        if (!Files.exists(Paths.get("Gerald(Hack)/Config/"))) Files.createDirectories(Paths.get("Gerald(Hack)/Config/"));
        for(Module module : GeraldHack.INSTANCE.moduleManager.getModules()) {
            if(module.getDescription().equalsIgnoreCase("DESCRIPTION")) continue;
            if (Files.exists(Paths.get("Gerald(Hack)/Config/" + configName + "/" + module.getName() + ".json")))
                new File("Gerald(Hack)/Config/" + configName + "/" + module.getName() + ".json").delete();
            Files.createFile(Paths.get("Gerald(Hack)/Config/" + configName + "/" + module.getName() + ".json"));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            OutputStreamWriter stream = new OutputStreamWriter(new FileOutputStream("Gerald(Hack)/Config/" + configName + "/" + module.getName() + ".json"), StandardCharsets.UTF_8);
            JsonObject moduleObject = new JsonObject();
            JsonObject settingObject = new JsonObject();
            moduleObject.add("Module", new JsonPrimitive(module.getName()));
            for(Setting setting : module.getSettings()) {
                if(setting instanceof BoolSetting) {
                    settingObject.add(setting.getName(), new JsonPrimitive(((BoolSetting) setting).getValue()));
                }else if(setting instanceof NumSetting) {
                    settingObject.add(setting.getName(), new JsonPrimitive(((NumSetting) setting).getValue()));
                }else if(setting instanceof ModeSetting) {
                    settingObject.add(setting.getName(), new JsonPrimitive(((ModeSetting) setting).getValueIndex()));
                }else if(setting instanceof ColorSetting) {
                    JsonObject colorObject = new JsonObject();
                    colorObject.add("Red", new JsonPrimitive(((ColorSetting) setting).getR()));
                    colorObject.add("Green", new JsonPrimitive(((ColorSetting) setting).getG()));
                    colorObject.add("Blue", new JsonPrimitive(((ColorSetting) setting).getB()));
                    colorObject.add("Alpha", new JsonPrimitive(((ColorSetting) setting).getA()));
                    settingObject.add(setting.getName(), colorObject);
                }else if(setting instanceof StringSetting) {
                    settingObject.add(setting.getName(), new JsonPrimitive(((StringSetting) setting).getString()));
                }
            }
            moduleObject.add("Bind", new JsonPrimitive(Keyboard.getKeyName(module.getKeybind())));
            moduleObject.add("Enabled", new JsonPrimitive(module.isEnabled()));
            moduleObject.add("Visible", new JsonPrimitive(module.isVisible));
            moduleObject.add("Settings", settingObject);
            stream.write(gson.toJson(new JsonParser().parse(moduleObject.toString())));
            stream.close();
        }
    }

    public void loadConfig(String configName) throws IOException {
        for(Module module : GeraldHack.INSTANCE.moduleManager.getModules()) {
            if (module.getDescription().equalsIgnoreCase("DESCRIPTION")) continue;
            if (!Files.exists(Paths.get("Gerald(Hack)/Config/" + configName + "/" + module.getName() + ".json")))
                return;
            InputStream stream = Files.newInputStream(Paths.get("Gerald(Hack)/Config/" + configName + "/" + module.getName() + ".json"));
            JsonObject moduleObject = new JsonParser().parse(new InputStreamReader(stream)).getAsJsonObject();
            if (moduleObject.get("Module") == null) return;
            //load settings
            JsonObject settingObject = moduleObject.get("Settings").getAsJsonObject();
            for (Setting setting : module.getSettings()) {
                JsonElement settingDataObject = settingObject.get(setting.getName());
                try {
                    if (settingDataObject != null && (settingDataObject.isJsonPrimitive() || settingDataObject.isJsonObject())) {
                        if (setting instanceof BoolSetting) {
                            ((BoolSetting) setting).setValue(settingDataObject.getAsBoolean());
                        } else if (setting instanceof NumSetting) {
                            ((NumSetting) setting).setValue(settingDataObject.getAsFloat());
                        } else if (setting instanceof ModeSetting) {
                            ((ModeSetting) setting).setValueIndex(settingDataObject.getAsInt());
                        } else if (setting instanceof ColorSetting) {
                            JsonObject colorObject = settingObject.get(setting.getName()).getAsJsonObject();
                            JsonElement redElement = colorObject.get("Red");
                            JsonElement greenElement = colorObject.get("Green");
                            JsonElement blueElement = colorObject.get("Blue");
                            JsonElement alphaElement = colorObject.get("Alpha");
                            try {
                                ((ColorSetting) setting).setR(redElement.getAsInt());
                                ((ColorSetting) setting).setG(greenElement.getAsInt());
                                ((ColorSetting) setting).setB(blueElement.getAsInt());
                                ((ColorSetting) setting).setA(alphaElement.getAsInt());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (setting instanceof StringSetting) {
                            ((StringSetting) setting).setString(settingDataObject.getAsString());
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Faulty setting found (" + module.getName() + ", " + setting.getName() + ")");
                    e.printStackTrace();
                }
            }
            //load bind
            JsonElement bindElement = moduleObject.get("Bind");
            String bindS = bindElement.getAsString();
            System.out.println(bindS);
            try {
                module.setKeybind(Keyboard.getKeyIndex(bindS));
            } catch (Exception e) {
                e.printStackTrace();
            }
            //load toggled
            JsonElement enabledElement = moduleObject.get("Enabled");
            String enabledS = enabledElement.getAsString();
            if (enabledS.contains("true")) {
                try {
                    module.toggle();
                    System.out.println("Toggled (" + module.getName() + ")");
                } catch (Exception ignored) {}
            }else if(enabledS.contains("false") && module.isEnabled()) {
                try {
                    module.toggle();
                }catch (Exception ignored) {}
            }
            //load visibility
            JsonElement visibleElement = moduleObject.get("Visible");
            String visibleS = visibleElement.getAsString();
            if (visibleS.contains("false")) {
                try {
                    module.setVisible(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(visibleS.contains("true") && module.isVisible) {
                try {
                    module.setVisible(true);
                }catch (Exception ignored) {}
            }
            System.out.println("Loaded config for module. (" + module.getName() + ")");
            stream.close();
        }
    }
}
