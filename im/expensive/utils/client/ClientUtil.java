/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.utils.client;

import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import im.expensive.utils.client.IMinecraft;
import im.expensive.utils.client.Vec2i;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.UUID;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SUpdateBossInfoPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

public final class ClientUtil
implements IMinecraft {
    private static Clip currentClip = null;
    private static boolean pvpMode;
    private static UUID uuid;
    private static final String DISCORD_APP_ID = "1190380952640294962";
    private static final DiscordRPC discordRPC;

    public static void updateBossInfo(SUpdateBossInfoPacket packet) {
        if (packet.getOperation() == SUpdateBossInfoPacket.Operation.ADD) {
            if (StringUtils.stripControlCodes(packet.getName().getString()).toLowerCase().contains("pvp")) {
                pvpMode = true;
                uuid = packet.getUniqueId();
            }
        } else if (packet.getOperation() == SUpdateBossInfoPacket.Operation.REMOVE && packet.getUniqueId().equals(uuid)) {
            pvpMode = false;
        }
    }

    public static boolean isConnectedToServer(String ip) {
        return mc.getCurrentServerData() != null && ClientUtil.mc.getCurrentServerData().serverIP != null && ClientUtil.mc.getCurrentServerData().serverIP.contains(ip);
    }

    public static boolean isPvP() {
        return pvpMode;
    }

    public static void updateDiscordPresence(String state, String details) {
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000L;
        presence.state = state;
        presence.details = details;
        discordRPC.Discord_UpdatePresence(presence);
    }

    public static void playSound(String sound, float value, boolean nonstop) {
        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();
        }
        try {
            currentClip = AudioSystem.getClip();
            InputStream is = mc.getResourceManager().getResource(new ResourceLocation("expensive/sounds/" + sound + ".wav")).getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bis);
            if (audioInputStream == null) {
                System.out.println("Sound not found!");
                return;
            }
            currentClip.open(audioInputStream);
            currentClip.start();
            FloatControl floatControl = (FloatControl)currentClip.getControl(FloatControl.Type.MASTER_GAIN);
            float min2 = floatControl.getMinimum();
            float max2 = floatControl.getMaximum();
            float volumeInDecibels = (float)((double)min2 * (1.0 - (double)value / 100.0) + (double)max2 * ((double)value / 100.0));
            floatControl.setValue(volumeInDecibels);
            if (nonstop) {
                currentClip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        currentClip.setFramePosition(0);
                        currentClip.start();
                    }
                });
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void playSound1(String sound, float value, boolean nonstop) {
        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();
        }
        try {
            currentClip = AudioSystem.getClip();
            InputStream is = mc.getResourceManager().getResource(new ResourceLocation("expensive/sounds/clientSounds/enable.wav")).getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bis);
            if (audioInputStream == null) {
                System.out.println("Sound not found!");
                return;
            }
            currentClip.open(audioInputStream);
            currentClip.start();
            FloatControl floatControl = (FloatControl)currentClip.getControl(FloatControl.Type.SAMPLE_RATE);
            float min2 = floatControl.getMinimum();
            float max2 = floatControl.getMaximum();
            float volumeInDecibels = (float)((double)min2 * (1.0 - (double)value / 100.0) + (double)max2 * ((double)value / 100.0));
            floatControl.setValue(volumeInDecibels);
            if (nonstop) {
                currentClip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        currentClip.setFramePosition(5);
                        currentClip.start();
                    }
                });
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void stopSound() {
        if (currentClip != null) {
            currentClip.stop();
            currentClip.close();
            currentClip = null;
        }
    }

    public static int calc(int value) {
        MainWindow rs = mc.getMainWindow();
        return (int)((double)value * rs.getGuiScaleFactor() / 2.0);
    }

    public static Vec2i getMouse(int mouseX, int mouseY) {
        return new Vec2i((int)((double)mouseX * Minecraft.getInstance().getMainWindow().getGuiScaleFactor() / 2.0), (int)((double)mouseY * Minecraft.getInstance().getMainWindow().getGuiScaleFactor() / 2.0));
    }

    private ClientUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        discordRPC = DiscordRPC.INSTANCE;
        discordRPC.Discord_Initialize(DISCORD_APP_ID, null, true, null);
    }
}

