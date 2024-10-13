/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventPacket;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.ModeSetting;
import im.expensive.functions.settings.impl.SliderSetting;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SUpdateTimePacket;

@FunctionRegister(name="World", type=Category.Render)
public class World
extends Function {
    public ModeSetting time = new ModeSetting("Time", "\u0414\u0435\u043d\u044c", "\u0414\u0435\u043d\u044c", "\u041d\u043e\u0447\u044c", "Custom");
    public SliderSetting timeSlider = new SliderSetting("Time Slider", 1000.0f, 1000.0f, 24000.0f, 1.0f);

    public World() {
        this.addSettings(this.time, this.timeSlider);
    }

    @Subscribe
    public void onPacket(EventPacket e) {
        IPacket<?> iPacket = e.getPacket();
        if (iPacket instanceof SUpdateTimePacket) {
            SUpdateTimePacket p = (SUpdateTimePacket)iPacket;
            String timeSetting = ((String)this.time.get()).toLowerCase();
            System.out.println("Time setting: " + timeSetting);
            switch (timeSetting) {
                case "\u0434\u0435\u043d\u044c": {
                    p.worldTime = 1000L;
                    break;
                }
                case "\u043d\u043e\u0447\u044c": {
                    p.worldTime = 13000L;
                    break;
                }
                case "custom": {
                    p.worldTime = (Long)this.timeSlider.getValue();
                    break;
                }
            }
        }
    }

    public ModeSetting getTime() {
        return this.time;
    }

    public SliderSetting getTimeSlider() {
        return this.timeSlider;
    }
}

