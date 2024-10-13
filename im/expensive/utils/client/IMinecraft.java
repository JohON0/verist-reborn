/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils.client;

import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.text.GradientUtil;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public interface IMinecraft {
    public static final Minecraft mc = Minecraft.getInstance();
    public static final MainWindow window = mc.getMainWindow();
    public static final BufferBuilder buffer = Tessellator.getInstance().getBuffer();
    public static final Tessellator tessellator = Tessellator.getInstance();
    public static final List<ITextComponent> clientMessages = new ArrayList<ITextComponent>();

    default public void print(String input) {
        if (Minecraft.player == null) {
            return;
        }
        IFormattableTextComponent text = GradientUtil.gradient("Verist Free", ColorUtils.rgba(255, 0, 0, 255), ColorUtils.rgba(0, 255, 0, 255)).append(new StringTextComponent(TextFormatting.DARK_GRAY + " : " + TextFormatting.RESET + input));
        clientMessages.add(text);
        IMinecraft.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(text, 0);
    }
}

