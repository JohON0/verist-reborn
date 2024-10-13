/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.mainmenu;

import com.mojang.blaze3d.matrix.MatrixStack;
import im.expensive.ui.mainmenu.Account;
import im.expensive.ui.mainmenu.AltConfig;
import im.expensive.utils.ScaleMath;
import im.expensive.utils.animations.Animation;
import im.expensive.utils.client.IMinecraft;
import im.expensive.utils.client.Vec2i;
import im.expensive.utils.math.MathUtil;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.Scissor;
import im.expensive.utils.render.StencilUtil;
import im.expensive.utils.render.font.Fonts;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Session;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import org.apache.commons.lang3.RandomStringUtils;

public class AltManager
extends Screen {
    public ArrayList<Account> accounts = new ArrayList();
    public float scroll;
    public float scrollAn;
    private String altName = "";
    private boolean typing = false;

    public AltManager() {
        super(new StringTextComponent(""));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.scrollAn = Animation.lerp(this.scrollAn, this.scroll, 5.0f);
        IMinecraft.mc.gameRenderer.setupOverlayRendering(2);
        float offset = 6.0f;
        float width = 250.0f;
        float height = 270.0f;
        float x = (float)IMinecraft.mc.getMainWindow().getScaledWidth() / 2.0f - width / 2.0f;
        float y = (float)IMinecraft.mc.getMainWindow().getScaledHeight() / 2.0f - height / 2.0f;
        DisplayUtils.drawRoundedRect(x - offset, y - offset, width + offset * 2.0f, height + offset * 2.0f, 5.0f, ColorUtils.rgba(22, 22, 22, 215));
        Fonts.sfMedium.drawText(matrixStack, "\u0414\u043e\u0431\u0430\u0432\u044c\u0442\u0435 \u043d\u043e\u0432\u044b\u0439 \u0430\u043a\u043a\u0430\u0443\u043d\u0442!", x + offset + 60.0f, y + offset, -1, 8.0f);
        Fonts.sfMedium.drawText(matrixStack, "\u0421\u043c\u0435\u043d\u0438 \u043d\u0438\u043a\u043d\u0435\u0439\u043c, \u0438 \u043e\u0442\u043f\u0440\u0430\u0432\u043b\u044f\u0439\u0441\u044f \u0432 \u0438\u0433\u0440\u0443!", x + offset, y + offset + 16.0f, -1, 8.0f);
        DisplayUtils.drawRoundedRect(x + offset, y + offset + 25.0f, width - offset * 2.0f, 20.0f, 2.0f, ColorUtils.rgba(60, 60, 60, 215));
        Scissor.push();
        Scissor.setFromComponentCoordinates(x + offset, y + offset + 25.0f, width - offset * 2.0f, 20.0);
        Fonts.sfMedium.drawText(matrixStack, (String)(this.typing ? this.altName + (System.currentTimeMillis() / 500L % 2L == 0L ? "|" : "") : (this.altName.isEmpty() ? "\u0412\u0432\u043e\u0434 \u0434\u043b\u044f \u043d\u0438\u043a\u0430" : this.altName)), x + offset + 2.0f, y + offset + 31.5f, -1, 9.0f);
        Scissor.unset();
        Scissor.pop();
        Fonts.sfMedium.drawText(matrixStack, "?", x + width - offset - 12.5f, y + offset + 31.0f, -1, 8.0f);
        Fonts.sfMedium.drawText(matrixStack, "\u0410\u043a\u043a\u0430\u0443\u043d\u0442\u044b:", x + offset, y + offset + 60.0f, -1, 8.0f);
        Fonts.sfMedium.drawText(matrixStack, "\u0412\u044b\u0431\u0435\u0440\u0438 \u0430\u043a\u043a\u0430\u0443\u043d\u0442 \u0438\u0437 \u0441\u043f\u0438\u0441\u043a\u0430!", x + offset, y + offset + 70.0f, ColorUtils.rgb(180, 180, 180), 8.0f);
        DisplayUtils.drawRoundedRect(x + offset, y + offset + 80.0f, width - offset * 2.0f, 177.5f, 2.0f, ColorUtils.rgba(60, 60, 60, 215));
        float size = 0.0f;
        float iter = this.scrollAn;
        float offsetAccounts = 0.0f;
        Scissor.push();
        Scissor.setFromComponentCoordinates(x + offset, y + offset + 80.0f, width - offset * 2.0f, 177.5);
        for (Account account : this.accounts) {
            float scrollY = y + iter * 22.0f;
            DisplayUtils.drawRoundedRect(x + offset + 2.0f, scrollY + offset + 82.0f + offsetAccounts, width - offset * 2.0f - 4.0f, 20.0f, 2.0f, ColorUtils.rgb(101, 101, 101));
            Fonts.sfMedium.drawText(matrixStack, account.accountName, x + offset + 25.0f, scrollY + offset + 82.0f + 8.0f + offsetAccounts, -1, 8.0f);
            StencilUtil.initStencilToWrite();
            DisplayUtils.drawRoundedRect(x + offset + 4.0f + 0.5f, scrollY + offset + 84.0f + offsetAccounts, 16.0f, 16.0f, 2.0f, Color.BLACK.getRGB());
            StencilUtil.readStencilBuffer(1);
            IMinecraft.mc.getTextureManager().bindTexture(account.skin);
            AbstractGui.drawScaledCustomSizeModalRect(x + offset + 4.0f + 0.5f, scrollY + offset + 84.0f + offsetAccounts, 8.0f, 8.0f, 8.0f, 8.0f, 16.0f, 16.0f, 64.0f, 64.0f);
            StencilUtil.uninitStencilBuffer();
            iter += 1.0f;
            size += 1.0f;
        }
        this.scroll = MathHelper.clamp(this.scroll, size > 8.0f ? -size + 4.0f : 0.0f, 0.0f);
        Scissor.unset();
        Scissor.pop();
        Fonts.sfMedium.drawText(matrixStack, "\u0412\u0430\u0448 \u043d\u0438\u043a - " + IMinecraft.mc.getSession().getUsername() + ".", x + offset, y + height - offset / 2.0f, ColorUtils.rgb(180, 180, 180), 8.0f);
        IMinecraft.mc.gameRenderer.setupOverlayRendering();
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 259 && !this.altName.isEmpty() && this.typing) {
            this.altName = this.altName.substring(0, this.altName.length() - 1);
        }
        if (keyCode == 257) {
            if (!this.altName.isEmpty()) {
                this.accounts.add(new Account(this.altName));
                AltConfig.updateFile();
            }
            this.typing = false;
            this.altName = "";
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (this.typing && this.altName.length() <= 20) {
            this.altName = this.altName + Character.toString(codePoint);
        }
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        float y;
        Vec2i fixed = ScaleMath.getMouse((int)mouseX, (int)mouseY);
        mouseX = fixed.getX();
        mouseY = fixed.getY();
        float offset = 6.0f;
        float width = 250.0f;
        float height = 270.0f;
        float x = (float)IMinecraft.mc.getMainWindow().getScaledWidth() / 2.0f - width / 2.0f;
        if (MathUtil.isHovered((float)mouseX, (float)mouseY, x + width - offset - 12.5f, (y = (float)IMinecraft.mc.getMainWindow().getScaledHeight() / 2.0f - height / 2.0f) + offset + 31.0f, 10.0f, 22.0f)) {
            this.accounts.add(new Account(RandomStringUtils.randomAlphabetic(8)));
            AltConfig.updateFile();
        }
        if (MathUtil.isHovered((float)mouseX, (float)mouseY, x + offset, y + offset + 25.0f, width - offset * 2.0f, 20.0f) && !MathUtil.isHovered((float)mouseX, (float)mouseY, x + width - offset - 12.5f, y + offset + 31.0f, 22.0f, 22.0f)) {
            this.typing = !this.typing;
        }
        float iter = this.scrollAn;
        float offsetAccounts = 0.0f;
        Iterator<Account> iterator2 = this.accounts.iterator();
        while (iterator2.hasNext()) {
            Account account = iterator2.next();
            float scrollY = y + iter * 22.0f;
            if (MathUtil.isHovered((float)mouseX, (float)mouseY, x + offset + 2.0f, scrollY + offset + 82.0f + offsetAccounts, width - offset * 2.0f - 4.0f, 20.0f)) {
                if (button == 0) {
                    IMinecraft.mc.session = new Session(account.accountName, "", "", "mojang");
                    AltConfig.updateFile();
                } else if (button == 1) {
                    iterator2.remove();
                    AltConfig.updateFile();
                }
            }
            iter += 1.0f;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        float y;
        Vec2i fixed = ScaleMath.getMouse((int)mouseX, (int)mouseY);
        mouseX = fixed.getX();
        mouseY = fixed.getY();
        float offset = 6.0f;
        float width = 250.0f;
        float height = 270.0f;
        float x = (float)IMinecraft.mc.getMainWindow().getScaledWidth() / 2.0f - width / 2.0f;
        if (MathUtil.isHovered((float)mouseX, (float)mouseY, x + offset, (y = (float)IMinecraft.mc.getMainWindow().getScaledHeight() / 2.0f - height / 2.0f) + offset + 80.0f, width - offset * 2.0f, 177.5f)) {
            this.scroll = (float)((double)this.scroll + delta * 1.0);
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
    }

    @Override
    public void tick() {
        super.tick();
    }
}

