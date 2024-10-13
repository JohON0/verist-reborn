/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.movement;

import com.google.common.eventbus.Subscribe;
import im.expensive.events.MovingEvent;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.vector.Vector3d;

@FunctionRegister(name="NoClip", type=Category.Movement)
public class NoClip
extends Function {
    @Subscribe
    private void onMoving(MovingEvent move) {
        block4: {
            block6: {
                block5: {
                    if (this.collisionPredict(move.getTo())) break block4;
                    if (move.isCollidedHorizontal()) {
                        move.setIgnoreHorizontal(true);
                    }
                    if (move.getMotion().y > 0.0) break block5;
                    if (!Minecraft.player.isSneaking()) break block6;
                }
                move.setIgnoreVertical(true);
            }
            move.getMotion().y = Math.min(move.getMotion().y, 99999.0);
        }
    }

    public boolean collisionPredict(Vector3d to) {
        boolean prevCollision = Minecraft.world.getCollisionShapes(Minecraft.player, Minecraft.player.getBoundingBox().shrink(0.0625)).toList().isEmpty();
        Vector3d backUp = new Vector3d(Minecraft.player.getPosX(), Minecraft.player.getPosY(), Minecraft.player.getPosZ());
        Minecraft.player.setPosition(to.x, to.y, to.z);
        boolean collision = Minecraft.world.getCollisionShapes(Minecraft.player, Minecraft.player.getBoundingBox().shrink(0.0625)).toList().isEmpty() && prevCollision;
        Minecraft.player.setPosition(backUp.x, backUp.y, backUp.z);
        return collision;
    }
}

