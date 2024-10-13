/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.impl.misc;

import im.expensive.Expensive;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.utils.math.StopWatch;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributeView;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.GameConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import net.optifine.shaders.Shaders;
import org.apache.commons.lang3.RandomStringUtils;

@FunctionRegister(name="UnHooked", type=Category.Misc)
public class SelfDestruct
extends Function {
    public boolean unhooked = false;
    public String secret = RandomStringUtils.randomAlphabetic(2);
    public StopWatch stopWatch = new StopWatch();
    public List<Function> saved = new ArrayList<Function>();

    @Override
    public boolean onEnable() {
        super.onEnable();
        this.process();
        this.print("\u0427\u0442\u043e \u0431\u044b \u0432\u0435\u0440\u043d\u0443\u0442\u044c \u0447\u0438\u0442 \u043d\u0430\u043f\u0438\u0448\u0438\u0442\u0435 \u0432 \u0447\u0430\u0442 " + TextFormatting.RED + this.secret);
        this.print("\u0412\u0441\u0435 \u0441\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u044f \u0443\u0434\u0430\u043b\u044f\u0442\u0441\u044f \u0447\u0435\u0440\u0435\u0437 10 \u0441\u0435\u043a\u0443\u043d\u0434");
        this.stopWatch.reset();
        new Thread(() -> {
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            SelfDestruct.mc.ingameGUI.getChatGUI().clearChatMessages(false);
            this.toggle();
        }).start();
        this.unhooked = true;
        return false;
    }

    public void process() {
        for (Function function : Expensive.getInstance().getFunctionRegistry().getFunctions()) {
            if (function == this || !function.isState()) continue;
            this.saved.add(function);
            function.setState(false, false);
        }
        SelfDestruct.mc.fileResourcepacks = new File(System.getenv("appdata") + "\\.tlauncher\\legacy\\Minecraft\\game\\resourcepacks");
        Shaders.shaderPacksDir = new File(System.getenv("appdata") + "\\.tlauncher\\legacy\\Minecraft\\game\\shaderpacks");
        File folder = new File("C:\\Verist");
        this.hiddenFolder(folder, true);
    }

    public void hook() {
        for (Function function : this.saved) {
            if (function == this || function.isState()) continue;
            function.setState(true, false);
        }
        File folder = new File("C:\\Verist");
        this.hiddenFolder(folder, false);
        SelfDestruct.mc.fileResourcepacks = GameConfiguration.instance.folderInfo.resourcePacksDir;
        Shaders.shaderPacksDir = new File(Minecraft.getInstance().gameDir, "shaderpacks");
        this.unhooked = false;
    }

    private void hiddenFolder(File folder, boolean hide) {
        if (folder.exists()) {
            try {
                Path folderPathObj = folder.toPath();
                DosFileAttributeView attributes = Files.getFileAttributeView(folderPathObj, DosFileAttributeView.class, new LinkOption[0]);
                attributes.setHidden(false);
            } catch (IOException e) {
                System.out.println("\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0441\u043a\u0440\u044b\u0442\u044c \u043f\u0430\u043f\u043a\u0443: " + e.getMessage());
            }
        }
    }
}

