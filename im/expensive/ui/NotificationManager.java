/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import im.expensive.utils.animations.Animation;
import im.expensive.utils.animations.Direction;
import im.expensive.utils.animations.impl.EaseBackIn;
import im.expensive.utils.animations.impl.EaseInOutQuad;
import im.expensive.utils.client.ClientUtil;
import im.expensive.utils.client.IMinecraft;
import im.expensive.utils.math.MathUtil;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.font.Fonts;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector4f;

public class NotificationManager {
    private final CopyOnWriteArrayList<Notification> notifications = new CopyOnWriteArrayList();
    private MathUtil AnimationMath;
    private ImageType imageType;
    boolean state;

    public void add(String text, String content, int time, ImageType imageType) {
        this.notifications.add(new Notification(text, content, time, imageType));
    }

    public void draw(MatrixStack stack) {
        int yOffset = 0;
        int windowWidth = ClientUtil.calc(IMinecraft.mc.getMainWindow().getScaledWidth());
        int windowHeight = ClientUtil.calc(IMinecraft.mc.getMainWindow().getScaledHeight());
        for (Notification notification : this.notifications) {
            if (System.currentTimeMillis() - notification.getTime() <= (long)notification.time2 * 1000L - 300L) {
                notification.yAnimation.setDirection(Direction.FORWARDS);
            }
            notification.alpha = (float)notification.animation.getOutput();
            if (System.currentTimeMillis() - notification.getTime() > (long)notification.time2 * 1000L) {
                notification.yAnimation.setDirection(Direction.BACKWARDS);
            }
            if (notification.yAnimation.finished(Direction.BACKWARDS)) {
                this.notifications.remove(notification);
                continue;
            }
            float x = (float)windowWidth - (Fonts.sfMedium.getWidth(notification.getText(), 7.0f) + 8.0f) - (float)ClientUtil.calc(430);
            float y = windowHeight - ClientUtil.calc(40);
            notification.yAnimation.setEndPoint(yOffset);
            notification.yAnimation.setDuration(500);
            notification.setX(x);
            notification.setY(MathUtil.fast(notification.getY(), y -= (float)((double)notification.draw(stack) * notification.yAnimation.getOutput() + (double)ClientUtil.calc(40)), 15.0f));
            ++yOffset;
        }
    }

    private class Notification {
        private float x;
        private float y = IMinecraft.mc.getMainWindow().getScaledHeight() + ClientUtil.calc(24);
        private String text;
        private String content;
        private long time = System.currentTimeMillis();
        public Animation animation = new EaseInOutQuad(500, 1.0, Direction.FORWARDS);
        public Animation yAnimation = new EaseBackIn(500, 1.0, 1.0f);
        private ImageType imageType;
        float alpha;
        int time2 = 3;
        private boolean isState;
        private boolean state;

        public Notification(String text, String content, int time, ImageType imageType) {
            this.text = text;
            this.content = content;
            this.time2 = time;
            this.imageType = imageType;
        }

        public float draw(MatrixStack stack) {
            int windowWidth = ClientUtil.calc(IMinecraft.mc.getMainWindow().getScaledWidth());
            int windowHeight = ClientUtil.calc(IMinecraft.mc.getMainWindow().getScaledHeight());
            float textWidth = Fonts.sfMedium.getWidth(this.text, 7.0f);
            float notificationWidth = textWidth + (float)ClientUtil.calc(8);
            float maxNotificationWidth = ClientUtil.calc(300);
            notificationWidth = Math.min(notificationWidth, maxNotificationWidth);
            float imageOffset = ClientUtil.calc(470);
            float xCentered = ((float)windowWidth - notificationWidth) / 2.0f + 900.0f;
            float backgroundX = xCentered - imageOffset;
            float imageX = xCentered - imageOffset - 13.0f;
            DisplayUtils.drawRoundedRect(backgroundX - 1.0f, this.y - 1.0f, notificationWidth + (float)ClientUtil.calc(43), (float)ClientUtil.calc(20), new Vector4f(ClientUtil.calc(5), ClientUtil.calc(5), ClientUtil.calc(5), ClientUtil.calc(5)), ColorUtils.rgb(ClientUtil.calc(23), ClientUtil.calc(22), ClientUtil.calc(22)));
            if (this.imageType == ImageType.FIRST_PHOTO) {
                ResourceLocation key = new ResourceLocation("expensive/images/cancel.png");
                DisplayUtils.drawShadow(imageX, this.y + (float)ClientUtil.calc(5), ClientUtil.calc(8), ClientUtil.calc(8), 11, ColorUtils.rgb(190, 34, 34));
                DisplayUtils.drawImage(key, imageX, this.y + (float)ClientUtil.calc(5), (float)ClientUtil.calc(8), (float)ClientUtil.calc(8), -1);
            } else {
                ResourceLocation key1 = new ResourceLocation("expensive/images/33d.png");
                DisplayUtils.drawShadow(imageX, this.y + (float)ClientUtil.calc(5), ClientUtil.calc(8), ClientUtil.calc(8), 11, ColorUtils.rgb(0, 128, 0));
                DisplayUtils.drawImage(key1, imageX, this.y + (float)ClientUtil.calc(5), (float)ClientUtil.calc(8), (float)ClientUtil.calc(8), -1);
            }
            Fonts.sfMedium.drawText(stack, this.text, xCentered - (float)ClientUtil.calc(467), this.y + (float)ClientUtil.calc(5), -1, 7.5f, 0.05f);
            int enabledColor = ColorUtils.rgba(0, 255, 0, 255);
            int disabledColor = ColorUtils.rgba(0, 170, 0, 255);
            int contentColor = this.state ? enabledColor : disabledColor;
            Fonts.sfMedium.drawText(stack, this.content, xCentered - (float)ClientUtil.calc(495), this.y + (float)ClientUtil.calc(2), contentColor, 7.0f, 0.05f);
            return ClientUtil.calc(25);
        }

        public float getX() {
            return this.x;
        }

        public float getY() {
            return this.y;
        }

        public void setX(float x) {
            this.x = x;
        }

        public void setY(float y) {
            this.y = y;
        }

        public String getText() {
            return this.text;
        }

        public String getContent() {
            return this.content;
        }

        public long getTime() {
            return this.time;
        }
    }

    public static enum ImageType {
        FIRST_PHOTO,
        SECOND_PHOTO;

    }
}

