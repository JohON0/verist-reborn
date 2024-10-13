/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.api;

import im.expensive.Expensive;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.api.FunctionRegistry;
import im.expensive.functions.api.WermetistPidor;
import im.expensive.functions.impl.misc.ClientSounds;
import im.expensive.functions.settings.Setting;
import im.expensive.ui.NotificationManager;
import im.expensive.utils.client.ClientUtil;
import im.expensive.utils.client.IMinecraft;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import ru.hogoshi.Animation;
import ru.hogoshi.util.Easings;

public abstract class Function
implements IMinecraft {
    private final String name;
    private final Category category;
    private boolean state;
    private int bind;
    private final List<Setting<?>> settings = new ObjectArrayList();
    private final Animation animation = new Animation();
    private Function Managed;

    public Function() {
        this.name = this.getClass().getAnnotation(FunctionRegister.class).name();
        this.category = this.getClass().getAnnotation(FunctionRegister.class).type();
        this.bind = this.getClass().getAnnotation(FunctionRegister.class).key();
    }

    public Function(String name) {
        this.name = name;
        this.category = Category.Combat;
    }

    public void addSettings(Setting<?> ... settings) {
        this.settings.addAll(List.of((Object[])settings));
    }

    public boolean onEnable() {
        this.animation.animate(1.0, 0.25, Easings.QUINT_IN);
        Expensive.getInstance().getEventBus().register(this);
        return false;
    }

    public void onDisable() {
        this.animation.animate(0.0, 0.25, Easings.SINE_BOTH);
        Expensive.getInstance().getEventBus().unregister(this);
    }

    public final void toggle() {
        block7: {
            block6: {
                if (Minecraft.player == null) break block6;
                if (Minecraft.world != null) break block7;
            }
            return;
        }
        boolean bl = this.state = !this.state;
        if (!this.state) {
            this.onDisable();
            WermetistPidor.NOTIFICATION_MANAGER.add(this.name + " was disable", "", 3, NotificationManager.ImageType.FIRST_PHOTO);
        } else {
            this.onEnable();
            WermetistPidor.NOTIFICATION_MANAGER.add(this.name + " was enable", "", 3, NotificationManager.ImageType.SECOND_PHOTO);
        }
        FunctionRegistry functionRegistry = Expensive.getInstance().getFunctionRegistry();
        ClientSounds clientSounds = functionRegistry.getClientSounds();
        if (clientSounds != null && clientSounds.isState()) {
            String fileName = clientSounds.getFileName(this.state);
            float volume = ((Float)clientSounds.volume.get()).floatValue();
            ClientUtil.playSound(fileName, volume, false);
        }
    }

    public final void setState(boolean newState, boolean config) {
        if (this.state == newState) {
            return;
        }
        this.state = newState;
        try {
            FunctionRegistry functionRegistry;
            ClientSounds clientSounds;
            if (this.state) {
                this.onEnable();
            } else {
                this.onDisable();
            }
            if (!config && (clientSounds = (functionRegistry = Expensive.getInstance().getFunctionRegistry()).getClientSounds()) != null && clientSounds.isState()) {
                String fileName = clientSounds.getFileName(this.state);
                float volume = ((Float)clientSounds.volume.get()).floatValue();
                ClientUtil.playSound(fileName, volume, false);
            }
        } catch (Exception e) {
            this.handleException(this.state ? "onEnable" : "onDisable", e);
        }
    }

    private void handleException(String methodName, Exception e) {
        if (Minecraft.player != null) {
            this.print("[" + this.name + "] \u041f\u0440\u043e\u0438\u0437\u043e\u0448\u043b\u0430 \u043e\u0448\u0438\u0431\u043a\u0430 \u0432 \u043c\u0435\u0442\u043e\u0434\u0435 " + TextFormatting.RED + methodName + TextFormatting.WHITE + "() \u041f\u0440\u0435\u0434\u043e\u0441\u0442\u0430\u0432\u044c\u0442\u0435 \u044d\u0442\u043e \u0441\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u0435 \u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a\u0443: " + TextFormatting.GRAY + e.getMessage());
            e.printStackTrace();
        } else {
            System.out.println("[" + this.name + " Error" + methodName + "() Message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getName() {
        return this.name;
    }

    public Category getCategory() {
        return this.category;
    }

    public boolean isState() {
        return this.state;
    }

    public int getBind() {
        return this.bind;
    }

    public List<Setting<?>> getSettings() {
        return this.settings;
    }

    public Animation getAnimation() {
        return this.animation;
    }

    public Function getManaged() {
        return this.Managed;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }
}

