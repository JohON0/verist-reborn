/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils.player;

import im.expensive.events.EventDamageReceive;
import im.expensive.events.EventPacket;
import im.expensive.utils.client.IMinecraft;
import im.expensive.utils.math.StopWatch;
import net.minecraft.client.Minecraft;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SEntityStatusPacket;
import net.minecraft.network.play.server.SExplosionPacket;
import net.minecraft.potion.Effects;

public class DamagePlayerUtil
implements IMinecraft {
    private final StopWatch timeTracker = new StopWatch();
    private boolean normalDamage;
    private boolean fallDamage;
    private boolean explosionDamage;
    private boolean arrowDamage;
    private boolean pearlDamage;

    public void onPacketEvent(EventPacket eventPacket) {
        boolean isDamage;
        boolean bl = isDamage = this.fallDamage || this.arrowDamage || this.explosionDamage || this.pearlDamage;
        if (this.isBadEffects()) {
            return;
        }
        if (eventPacket.getPacket() instanceof SExplosionPacket) {
            this.explosionDamage = true;
        }
        if (!isDamage) {
            SEntityStatusPacket statusPacket;
            IPacket<?> packet = eventPacket.getPacket();
            if (packet instanceof SEntityStatusPacket && (statusPacket = (SEntityStatusPacket)packet).getOpCode() == 2) {
                if (statusPacket.getEntity(Minecraft.world) == Minecraft.player) {
                    this.normalDamage = true;
                }
            }
        } else if (Minecraft.player.hurtTime > 0) {
            this.normalDamage = false;
            this.reset();
        }
    }

    public boolean time(long time) {
        if (this.normalDamage) {
            if (this.timeTracker.isReached(time)) {
                this.normalDamage = false;
                this.timeTracker.reset();
                return true;
            }
        } else {
            this.timeTracker.reset();
        }
        return false;
    }

    public void processDamage(EventDamageReceive damageEvent) {
        switch (damageEvent.getDamageType()) {
            case FALL: {
                this.fallDamage = true;
                break;
            }
            case ARROW: {
                this.arrowDamage = true;
                break;
            }
            case ENDER_PEARL: {
                this.pearlDamage = true;
            }
        }
        this.normalDamage = false;
    }

    public void resetDamages() {
        this.normalDamage = false;
        this.reset();
        this.timeTracker.reset();
    }

    private void reset() {
        this.fallDamage = false;
        this.explosionDamage = false;
        this.arrowDamage = false;
        this.pearlDamage = false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean isBadEffects() {
        if (Minecraft.player == null) {
            return false;
        }
        if (Minecraft.player.isPotionActive(Effects.POISON)) return true;
        if (Minecraft.player.isPotionActive(Effects.WITHER)) return true;
        if (!Minecraft.player.isPotionActive(Effects.INSTANT_DAMAGE)) return false;
        return true;
    }

    public boolean isNormalDamage() {
        return this.normalDamage;
    }
}

