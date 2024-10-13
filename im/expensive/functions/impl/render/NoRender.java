//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package im.expensive.functions.impl.render;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventCancelOverlay;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.Setting;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.functions.settings.impl.ModeListSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Effects;

@FunctionRegister(
        name = "NoRender",
        type = Category.Render
)
public class NoRender extends Function {
    public ModeListSetting element = new ModeListSetting("Удалять", new BooleanSetting[]{new BooleanSetting("Огонь на экране", true), new BooleanSetting("Линия босса", true), new BooleanSetting("Анимация тотема", true), new BooleanSetting("Тайтлы", true), new BooleanSetting("Таблица", true), new BooleanSetting("Туман", true), new BooleanSetting("Тряску камеры", true), new BooleanSetting("Плохие эффекты", true), new BooleanSetting("Дождь", true)});

    public NoRender() {
        this.addSettings(new Setting[]{this.element});
    }

    @Subscribe
    private void onUpdate(EventUpdate e) {
        this.handleEventUpdate(e);
    }

    @Subscribe
    private void onEventCancelOverlay(EventCancelOverlay e) {
        this.handleEventOverlaysRender(e);
    }

    private void handleEventOverlaysRender(EventCancelOverlay event) {
        boolean var10000;
        switch (event.overlayType.ordinal()) {
            case 1:
                var10000 = (Boolean)this.element.getValueByName("Огонь на экране").get();
                break;
            case 2:
                var10000 = (Boolean)this.element.getValueByName("Линия босса").get();
                break;
            case 3:
                var10000 = (Boolean)this.element.getValueByName("Таблица").get();
                break;
            case 4:
                var10000 = (Boolean)this.element.getValueByName("Тайтлы").get();
                break;
            case 5:
                var10000 = (Boolean)this.element.getValueByName("Анимация тотема").get();
                break;
            case 6:
                var10000 = (Boolean)this.element.getValueByName("Туман").get();
                break;
            case 7:
                var10000 = (Boolean)this.element.getValueByName("Тряску камеры").get();
                break;
            default:
                throw new IncompatibleClassChangeError();
        }

        boolean cancelOverlay = var10000;
        if (cancelOverlay) {
            event.cancel();
        }

    }

    private void handleEventUpdate(EventUpdate event) {
        Minecraft var10000;
        boolean isRaining;
        boolean var4;
        label28: {
            label27: {
                var10000 = mc;
                isRaining = Minecraft.world.isRaining() && (Boolean)this.element.getValueByName("Дождь").get();
                var10000 = mc;
                if (!Minecraft.player.isPotionActive(Effects.BLINDNESS)) {
                    var10000 = mc;
                    if (!Minecraft.player.isPotionActive(Effects.NAUSEA)) {
                        break label27;
                    }
                }

                if ((Boolean)this.element.getValueByName("Плохие эффекты").get()) {
                    var4 = true;
                    break label28;
                }
            }

            var4 = false;
        }

        boolean hasEffects = var4;
        if (isRaining) {
            var10000 = mc;
            Minecraft.world.setRainStrength(0.0F);
            var10000 = mc;
            Minecraft.world.setThunderStrength(0.0F);
        }

        if (hasEffects) {
            var10000 = mc;
            Minecraft.player.removePotionEffect(Effects.NAUSEA);
            var10000 = mc;
            Minecraft.player.removePotionEffect(Effects.BLINDNESS);
        }

    }
}
