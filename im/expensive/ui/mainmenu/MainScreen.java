/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.mainmenu;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import im.expensive.Expensive;
import im.expensive.utils.client.ClientUtil;
import im.expensive.utils.client.IMinecraft;
import im.expensive.utils.client.Vec2i;
import im.expensive.utils.math.MathUtil;
import im.expensive.utils.math.StopWatch;
import im.expensive.utils.math.Vector4i;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.KawaseBlur;
import im.expensive.utils.render.Stencil;
import im.expensive.utils.render.font.Fonts;
import im.expensive.utils.shader.ShaderUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.OptionsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorldSelectionScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.util.text.ITextComponent;

public class MainScreen
extends Screen
implements IMinecraft {
    private boolean showWelcomeText = true;
    private long welcomeTextStartTime;
    private boolean firstRender = true;
    private float[] currentY;
    private float[] targetY;
    private static final float ANIMATION_SPEED = 0.3f;
    private final ResourceLocation backmenu = new ResourceLocation("expensive/images/sun.png");
    private final ResourceLocation logo = new ResourceLocation("expensive/images/logo.png");
    private String altName = "";
    private boolean typing;
    private final List<Button> buttons = new ArrayList<Button>();
    private static final CopyOnWriteArrayList<Particle> particles = new CopyOnWriteArrayList();
    private final StopWatch stopWatch = new StopWatch();
    static boolean start = false;
    public static final ResourceLocation button = new ResourceLocation("expensive/images/button.png");

    public MainScreen() {
        super(ITextComponent.getTextComponentOrEmpty(""));
        int logoCount = 5;
        this.currentY = new float[logoCount];
        this.targetY = new float[logoCount];
        Arrays.fill(this.currentY, 0.0f);
        Arrays.fill(this.targetY, 0.0f);
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
        float widthButton = 150.0f;
        if (this.firstRender) {
            this.showWelcomeText = true;
            this.welcomeTextStartTime = System.currentTimeMillis();
            this.firstRender = false;
        }
        float x = (float)ClientUtil.calc(width) / 2.0f - widthButton / 2.0f;
        float y = Math.round((float)ClientUtil.calc(height) / 2.0f + 1.0f);
        this.buttons.clear();
        this.buttons.add(new Button(this, x - 27.0f, y - 80.0f, widthButton, 51.0f, "", () -> mc.displayGuiScreen(new WorldSelectionScreen(this))));
        this.buttons.add(new Button(this, x + 10.0f, (y += 39.0f) - 70.0f, widthButton, 51.0f, "", () -> mc.displayGuiScreen(new MultiplayerScreen(this))));
        this.buttons.add(new Button(this, x + 10.0f, (y += 39.0f) - 60.0f, widthButton, 50.0f, "", () -> mc.displayGuiScreen(new OptionsScreen(this, MainScreen.mc.gameSettings))));
        List<Button> var10000 = this.buttons;
        float var10004 = x + 115.0f;
        float var10005 = (y += 39.0f) - 127.0f;
        float var10006 = widthButton - 107.0f;
        Minecraft var10009 = mc;
        Objects.requireNonNull(var10009);
        var10000.add(new Button(this, var10004 - 100.0f, var10005 + 70.0f, var10006 + 100.0f, 50.0f, "", var10009::shutdownMinecraftApplet));
        this.buttons.add(new Button(this, x, (y += -28.0f) + 15.0f, widthButton, 50.0f, "", () -> mc.displayGuiScreen(Expensive.getInstance().getAltManager())));
        int yLogo = Math.round((float)ClientUtil.calc(height) / 2.0f + 1.0f);
        int[] yPositions = new int[]{45, 45, 45, 45, 140};
        for (int i = 0; i < yPositions.length; ++i) {
            this.currentY[i] = yLogo + yPositions[i];
            this.targetY[i] = yLogo + yPositions[i];
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        if (this.stopWatch.isReached(100L)) {
            particles.add(new Particle(this));
            this.stopWatch.reset();
        }
        MainWindow mainWindow = mc.getMainWindow();
        int windowWidth = ClientUtil.calc(mainWindow.getScaledWidth());
        int windowHeight = ClientUtil.calc(mainWindow.getScaledHeight());
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        DisplayUtils.shaderMainMenu.init();
        DisplayUtils.shaderMainMenu.setUniformf("time", (float)(System.currentTimeMillis() - Expensive.initTime) / 1500.0f);
        DisplayUtils.shaderMainMenu.setUniformf("resolution", mc.getMainWindow().getWidth(), mc.getMainWindow().getHeight());
        ShaderUtil.drawQuads();
        DisplayUtils.shaderMainMenu.unload();
        GlStateManager.disableBlend();
        int logoWidth = 100;
        int logoHeight = 100;
        int xLogo = (windowWidth - logoWidth) / 2;
        int yLogo = (windowHeight - logoHeight) / 2;
        int yOffset = 6;
        int[] yPositions = new int[]{-15, 30, 75, 120, 165};
        ResourceLocation[] logos = new ResourceLocation[]{new ResourceLocation("expensive/images/single.png"), new ResourceLocation("expensive/images/multi.png"), new ResourceLocation("expensive/images/setting.png"), new ResourceLocation("expensive/images/exit.png"), new ResourceLocation("expensive/images/plus2.png")};
        for (int i = 0; i < logos.length; ++i) {
            int logoX = xLogo - 20;
            int logoY = yLogo + yPositions[i];
            this.currentY[i] = logoY;
            boolean isHovered = MathUtil.isHovered(mouseX, mouseY, logoX, (int)this.currentY[i], 32.0f, 32.0f);
            int backgroundColor = isHovered ? ColorUtils.rgba(255, 255, 255, 255) : ColorUtils.rgba(20, 20, 20, 255);
            DisplayUtils.drawRoundedRect((float)(logoX - 5), (float)((int)this.currentY[i]), (float)(logoWidth + 30), (float)(logoHeight - 60), new Vector4f(5.0f, 5.0f, 5.0f, 5.0f), new Vector4i(backgroundColor, backgroundColor, backgroundColor, backgroundColor));
            DisplayUtils.drawRoundedRect((float)(logoX - 5), (float)((int)this.currentY[i]), (float)(logoWidth - 59), (float)(logoHeight - 60), new Vector4f(5.0f, 5.0f, 5.0f, 5.0f), new Vector4i(ColorUtils.getColor(0), ColorUtils.getColor(90), ColorUtils.getColor(180), ColorUtils.getColor(270)));
            DisplayUtils.drawImage(logos[i], (float)logoX, (float)((int)this.currentY[i] + 4), 32.0f, 32.0f, -1);
        }
        DisplayUtils.drawRoundedRect((float)(xLogo - 70), (float)yLogo - 30.0f, 230.0f, 250.0f, 10.0f, ColorUtils.rgba(0, 0, 0, 50));
        float textX = xLogo - 160;
        float textY = yLogo;
        float centerX = (float)windowWidth / 2.0f;
        float textOffsetX = 160.0f;
        float textStartY = yLogo;
        this.drawGradientText(stack, "SinglePlayer", centerX - textOffsetX - 310.0f, textStartY);
        this.drawGradientText(stack, "MultiPlayer", centerX - textOffsetX - 310.0f, textStartY + 45.0f);
        this.drawGradientText(stack, "Options", centerX - textOffsetX - 310.0f, textStartY + 90.0f);
        this.drawGradientText(stack, "Exit", centerX - textOffsetX - 310.0f, textStartY + 135.0f);
        this.drawGradientText(stack, "AltManager", centerX - textOffsetX - 310.0f, textStartY + 180.0f);
        MainScreen.mc.gameRenderer.setupOverlayRendering(2);
        KawaseBlur.blur.updateBlur(3.0f, 4);
        this.drawButtons(stack, mouseX, mouseY, partialTicks);
        MainScreen.mc.gameRenderer.setupOverlayRendering(2);
    }

    private void drawGradientText(MatrixStack stack, String text, float x, float y) {
        int[] gradientColors = new int[]{ColorUtils.getColor(90), ColorUtils.getColor(180), ColorUtils.getColor(270), ColorUtils.getColor(360)};
        int textLength = text.length();
        float totalTextWidth = 0.0f;
        for (int i = 0; i < textLength; ++i) {
            char c = text.charAt(i);
            String charStr = String.valueOf(c);
            totalTextWidth += Fonts.sfMedium.getWidth(charStr, 9.0f, 0.04f) * 0.8f;
        }
        float startX = x + ((float)this.width - totalTextWidth) / 2.0f;
        for (int i = 0; i < textLength; ++i) {
            float relativePosition = (float)i / (float)textLength;
            int colorToUse = this.interpolateColor(gradientColors, relativePosition);
            char c = text.charAt(i);
            String charStr = String.valueOf(c);
            Fonts.sfMedium.drawText(stack, charStr, startX, y, colorToUse, 9.0f, 0.04f);
            startX += Fonts.sfMedium.getWidth(charStr, 9.0f, 0.04f) * 0.8f;
        }
    }

    private int interpolateColor(int[] colors, float position) {
        int numColors = colors.length;
        float segmentLength = 1.0f / (float)(numColors - 1);
        int segment = (int)(position / segmentLength);
        float segmentPosition = position % segmentLength / segmentLength;
        int startColor = colors[segment];
        int endColor = colors[Math.min(segment + 1, numColors - 1)];
        return this.interpolateColors(startColor, endColor, segmentPosition);
    }

    private int interpolateColors(int colorA, int colorB, float ratio) {
        int a = colorA >> 24 & 0xFF;
        int r = colorA >> 16 & 0xFF;
        int g = colorA >> 8 & 0xFF;
        int b = colorA & 0xFF;
        int a2 = colorB >> 24 & 0xFF;
        int r2 = colorB >> 16 & 0xFF;
        int g2 = colorB >> 8 & 0xFF;
        int b2 = colorB & 0xFF;
        int newA = (int)((float)a + (float)(a2 - a) * ratio);
        int newR = (int)((float)r + (float)(r2 - r) * ratio);
        int newG = (int)((float)g + (float)(g2 - g) * ratio);
        int newB = (int)((float)b + (float)(b2 - b) * ratio);
        return newA << 24 | newR << 16 | newG << 8 | newB;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Vec2i fixed = ClientUtil.getMouse((int)mouseX, (int)mouseY);
        this.buttons.forEach(b -> b.click(fixed.getX(), fixed.getY(), button));
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void drawButtons(MatrixStack stack, int mX, int mY, float pt) {
        this.buttons.forEach(b -> b.render(stack, mX, mY, pt));
    }

    private static class Button {
        private final float x;
        private final float y;
        private final float width;
        private final float height;
        private String text;
        private Runnable action;

        public void render(MatrixStack stack, int mouseX, int mouseY, float pt) {
            Stencil.initStencilToWrite();
            DisplayUtils.drawRoundedRect(this.x, this.y + 2.0f, this.width, this.height, 5.0f, ColorUtils.rgba(0, 0, 0, 10));
            DisplayUtils.drawRoundedRect(this.x, this.y, this.width, this.height, new Vector4f(4.0f, 4.0f, 4.0f, 4.0f), ColorUtils.rgba(10, 10, 10, 250));
            Stencil.readStencilBuffer(1);
            Stencil.uninitStencilBuffer();
            int color = ColorUtils.rgba(0, 0, 0, 255);
            if (MathUtil.isHovered(mouseX, mouseY, this.x, this.y, this.width, this.height)) {
                color = ColorUtils.rgba(255, 255, 255, 240);
            }
            Fonts.sfMedium.drawGradientText(stack, this.text, this.x + this.width / 2.0f - 33.0f, this.y + this.height / 2.0f - 7.5f + 4.0f, color, color, 12, 0.1f);
        }

        public void click(int mouseX, int mouseY, int button) {
            if (MathUtil.isHovered(mouseX, mouseY, this.x, this.y, this.width, this.height)) {
                this.action.run();
            }
        }

        public Button(MainScreen var11, float x, float y, float width, float height, String text, Runnable action) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.text = text;
            this.action = action;
        }

        public float getX() {
            return this.x;
        }

        public float getY() {
            return this.y;
        }

        public float getWidth() {
            return this.width;
        }

        public float getHeight() {
            return this.height;
        }
    }

    private static class Particle {
        private final float x = ThreadLocalRandom.current().nextInt(0, IMinecraft.mc.getMainWindow().getScaledWidth());
        private float y = 0.0f;
        private float size = 0.0f;

        public Particle(MainScreen var11) {
        }

        public void update() {
            this.y += 1.0f;
        }

        public void render(MatrixStack stack) {
            this.size += 0.1f;
            GlStateManager.pushMatrix();
            GlStateManager.translated((double)this.x + Math.sin((float)System.nanoTime() / 1.0E9f) * 5.0, this.y, 0.0);
            GlStateManager.rotatef(this.size * 20.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.translated(-((double)this.x + Math.sin((float)System.nanoTime() / 1.0E9f) * 5.0), -this.y, 0.0);
            float multi = 1.0f - MathHelper.clamp(this.y / (float)IMinecraft.mc.getMainWindow().getScaledHeight(), 0.0f, 1.0f);
            this.y += 1.0f;
            Fonts.damage.drawText(stack, "A", (float)((double)this.x + Math.sin((float)System.nanoTime() / 1.0E9f) * 5.0), this.y, -1, MathHelper.clamp(this.size * multi, 0.0f, 9.0f));
            GlStateManager.popMatrix();
            if (this.y >= (float)IMinecraft.mc.getMainWindow().getScaledHeight()) {
                particles.remove(this);
            }
        }
    }
}

