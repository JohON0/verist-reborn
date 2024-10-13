/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.player;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventPacket;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.client.CPlayerPacket;

@FunctionRegister(name="BypassAura", type=Category.Player)
public class NoRotate
extends Function {
    private float targetYaw;
    private float targetPitch;
    private boolean isPacketSent;

    @Subscribe
    public void onPacket(EventPacket event) {
        IPacket<?> iPacket;
        if (event.isSend() && this.isPacketSent && (iPacket = event.getPacket()) instanceof CPlayerPacket) {
            CPlayerPacket playerPacket = (CPlayerPacket)iPacket;
            playerPacket.setRotation(this.targetYaw, this.targetPitch);
            this.isPacketSent = false;
        }
    }

    public void sendRotationPacket(float yaw, float pitch) {
        this.targetYaw = yaw;
        this.targetPitch = pitch;
        this.isPacketSent = true;
    }
}

