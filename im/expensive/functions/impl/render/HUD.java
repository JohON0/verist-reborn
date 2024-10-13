/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import com.google.common.eventbus.Subscribe;
import im.expensive.Expensive;
import im.expensive.events.EventDisplay;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.impl.render.Themes;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.functions.settings.impl.ModeListSetting;
import im.expensive.ui.display.impl.ArmorRenderer;
import im.expensive.ui.display.impl.ArrayListRenderer;
import im.expensive.ui.display.impl.CoordsRenderer;
import im.expensive.ui.display.impl.InventoryRenderer;
import im.expensive.ui.display.impl.KeyBindRenderer;
import im.expensive.ui.display.impl.KeyStrocesRenderer;
import im.expensive.ui.display.impl.PotionRenderer;
import im.expensive.ui.display.impl.SchedulesRenderer;
import im.expensive.ui.display.impl.StaffListRenderer;
import im.expensive.ui.display.impl.TargetInfoRenderer;
import im.expensive.ui.display.impl.WatermarkRenderer;
import im.expensive.ui.styles.StyleManager;
import im.expensive.utils.drag.DragManager;
import im.expensive.utils.drag.Dragging;
import im.expensive.utils.render.ColorUtils;

@FunctionRegister(name="HUD", type=Category.Render)
public class HUD
extends Function {
    private StyleManager styleManager;
    private final ModeListSetting elements = new ModeListSetting("\u042d\u043b\u0435\u043c\u0435\u043d\u0442\u044b", new BooleanSetting("\u0412\u0430\u0442\u0435\u0440\u043c\u0430\u0440\u043a\u0430", true), new BooleanSetting("\u0421\u043f\u0438\u0441\u043e\u043a \u043c\u043e\u0434\u0443\u043b\u0435\u0439", true), new BooleanSetting("\u041a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b", true), new BooleanSetting("\u042d\u0444\u0444\u0435\u043a\u0442\u044b", true), new BooleanSetting("\u0421\u043f\u0438\u0441\u043e\u043a \u043c\u043e\u0434\u0435\u0440\u0430\u0446\u0438\u0438", true), new BooleanSetting("\u0410\u043a\u0442\u0438\u0432\u043d\u044b\u0435 \u0431\u0438\u043d\u0434\u044b", true), new BooleanSetting("\u0410\u043a\u0442\u0438\u0432\u043d\u044b\u0439 \u0442\u0430\u0440\u0433\u0435\u0442", true), new BooleanSetting("\u0411\u0440\u043e\u043d\u044f", true), new BooleanSetting("key", true), new BooleanSetting("inventory", true), new BooleanSetting("\u0420\u0430\u0441\u043f\u0438\u0441\u0430\u043d\u0438\u0435 \u0441\u043e\u0431\u044b\u0442\u0438\u0439 RW", true));
    private final WatermarkRenderer watermarkRenderer = new WatermarkRenderer();
    private final ArrayListRenderer arrayListRenderer = new ArrayListRenderer();
    private final CoordsRenderer coordsRenderer = new CoordsRenderer();
    private final PotionRenderer potionRenderer;
    private final InventoryRenderer inventoryRenderer;
    private final KeyBindRenderer keyBindRenderer;
    private final TargetInfoRenderer targetInfoRenderer;
    private final ArmorRenderer armorRenderer;
    private final StaffListRenderer staffListRenderer;
    private final SchedulesRenderer schedulesRenderer;
    private final KeyStrocesRenderer keyStrocesRenderer;

    @Subscribe
    private void onUpdate(EventUpdate e) {
        if (!HUD.mc.gameSettings.showDebugInfo) {
            if (((Boolean)this.elements.getValueByName("\u0421\u043f\u0438\u0441\u043e\u043a \u043c\u043e\u0434\u0435\u0440\u0430\u0446\u0438\u0438").get()).booleanValue()) {
                this.staffListRenderer.update(e);
            }
            if (((Boolean)this.elements.getValueByName("\u0420\u0430\u0441\u043f\u0438\u0441\u0430\u043d\u0438\u0435 \u0441\u043e\u0431\u044b\u0442\u0438\u0439 RW").get()).booleanValue()) {
                this.schedulesRenderer.update(e);
            }
            if (((Boolean)this.elements.getValueByName("\u0421\u043f\u0438\u0441\u043e\u043a \u043c\u043e\u0434\u0443\u043b\u0435\u0439").get()).booleanValue()) {
                this.arrayListRenderer.update(e);
            }
        }
    }

    @Subscribe
    private void onDisplay(EventDisplay e) {
        if (!HUD.mc.gameSettings.showDebugInfo && e.getType() == EventDisplay.Type.POST) {
            if (((Boolean)this.elements.getValueByName("\u042d\u0444\u0444\u0435\u043a\u0442\u044b").get()).booleanValue()) {
                this.potionRenderer.render(e);
            }
            if (((Boolean)this.elements.getValueByName("\u0412\u0430\u0442\u0435\u0440\u043c\u0430\u0440\u043a\u0430").get()).booleanValue()) {
                this.watermarkRenderer.render(e);
            }
            if (((Boolean)this.elements.getValueByName("\u0421\u043f\u0438\u0441\u043e\u043a \u043c\u043e\u0434\u0443\u043b\u0435\u0439").get()).booleanValue()) {
                this.arrayListRenderer.render(e);
            }
            if (((Boolean)this.elements.getValueByName("\u0410\u043a\u0442\u0438\u0432\u043d\u044b\u0435 \u0431\u0438\u043d\u0434\u044b").get()).booleanValue()) {
                this.keyBindRenderer.render(e);
            }
            if (((Boolean)this.elements.getValueByName("\u0421\u043f\u0438\u0441\u043e\u043a \u043c\u043e\u0434\u0435\u0440\u0430\u0446\u0438\u0438").get()).booleanValue()) {
                this.staffListRenderer.render(e);
            }
            if (((Boolean)this.elements.getValueByName("\u0410\u043a\u0442\u0438\u0432\u043d\u044b\u0439 \u0442\u0430\u0440\u0433\u0435\u0442").get()).booleanValue()) {
                this.targetInfoRenderer.render(e);
            }
            if (((Boolean)this.elements.getValueByName("\u0411\u0440\u043e\u043d\u044f").get()).booleanValue()) {
                this.armorRenderer.render(e);
            }
            if (((Boolean)this.elements.getValueByName("key").get()).booleanValue()) {
                this.keyStrocesRenderer.render(e);
            }
            if (((Boolean)this.elements.getValueByName("inventory").get()).booleanValue()) {
                this.inventoryRenderer.render(e);
            }
            if (((Boolean)this.elements.getValueByName("\u0420\u0430\u0441\u043f\u0438\u0441\u0430\u043d\u0438\u0435 \u0441\u043e\u0431\u044b\u0442\u0438\u0439 RW").get()).booleanValue()) {
                this.schedulesRenderer.render(e);
            }
        }
    }

    public HUD() {
        Dragging key = Expensive.getInstance().createDrag(this, "key", 450.0f, 6.0f);
        Dragging potions = Expensive.getInstance().createDrag(this, "Potions", 405.0f, 6.0f);
        Dragging schedules = Expensive.getInstance().createDrag(this, "\u0420\u0430\u0441\u043f\u0438\u0441\u0430\u043d\u0438\u0435 \u0441\u043e\u0431\u044b\u0442\u0438\u0439 RW", 405.0f, 43.0f);
        Dragging armor = Expensive.getInstance().createDrag(this, "Armor", 450.0f, 27.0f);
        Dragging keyBinds = Expensive.getInstance().createDrag(this, "KeyBinds", 405.0f, 27.0f);
        Dragging dragging = Expensive.getInstance().createDrag(this, "TargetHUD", 450.0f, 128.0f);
        Dragging staffList = Expensive.getInstance().createDrag(this, "StaffList", 450.0f, 5.0f);
        Dragging Inventory2 = Expensive.getInstance().createDrag(this, "inventory", 450.0f, 70.0f);
        this.schedulesRenderer = new SchedulesRenderer(schedules);
        this.potionRenderer = new PotionRenderer(potions);
        this.inventoryRenderer = new InventoryRenderer(Inventory2);
        this.armorRenderer = new ArmorRenderer(armor);
        this.keyStrocesRenderer = new KeyStrocesRenderer(key);
        this.keyBindRenderer = new KeyBindRenderer(keyBinds);
        this.staffListRenderer = new StaffListRenderer(staffList);
        this.targetInfoRenderer = new TargetInfoRenderer(dragging);
        this.addSettings(this.elements);
        DragManager.load();
    }

    public static int getColor(int index) {
        return Themes.viborTEM(index + 16);
    }

    public static int getColor(int index, float mult) {
        return Themes.viborTEM((int)((float)index * mult + 16.0f) + 16);
    }

    public static int getColor(int firstColor, int secondColor, int index, float mult) {
        return ColorUtils.gradient(firstColor, secondColor, (int)((float)index * mult), 10);
    }
}

