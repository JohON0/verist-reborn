/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.display.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.mojang.blaze3d.matrix.MatrixStack;
import im.expensive.Expensive;
import im.expensive.events.EventDisplay;
import im.expensive.events.EventUpdate;
import im.expensive.ui.display.ElementRenderer;
import im.expensive.ui.display.ElementUpdater;
import im.expensive.ui.schedules.funtime.FunTimeEventData;
import im.expensive.ui.styles.Style;
import im.expensive.utils.HTTP;
import im.expensive.utils.drag.Dragging;
import im.expensive.utils.math.StopWatch;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.Scissor;
import im.expensive.utils.render.font.Fonts;
import im.expensive.utils.text.GradientUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.util.text.StringTextComponent;

public class FuntimeSchedulesRenderer
implements ElementRenderer,
ElementUpdater {
    static final String EVENT_LIST_URL = "http://87.251.66.149:1488/funtime/" + Expensive.userData.getUser();
    private final List<FunTimeEventData> eventList = new CopyOnWriteArrayList<FunTimeEventData>();
    private final Dragging dragging;
    private float width;
    private float height;
    private final StopWatch stopWatch = new StopWatch();
    private final boolean notify = false;

    public FuntimeSchedulesRenderer(Dragging dragging) {
        this.dragging = dragging;
        this.updateEventsAsync();
    }

    @Override
    public void update(EventUpdate e) {
        if (this.stopWatch.isReached(180000L)) {
            this.updateEventsAsync();
            this.stopWatch.reset();
        }
    }

    @Override
    public void render(EventDisplay eventDisplay) {
        MatrixStack ms = eventDisplay.getMatrixStack();
        float posX = this.dragging.getX();
        float posY = this.dragging.getY();
        float fontSize = 6.5f;
        float padding = 5.0f;
        StringTextComponent name = GradientUtil.gradient("FT Schedules", ColorUtils.rgba(255, 0, 0, 255), ColorUtils.rgba(0, 255, 0, 255));
        Style style = Expensive.getInstance().getStyleManager().getCurrentStyle();
        DisplayUtils.drawShadow(posX, posY, this.width, this.height, 10, style.getFirstColor().getRGB(), style.getSecondColor().getRGB());
        this.drawStyledRect(posX, posY, this.width, this.height, 4.0f);
        Scissor.push();
        Scissor.setFromComponentCoordinates(posX, posY, this.width, this.height);
        Fonts.sfui.drawCenteredText(ms, name, posX + this.width / 2.0f, posY + padding + 0.5f, fontSize);
        float maxWidth = Fonts.sfMedium.getWidth(name, fontSize) + padding * 2.0f;
        float localHeight = fontSize + padding * 2.0f;
        DisplayUtils.drawRectHorizontalW(posX + 0.5f, posY += fontSize + padding * 2.0f, this.width - 1.0f, 2.5, 3, ColorUtils.rgba(0, 0, 0, 63));
        posY += 3.0f;
        for (FunTimeEventData data : this.eventList) {
            int timeTo = data.timeTo - (int)(System.currentTimeMillis() / 1000L);
            if (timeTo > 500 || timeTo <= 0) continue;
            String nameText = String.format("/an%d", data.anarchy);
            String formattedTime = this.formatTime(timeTo);
            float nameWidth = Fonts.sfMedium.getWidth(nameText, fontSize);
            float bindWidth = Fonts.sfMedium.getWidth(formattedTime, fontSize);
            float localWidth = nameWidth + bindWidth + padding * 3.0f;
            Fonts.sfMedium.drawText(ms, nameText, posX + padding, posY, ColorUtils.rgba(210, 210, 210, 255), fontSize);
            Fonts.sfMedium.drawText(ms, formattedTime, posX + this.width - padding - bindWidth, posY, ColorUtils.rgba(210, 210, 210, 255), fontSize);
            if (localWidth > maxWidth) {
                maxWidth = localWidth;
            }
            posY += fontSize + padding;
            localHeight += fontSize + padding;
        }
        Scissor.unset();
        Scissor.pop();
        this.width = Math.max(maxWidth, 80.0f);
        this.height = localHeight + 2.5f;
        this.dragging.setWidth(this.width);
        this.dragging.setHeight(this.height);
    }

    private String formatTime(int seconds) {
        if (seconds < 60) {
            return String.format("%2ds", seconds);
        }
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%2dm %2ds", minutes, remainingSeconds);
    }

    private void drawStyledRect(float x, float y, float width, float height, float radius) {
        DisplayUtils.drawRoundedRect(x - 0.5f, y - 0.5f, width + 1.0f, height + 1.0f, radius + 0.5f, ColorUtils.getColor(0));
        DisplayUtils.drawRoundedRect(x, y, width, height, radius, ColorUtils.rgba(21, 21, 21, 255));
    }

    public void updateEventsAsync() {
        CompletableFuture.supplyAsync(() -> {
            String resp = HTTP.getHTTP(EVENT_LIST_URL);
            Gson gson = new Gson();
            JsonArray list = gson.fromJson(resp.trim(), JsonArray.class);
            ArrayList<FunTimeEventData> events = new ArrayList<FunTimeEventData>();
            for (int i = 0; i < list.size(); ++i) {
                JsonArray obj = list.get(i).getAsJsonArray();
                FunTimeEventData eventData = new FunTimeEventData();
                eventData.timeTo = (int)(System.currentTimeMillis() / 1000L) + obj.get(0).getAsInt();
                eventData.anarchy = Integer.parseInt(obj.get(1).getAsString());
                if (events.size() >= 8) continue;
                events.add(eventData);
            }
            return events;
        }).thenAcceptAsync(updatedEvents -> {
            this.eventList.clear();
            this.eventList.addAll((Collection<FunTimeEventData>)updatedEvents);
        });
    }
}

