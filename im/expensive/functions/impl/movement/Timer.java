/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.movement;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.SliderSetting;

@FunctionRegister(name="Timer", type=Category.Movement)
public class Timer
extends Function {
    private final SliderSetting speed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c", 2.0f, 0.1f, 10.0f, 0.1f);

    public Timer() {
        this.addSettings(this.speed);
    }

    @Subscribe
    private void onUpdate(EventUpdate e) {
        Timer.mc.timer.timerSpeed = ((Float)this.speed.get()).floatValue();
    }

    private void reset() {
        Timer.mc.timer.timerSpeed = 1.0f;
    }

    @Override
    public boolean onEnable() {
        super.onEnable();
        this.reset();
        return false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.reset();
    }
}

