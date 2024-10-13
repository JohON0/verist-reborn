/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.expensive.Expensive;
import im.expensive.functions.settings.Setting;
import im.expensive.functions.settings.impl.BindSetting;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.functions.settings.impl.ColorSetting;
import im.expensive.functions.settings.impl.ModeListSetting;
import im.expensive.functions.settings.impl.ModeSetting;
import im.expensive.functions.settings.impl.SliderSetting;
import im.expensive.functions.settings.impl.StringSetting;
import im.expensive.ui.styles.Style;
import im.expensive.utils.client.IMinecraft;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;

public class Config
implements IMinecraft {
    private final File file;
    private final String name;

    public Config(String name) {
        this.name = name;
        this.file = new File(new File(Minecraft.getInstance().gameDir, "\\verist\\configs"), name + ".cfg");
    }

    public void loadConfig(JsonObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        if (jsonObject.has("functions")) {
            this.loadFunctionSettings(jsonObject.getAsJsonObject("functions"));
        }
        if (jsonObject.has("styles")) {
            this.loadStyleSettings(jsonObject.getAsJsonObject("styles"));
        }
    }

    private void loadStyleSettings(JsonObject stylesObject) {
        for (Map.Entry<String, JsonElement> entry : stylesObject.entrySet()) {
            boolean isSelected;
            String styleName = entry.getKey();
            JsonObject styleObject = entry.getValue().getAsJsonObject();
            Style style = this.findStyleByName(styleName);
            if (style == null || !styleObject.has("selected") || !(isSelected = styleObject.get("selected").getAsBoolean())) continue;
            Expensive.getInstance().getStyleManager().setCurrentStyle(style);
        }
    }

    private Style findStyleByName(String styleName) {
        if (Expensive.getInstance() == null || Expensive.getInstance().getStyleManager() == null) {
            return null;
        }
        List<Style> styleList = Expensive.getInstance().getStyleManager().getStyleList();
        if (styleList == null) {
            return null;
        }
        for (Style style : styleList) {
            if (!style.getStyleName().equalsIgnoreCase(styleName)) continue;
            return style;
        }
        return null;
    }

    private void loadFunctionSettings(JsonObject functionsObject) {
        Expensive.getInstance().getFunctionRegistry().getFunctions().forEach(f -> {
            JsonObject moduleObject = functionsObject.getAsJsonObject(f.getName().toLowerCase());
            if (moduleObject == null) {
                return;
            }
            f.setState(false, true);
            this.loadSettingFromJson(moduleObject, "bind", value -> f.setBind(value.getAsInt()));
            this.loadSettingFromJson(moduleObject, "state", value -> f.setState(value.getAsBoolean(), true));
            f.getSettings().forEach(setting -> this.loadIndividualSetting(moduleObject, (Setting<?>)setting));
        });
    }

    private void loadIndividualSetting(JsonObject moduleObject, Setting<?> setting) {
        JsonElement settingElement = moduleObject.get(setting.getName());
        if (settingElement == null || settingElement.isJsonNull()) {
            return;
        }
        if (setting instanceof SliderSetting) {
            ((SliderSetting)setting).set(Float.valueOf(settingElement.getAsFloat()));
        }
        if (setting instanceof BooleanSetting) {
            ((BooleanSetting)setting).set(settingElement.getAsBoolean());
        }
        if (setting instanceof ColorSetting) {
            ((ColorSetting)setting).set(settingElement.getAsInt());
        }
        if (setting instanceof ModeSetting) {
            ((ModeSetting)setting).set(settingElement.getAsString());
        }
        if (setting instanceof BindSetting) {
            ((BindSetting)setting).set(settingElement.getAsInt());
        }
        if (setting instanceof StringSetting) {
            ((StringSetting)setting).set(settingElement.getAsString());
        }
        if (setting instanceof ModeListSetting) {
            this.loadModeListSetting((ModeListSetting)setting, moduleObject);
        }
    }

    private void loadModeListSetting(ModeListSetting setting, JsonObject moduleObject) {
        JsonObject elements = moduleObject.getAsJsonObject(setting.getName());
        ((List)setting.get()).forEach(option -> {
            JsonElement optionElement = elements.get(option.getName());
            if (optionElement != null && !optionElement.isJsonNull()) {
                option.set(optionElement.getAsBoolean());
            }
        });
    }

    private void loadSettingFromJson(JsonObject jsonObject, String key, Consumer<JsonElement> consumer) {
        JsonElement element = jsonObject.get(key);
        if (element != null && !element.isJsonNull()) {
            consumer.accept(element);
        }
    }

    public JsonElement saveConfig() {
        JsonObject functionsObject = new JsonObject();
        JsonObject stylesObject = new JsonObject();
        this.saveFunctionSettings(functionsObject);
        this.saveStyleSettings(stylesObject);
        JsonObject newObject = new JsonObject();
        newObject.add("functions", functionsObject);
        newObject.add("styles", stylesObject);
        return newObject;
    }

    private void saveFunctionSettings(JsonObject functionsObject) {
        Expensive.getInstance().getFunctionRegistry().getFunctions().forEach(module -> {
            JsonObject moduleObject = new JsonObject();
            moduleObject.addProperty("bind", module.getBind());
            moduleObject.addProperty("state", module.isState());
            module.getSettings().forEach(setting -> this.saveIndividualSetting(moduleObject, (Setting<?>)setting));
            functionsObject.add(module.getName().toLowerCase(), moduleObject);
        });
    }

    private void saveIndividualSetting(JsonObject moduleObject, Setting<?> setting) {
        if (setting instanceof BooleanSetting) {
            moduleObject.addProperty(setting.getName(), (Boolean)((BooleanSetting)setting).get());
        }
        if (setting instanceof SliderSetting) {
            moduleObject.addProperty(setting.getName(), (Number)((SliderSetting)setting).get());
        }
        if (setting instanceof ModeSetting) {
            moduleObject.addProperty(setting.getName(), (String)((ModeSetting)setting).get());
        }
        if (setting instanceof ColorSetting) {
            moduleObject.addProperty(setting.getName(), (Number)((ColorSetting)setting).get());
        }
        if (setting instanceof BindSetting) {
            moduleObject.addProperty(setting.getName(), (Number)((BindSetting)setting).get());
        }
        if (setting instanceof StringSetting) {
            moduleObject.addProperty(setting.getName(), (String)((StringSetting)setting).get());
        }
        if (setting instanceof ModeListSetting) {
            this.saveModeListSetting(moduleObject, (ModeListSetting)setting);
        }
    }

    private void saveModeListSetting(JsonObject moduleObject, ModeListSetting setting) {
        JsonObject elements = new JsonObject();
        ((List)setting.get()).forEach(option -> elements.addProperty(option.getName(), (Boolean)option.get()));
        moduleObject.add(setting.getName(), elements);
    }

    private void saveStyleSettings(JsonObject stylesObject) {
        List<Style> styleList;
        if (Expensive.getInstance() != null && Expensive.getInstance().getStyleManager() != null && (styleList = Expensive.getInstance().getStyleManager().getStyleList()) != null) {
            for (Style style : styleList) {
                JsonObject styleObject = new JsonObject();
                styleObject.addProperty("selected", Expensive.getInstance().getStyleManager().getCurrentStyle() == style);
                stylesObject.add(style.getStyleName(), styleObject);
            }
        }
    }

    public File getFile() {
        return this.file;
    }

    public String getName() {
        return this.name;
    }
}

