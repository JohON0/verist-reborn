/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.dropdown;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import im.expensive.Expensive;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.FunctionRegistry;
import im.expensive.functions.impl.misc.Tyanka;
import im.expensive.functions.impl.render.ClickGui;
import im.expensive.functions.settings.impl.BooleanSetting;
import im.expensive.functions.settings.impl.ModeSetting;
import im.expensive.ui.dropdown.Panel;
import im.expensive.ui.dropdown.components.settings.SearchField;
import im.expensive.utils.CustomFramebuffer;
import im.expensive.utils.client.ClientUtil;
import im.expensive.utils.client.IMinecraft;
import im.expensive.utils.client.Vec2i;
import im.expensive.utils.math.MathUtil;
import im.expensive.utils.render.Cursors;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.KawaseBlur;
import im.expensive.utils.render.Scissor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.glfw.GLFW;
import ru.hogoshi.Animation;
import ru.hogoshi.util.Easings;

public class DropDown
extends Screen
implements IMinecraft {
    private final List<Panel> panels = new ArrayList<Panel>();
    private static Animation animation = new Animation();
    private float x;
    private float y = mc.getMainWindow().scaledHeight() + 24;
    boolean sorted = true;
    public SearchField searchField;
    public static float scale = 1.0f;
    private final ParticleManager particleManager = new ParticleManager();

    public DropDown(ITextComponent titleIn) {
        super(titleIn);
        for (Category category : Category.values()) {
            this.panels.add(new Panel(category));
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        animation = animation.animate(1.0, (double)0.45f, Easings.EXPO_OUT);
        super.init();
        int windowWidth = ClientUtil.calc(mc.getMainWindow().getScaledWidth());
        int windowHeight = ClientUtil.calc(mc.getMainWindow().getScaledHeight());
        float x = (float)windowWidth / 2.0f - (float)(this.panels.size() * 145) / 2.0f + 290.0f + 27.5f;
        float y = (float)windowHeight / 2.0f + 162.5f + 30.0f;
        this.searchField = new SearchField((int)x - 10, (int)y - 10, 90, 16, "\u041f\u043e\u0438\u0441\u043a...");
    }

    @Override
    public void closeScreen() {
        super.closeScreen();
        GLFW.glfwSetCursor(Minecraft.getInstance().getMainWindow().getHandle(), Cursors.ARROW);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        Vec2i fixMouse = this.adjustMouseCoordinates((int)mouseX, (int)mouseY);
        Vec2i fix = ClientUtil.getMouse(fixMouse.getX(), fixMouse.getY());
        mouseX = fix.getX();
        mouseY = fix.getY();
        for (Panel panel : this.panels) {
            if (!MathUtil.isHovered((float)mouseX, (float)mouseY, panel.getX(), panel.getY(), panel.getWidth(), panel.getHeight())) continue;
            panel.setScroll((float)((double)panel.getScroll() + delta * 20.0));
            this.sorted = true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        Iterator<Panel> var3 = this.panels.iterator();
        do {
            if (!var3.hasNext()) {
                return super.charTyped(codePoint, modifiers);
            }
            Panel panel = var3.next();
            panel.charTyped(codePoint, modifiers);
        } while (!this.searchField.charTyped(codePoint, modifiers));
        return true;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        BooleanSetting Blur;
        FunctionRegistry functionRegistry = Expensive.getInstance().getFunctionRegistry();
        ClickGui clickgui = functionRegistry.getClickGui();
        boolean clickguicreate = clickgui.isState();
        if (clickguicreate && ((Boolean)(Blur = clickgui.Blur).get()).booleanValue()) {
            KawaseBlur.blur.updateBlur(1.0f, 4);
            GlStateManager.bindTexture(KawaseBlur.blur.BLURRED.framebufferTexture);
            CustomFramebuffer.drawTexture();
        }
        animation.update();
        if (animation.getValue() < 0.1) {
            this.closeScreen();
        }
        float off = 30.0f;
        float width = (float)this.panels.size() * 135.0f;
        this.updateScaleBasedOnScreenWidth();
        int windowWidth = ClientUtil.calc(mc.getMainWindow().getScaledWidth());
        int windowHeight = ClientUtil.calc(mc.getMainWindow().getScaledHeight());
        Vec2i fixMouse = this.adjustMouseCoordinates(mouseX, mouseY);
        Vec2i fix = ClientUtil.getMouse(fixMouse.getX(), fixMouse.getY());
        mouseX = fix.getX();
        mouseY = fix.getY();
        float x = 40.0f;
        float y = 40.0f;
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float)windowWidth / 2.0f, (float)windowHeight / 2.0f, 0.0f);
        GlStateManager.scaled(animation.getValue(), animation.getValue(), 1.0);
        GlStateManager.scaled(scale, scale, 1.0);
        GlStateManager.translatef((float)(-windowWidth) / 2.0f, (float)(-windowHeight) / 2.0f, 0.0f);
        this.particleManager.render(matrixStack, partialTicks);
        Tyanka tyanka = functionRegistry.getTyanka();
        boolean tyankaState = tyanka.isState();
        ResourceLocation firstImage = new ResourceLocation("expensive/images/tay2.png");
        ResourceLocation secondImage = new ResourceLocation("expensive/images/tay3.png");
        ResourceLocation thirdImage = new ResourceLocation("expensive/images/tay4.png");
        if (tyankaState) {
            ModeSetting type33 = tyanka.type33;
            if (((String)type33.get()).equals("\u041f\u0435\u0440\u0432\u043e\u0435")) {
                DisplayUtils.drawImage(firstImage, x + 680.0f, (float)windowHeight / 2.0f - 60.0f, 400.0f, 400.0f, -1);
            } else if (((String)type33.get()).equals("\u0412\u0442\u043e\u0440\u043e\u0435")) {
                DisplayUtils.drawImage(secondImage, x + 680.0f, (float)windowHeight / 2.0f - 60.0f, 400.0f, 400.0f, -1);
            } else if (((String)type33.get()).equals("\u0422\u0440\u0435\u0442\u044c\u0435")) {
                DisplayUtils.drawImage(thirdImage, x + 680.0f, (float)windowHeight / 2.0f - 60.0f, 400.0f, 400.0f, -1);
            }
        }
        int newScreenWidth = mc.getMainWindow().getScaledWidth();
        int newScreenHeight = mc.getMainWindow().getScaledHeight();
        float panelWidth = 105.0f;
        float offsetX = ((float)newScreenWidth - (float)this.panels.size() * panelWidth) / 2.0f - 80.0f;
        for (int i = 0; i < this.panels.size(); ++i) {
            Panel panel = this.panels.get(i);
            panel.setX(offsetX + (float)i * (panelWidth + 30.0f));
            panel.setY((float)newScreenHeight / 2.0f - 160.0f);
            float animationValue = (float)animation.getValue() * scale;
            float halfAnimationValueRest = (1.0f - animationValue) / 2.0f;
            float testX = panel.getX() + panel.getWidth() * halfAnimationValueRest;
            float testY = panel.getY() + panel.getHeight() * halfAnimationValueRest;
            float testW = panel.getWidth() * animationValue;
            float testH = panel.getHeight() * animationValue;
            testX = testX * animationValue + ((float)windowWidth - testW) * halfAnimationValueRest;
            Scissor.push();
            Scissor.setFromComponentCoordinates(testX, testY, testW, testH - 0.5f);
            panel.render(matrixStack, mouseX, mouseY);
            Scissor.unset();
            Scissor.pop();
        }
        this.searchField.render(matrixStack, mouseX, mouseY, partialTicks);
        GlStateManager.popMatrix();
        DropDown.mc.gameRenderer.setupOverlayRendering();
    }

    private void updateScaleBasedOnScreenWidth() {
        float screenWidth;
        float PANEL_WIDTH = 115.0f;
        float MARGIN = 10.0f;
        float MIN_SCALE = 0.5f;
        float totalPanelWidth = (float)this.panels.size() * 125.0f;
        if (totalPanelWidth >= (screenWidth = (float)mc.getMainWindow().getScaledWidth())) {
            scale = screenWidth / totalPanelWidth;
            scale = MathHelper.clamp(scale, 0.5f, 1.0f);
        } else {
            scale = 1.0f;
        }
    }

    public boolean isSearching() {
        return !this.searchField.isEmpty();
    }

    public String getSearchText() {
        return this.searchField.getText();
    }

    public boolean searchCheck(String text) {
        return this.isSearching() && !text.replaceAll(" ", "").toLowerCase().contains(this.getSearchText().replaceAll(" ", "").toLowerCase());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        Iterator<Panel> var4 = this.panels.iterator();
        do {
            if (!var4.hasNext()) {
                if (keyCode == 256) {
                    animation = animation.animate(0.0, 0.25, Easings.EXPO_OUT);
                    return false;
                }
                return super.keyPressed(keyCode, scanCode, modifiers);
            }
            Panel panel = var4.next();
            panel.keyPressed(keyCode, scanCode, modifiers);
        } while (!this.searchField.keyPressed(keyCode, scanCode, modifiers));
        return true;
    }

    private Vec2i adjustMouseCoordinates(int mouseX, int mouseY) {
        int windowWidth = mc.getMainWindow().getScaledWidth();
        int windowHeight = mc.getMainWindow().getScaledHeight();
        float adjustedMouseX = ((float)mouseX - (float)windowWidth / 2.0f) / scale + (float)windowWidth / 2.0f;
        float adjustedMouseY = ((float)mouseY - (float)windowHeight / 2.0f) / scale + (float)windowHeight / 2.0f;
        return new Vec2i((int)adjustedMouseX, (int)adjustedMouseY);
    }

    private double pathX(float mouseX, float scale) {
        if (scale == 1.0f) {
            return mouseX;
        }
        int windowWidth = mc.getMainWindow().scaledWidth();
        int windowHeight = mc.getMainWindow().scaledHeight();
        mouseX /= scale;
        return mouseX -= (float)windowWidth / 2.0f - (float)windowWidth / 2.0f * scale;
    }

    private double pathY(float mouseY, float scale) {
        if (scale == 1.0f) {
            return mouseY;
        }
        int windowWidth = mc.getMainWindow().scaledWidth();
        int windowHeight = mc.getMainWindow().scaledHeight();
        mouseY /= scale;
        return mouseY -= (float)windowHeight / 2.0f - (float)windowHeight / 2.0f * scale;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Vec2i fixMouse = this.adjustMouseCoordinates((int)mouseX, (int)mouseY);
        Vec2i fix = ClientUtil.getMouse(fixMouse.getX(), fixMouse.getY());
        if (this.searchField.mouseClicked(mouseX = (double)fix.getX(), mouseY = (double)fix.getY(), button)) {
            return true;
        }
        for (Panel panel : this.panels) {
            panel.mouseClick((float)mouseX, (float)mouseY, button);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        Vec2i fixMouse = this.adjustMouseCoordinates((int)mouseX, (int)mouseY);
        Vec2i fix = ClientUtil.getMouse(fixMouse.getX(), fixMouse.getY());
        mouseX = fix.getX();
        mouseY = fix.getY();
        for (Panel panel : this.panels) {
            panel.mouseRelease((float)mouseX, (float)mouseY, button);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public static Animation getAnimation() {
        return animation;
    }

    class ParticleManager {
        private final List<Particle5> particles = new ArrayList<Particle5>();
        private final Random random = new Random();

        ParticleManager() {
        }

        public void update() {
            this.particles.forEach(Particle5::update);
            this.particles.removeIf(p -> p.y > (float)IMinecraft.mc.getMainWindow().getScaledHeight());
            if ((double)this.random.nextFloat() < 0.1) {
                this.particles.add(new Particle5(this.random.nextInt(IMinecraft.mc.getMainWindow().getScaledWidth()), -16.0f));
            }
        }

        public void render(MatrixStack matrixStack, float partialTicks) {
            BooleanSetting Xoxo;
            FunctionRegistry functionRegistry = Expensive.getInstance().getFunctionRegistry();
            ClickGui clickgui = functionRegistry.getClickGui();
            boolean clickguicreate = clickgui.isState();
            if (clickguicreate && ((Boolean)(Xoxo = clickgui.Xoxo).get()).booleanValue()) {
                this.update();
                this.particles.forEach(p -> p.render(matrixStack));
            }
        }
    }

    class Particle5 {
        private float x;
        private float y;
        private final float speedX;
        private final float speedY;
        private float rotation;
        private final ResourceLocation texture = new ResourceLocation("expensive/images/star.png");

        public Particle5(float x, float y) {
            this.x = x;
            this.y = y;
            this.rotation = 0.0f;
            Random rand = new Random();
            this.speedX = (rand.nextFloat() - 0.5f) * 2.0f;
            this.speedY = rand.nextFloat() + 0.5f;
        }

        public void update() {
            this.x += this.speedX;
            this.y += this.speedY;
            this.rotation += 5.0f;
        }

        public void render(MatrixStack matrixStack) {
            GlStateManager.pushMatrix();
            GlStateManager.translatef(this.x + 8.0f, this.y + 8.0f, 0.0f);
            GlStateManager.rotatef(this.rotation, 0.0f, 0.0f, 1.0f);
            GlStateManager.translatef(-8.0f, -8.0f, 0.0f);
            DisplayUtils.drawImage(this.texture, 0.0f, 0.0f, 16.0f, 16.0f, -1);
            GlStateManager.popMatrix();
        }
    }

    class Particle {
        private float x;
        private float y;
        private final float speedX;
        private final float speedY;
        private final ResourceLocation texture = new ResourceLocation("expensive/images/star.png");

        public Particle(float x, float y) {
            this.x = x;
            this.y = y;
            Random rand = new Random();
            this.speedX = (rand.nextFloat() - 0.5f) * 2.0f;
            this.speedY = rand.nextFloat() + 0.5f;
        }

        public void update() {
            this.x += this.speedX;
            this.y += this.speedY;
        }

        public void render(MatrixStack matrixStack) {
            DisplayUtils.drawImage(this.texture, this.x, this.y, 16.0f, 16.0f, -1);
        }
    }
}

