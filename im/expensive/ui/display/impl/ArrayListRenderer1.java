/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.display.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import im.expensive.events.EventDisplay;
import im.expensive.events.EventUpdate;
import im.expensive.ui.display.ElementRenderer;
import im.expensive.ui.display.ElementUpdater;
import im.expensive.utils.math.StopWatch;
import im.expensive.utils.render.DisplayUtils;
import java.util.List;
import net.minecraft.entity.Entity;

public class ArrayListRenderer1
implements ElementRenderer,
ElementUpdater {
    private int lastIndex;
    private List<Entity> players;
    StopWatch stopWatch = new StopWatch();

    @Override
    public void update(EventUpdate e) {
        if (this.stopWatch.isReached(1000L)) {
            this.stopWatch.reset();
        }
    }

    @Override
    public void render(EventDisplay eventDisplay) {
        MatrixStack ms = eventDisplay.getMatrixStack();
        float posX = 100.0f;
        float posY = 100.0f;
        float radarRadius = 50.0f;
        if (this.players == null) {
            return;
        }
        for (Entity player : this.players) {
            float playerX = 5.0f;
            float distanceX = playerX - posX;
            float playerY = 5.0f;
            float distanceY = playerY - posY;
            if (!(Math.sqrt(distanceX * distanceX + distanceY * distanceY) <= (double)radarRadius)) continue;
            float playerRadius = 3.0f;
            float radarPlayerX = posX + distanceX;
            float radarPlayerY = posY + distanceY;
            DisplayUtils.drawCircle(radarPlayerX, radarPlayerY, playerRadius, -1);
        }
    }
}

