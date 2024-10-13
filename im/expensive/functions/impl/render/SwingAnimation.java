/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.impl.combat.KillAura;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.functions.settings.impl.ModeSetting;
import im.expensive.functions.settings.impl.SliderSetting;
import net.minecraft.util.math.vector.Vector3f;

@FunctionRegister(name="SwingAnimation", type=Category.Render)
public class SwingAnimation
extends Function {
    public ModeSetting animationMode = new ModeSetting("\u041c\u043e\u0434", "Two", "First", "Two", "Free", "Four", "Five", "Six", "Circle");
    public SliderSetting swingPower = new SliderSetting("\u0421\u0438\u043b\u0430", 5.0f, 1.0f, 10.0f, 0.05f);
    public SliderSetting swingSpeed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c", 10.0f, 3.0f, 10.0f, 1.0f);
    public SliderSetting scale = new SliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440", 1.0f, 0.5f, 1.5f, 0.05f);
    public final BooleanSetting onlyAura = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u0441 \u043a\u0438\u043b\u043b\u0430\u0443\u0440\u043e\u0439", true);
    public final BooleanSetting fullCircleRotation = new BooleanSetting("\u0412\u0440\u0430\u0449\u0435\u043d\u0438\u0435 \u043d\u0430 360", false);
    public KillAura killAura;

    public SwingAnimation(KillAura killAura) {
        this.killAura = killAura;
        this.addSettings(this.animationMode, this.swingPower, this.swingSpeed, this.scale, this.onlyAura, this.fullCircleRotation);
    }

    public void animationProcess(MatrixStack stack, float swingProgress, Runnable runnable) {
        float anim = (float)Math.sin((double)swingProgress * 1.5707963267948966 * 2.0);
        if (((Boolean)this.onlyAura.get()).booleanValue() && this.killAura.getTarget() == null) {
            runnable.run();
            return;
        }
        if (((Boolean)this.fullCircleRotation.get()).booleanValue()) {
            long time = System.currentTimeMillis();
            float rotation = (float)(time % 3600L) / 10.0f;
            stack.scale(((Float)this.scale.get()).floatValue(), ((Float)this.scale.get()).floatValue(), ((Float)this.scale.get()).floatValue());
            float yOffset = -0.2f * swingProgress;
            stack.translate(0.0, yOffset, -0.5);
            stack.rotate(Vector3f.YP.rotationDegrees(rotation));
        } else {
            switch (this.animationMode.getIndex()) {
                case 0: {
                    stack.scale(((Float)this.scale.get()).floatValue(), ((Float)this.scale.get()).floatValue(), ((Float)this.scale.get()).floatValue());
                    stack.translate(0.4f, 0.1f, -0.5);
                    stack.rotate(Vector3f.YP.rotationDegrees(90.0f));
                    stack.rotate(Vector3f.ZP.rotationDegrees(-60.0f));
                    stack.rotate(Vector3f.XP.rotationDegrees(-90.0f - ((Float)this.swingPower.get()).floatValue() * 10.0f * anim));
                    break;
                }
                case 1: {
                    stack.scale(((Float)this.scale.get()).floatValue(), ((Float)this.scale.get()).floatValue(), ((Float)this.scale.get()).floatValue());
                    stack.translate(0.0, 0.0, -0.5);
                    stack.rotate(Vector3f.YP.rotationDegrees(15.0f * anim));
                    stack.rotate(Vector3f.ZP.rotationDegrees(-60.0f * anim));
                    stack.rotate(Vector3f.XP.rotationDegrees((-90.0f - ((Float)this.swingPower.get()).floatValue()) * anim));
                    break;
                }
                case 2: {
                    stack.scale(((Float)this.scale.get()).floatValue(), ((Float)this.scale.get()).floatValue(), ((Float)this.scale.get()).floatValue());
                    stack.translate(0.4f, 0.0, -0.5);
                    stack.rotate(Vector3f.YP.rotationDegrees(90.0f));
                    stack.rotate(Vector3f.ZP.rotationDegrees(-30.0f));
                    stack.rotate(Vector3f.XP.rotationDegrees(-90.0f - ((Float)this.swingPower.get()).floatValue() * 10.0f * anim));
                    break;
                }
                case 3: {
                    stack.scale(((Float)this.scale.get()).floatValue(), ((Float)this.scale.get()).floatValue(), ((Float)this.scale.get()).floatValue());
                    stack.translate(0.0, 0.0, -0.5);
                    stack.rotate(Vector3f.YP.rotationDegrees(0.0f - anim));
                    stack.rotate(Vector3f.ZP.rotationDegrees(60.0f * anim));
                    stack.rotate(Vector3f.XP.rotationDegrees(-50.0f + anim));
                    break;
                }
                case 4: {
                    stack.scale(((Float)this.scale.get()).floatValue(), ((Float)this.scale.get()).floatValue(), ((Float)this.scale.get()).floatValue());
                    stack.translate(0.0, 0.0, -0.5);
                    stack.rotate(Vector3f.YP.rotationDegrees(90.0f));
                    stack.rotate(Vector3f.ZP.rotationDegrees(0.0f));
                    stack.rotate(Vector3f.XP.rotationDegrees(-200.0f - ((Float)this.swingPower.get()).floatValue() * 10.0f * anim));
                    break;
                }
                case 5: {
                    stack.scale(((Float)this.scale.get()).floatValue(), ((Float)this.scale.get()).floatValue(), ((Float)this.scale.get()).floatValue());
                    stack.translate(0.0, -0.5f * anim, -0.5f * anim);
                    stack.rotate(Vector3f.XP.rotationDegrees(-90.0f + anim));
                    stack.translate(0.0, 0.5f * anim, 0.5f * anim);
                    break;
                }
                default: {
                    stack.scale(((Float)this.scale.get()).floatValue(), ((Float)this.scale.get()).floatValue(), ((Float)this.scale.get()).floatValue());
                    runnable.run();
                }
            }
        }
    }
}

