/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sun.java.accessibility.util.AWTEventMonitor
 */
package im.expensive.functions.impl.render;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.sun.java.accessibility.util.AWTEventMonitor;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

@FunctionRegister(name="DiscordRPC", type=Category.Render)
public class RPC
extends Function {
    private static boolean isAppActive = true;
    private static final DiscordRPC discordRPC = DiscordRPC.INSTANCE;
    private static final String discordID = "1190380952640294962";
    private static final DiscordRichPresence discordRichPresence = new DiscordRichPresence();

    @Override
    public boolean onEnable() {
        super.onEnable();
        RPC.startRPC();
        AWTEventMonitor.addWindowListener((WindowListener)new WindowAdapter(){

            @Override
            public void windowIconified(WindowEvent e) {
                isAppActive = false;
                RPC.updateRPCText();
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                isAppActive = true;
                RPC.updateRPCText();
            }
        });
        return false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        RPC.stopRPC();
    }

    public static void stopRPC() {
        discordRPC.Discord_Shutdown();
    }

    public static void startRPC() {
        DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
        discordRPC.Discord_Initialize(discordID, eventHandlers, true, null);
        RPC.discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
        RPC.discordRichPresence.largeImageKey = "https://cdn.pixabay.com/animation/2023/02/19/11/34/11-34-33-86_512.gif";
        RPC.discordRichPresence.largeImageText = "Verist.XXX";
        new Thread(() -> {
            while (true) {
                RPC.updateRPCText();
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException interruptedException) {
                }
            }
        }).start();
    }

    private static void updateRPCText() {
        if (isAppActive) {
            RPC.discordRichPresence.details = "\ud835\udc7d\ud835\udc86\ud835\udc93\ud835\udc94\ud835\udc8a\ud835\udc90\ud835\udc8f -> \ud835\udc6d\ud835\udc93\ud835\udc86\ud835\udc86";
            RPC.discordRichPresence.state = "\ud835\udc69\ud835\udc96\ud835\udc8a\ud835\udc8d\ud835\udc85 -> 2.2";
        } else {
            RPC.discordRichPresence.details = "Version -> Free";
            RPC.discordRichPresence.state = "Build -> 2.2";
        }
        discordRPC.Discord_UpdatePresence(discordRichPresence);
    }
}

