/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils.drag;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import im.expensive.utils.drag.Dragging;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.client.Minecraft;

public class DragManager {
    public static final Logger logger = Logger.getLogger(DragManager.class.getName());
    public static LinkedHashMap<String, Dragging> draggables = new LinkedHashMap();
    public static final File DRAG_DATA = new File(Minecraft.getInstance().gameDir, "verist/files/drags.cfg");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

    public static void save() {
        if (!DRAG_DATA.getParentFile().exists()) {
            DRAG_DATA.getParentFile().mkdirs();
        }
        try (FileWriter writer = new FileWriter(DRAG_DATA);){
            writer.write(GSON.toJson(draggables.values()));
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Failed to save drag data", ex);
        }
    }

    public static void load() {
        if (!DRAG_DATA.exists()) {
            return;
        }
        try {
            Dragging[] draggings;
            for (Dragging dragging : draggings = GSON.fromJson(Files.readString((Path)DRAG_DATA.toPath()), Dragging[].class)) {
                if (dragging == null) continue;
                Dragging currentDrag = draggables.get(dragging.getName());
                if (currentDrag != null) {
                    currentDrag.setX(dragging.getX());
                    currentDrag.setY(dragging.getY());
                    continue;
                }
                draggables.put(dragging.getName(), dragging);
            }
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Failed to load drag data", ex);
        }
    }
}

