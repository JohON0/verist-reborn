/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.misc;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.AttackEvent;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.impl.ModeSetting;
import im.expensive.functions.settings.impl.SliderSetting;
import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

@FunctionRegister(name="HitSound", type=Category.Misc)
public class HitSound
extends Function {
    private final ModeSetting sound = new ModeSetting("\u0417\u0432\u0443\u043a", "bell", "bell", "metallic", "rust", "bubble", "bonk", "crime", "uwu");
    SliderSetting volume = new SliderSetting("\u0413\u0440\u043e\u043c\u043a\u043e\u0441\u0442\u044c", 35.0f, 5.0f, 100.0f, 5.0f);

    public HitSound() {
        this.addSettings(this.sound, this.volume);
    }

    @Subscribe
    public void onPacket(AttackEvent e) {
        block3: {
            block2: {
                if (Minecraft.player == null) break block2;
                if (Minecraft.world != null) break block3;
            }
            return;
        }
        this.playSound(e.entity);
    }

    public void playSound(Entity e) {
        try {
            Clip clip = AudioSystem.getClip();
            InputStream is = mc.getResourceManager().getResource(new ResourceLocation("expensive/sounds/" + (String)this.sound.get() + ".wav")).getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bis);
            if (audioInputStream == null) {
                System.out.println("Sound not found!");
                return;
            }
            clip.open(audioInputStream);
            clip.start();
            FloatControl floatControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            if (e != null) {
                FloatControl balance = (FloatControl)clip.getControl(FloatControl.Type.BALANCE);
                Vector3d vector3d = e.getPositionVec();
                Minecraft.getInstance();
                Vector3d vec = vector3d.subtract(Minecraft.player.getPositionVec());
                double yaw = MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(vec.z, vec.x)) - 90.0);
                double delta = MathHelper.wrapDegrees(yaw - (double)Minecraft.player.rotationYaw);
                if (Math.abs(delta) > 180.0) {
                    delta -= Math.signum(delta) * 360.0;
                }
                try {
                    balance.setValue((float)delta / 180.0f);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            floatControl.setValue(-(Minecraft.player.getDistance(e) * 5.0f) - this.volume.max / ((Float)this.volume.get()).floatValue());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

