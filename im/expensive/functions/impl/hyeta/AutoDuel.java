//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package im.expensive.functions.impl.hyeta;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.mojang.authlib.GameProfile;
import im.expensive.events.EventPacket;
import im.expensive.events.EventUpdate;
import im.expensive.functions.api.Category;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegister;
import im.expensive.functions.settings.Setting;
import im.expensive.functions.settings.impl.ModeSetting;
import im.expensive.utils.math.StopWatch;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SChatPacket;

@FunctionRegister(
        name = "AutoDuel",
        type = Category.Misc
)
public class AutoDuel extends Function {
    private static final Pattern pattern = Pattern.compile("^\\w{3,16}$");
    private final ModeSetting mode = new ModeSetting("Mode", "Шары", new String[]{"Шары", "Щит", "Шипы 3", "Незеритка", "Читерский рай", "Лук", "Классик", "Тотемы", "Нодебафф"});
    private double lastPosX;
    private double lastPosY;
    private double lastPosZ;
    private final List<String> sent = Lists.newArrayList();
    private final StopWatch counter = new StopWatch();
    private final StopWatch counter2 = new StopWatch();
    private final StopWatch counterChoice = new StopWatch();
    private final StopWatch counterTo = new StopWatch();

    public AutoDuel() {
        this.addSettings(new Setting[]{this.mode});
    }

    @Subscribe
    private void onUpdt(EventUpdate e) {
        List<String> players = this.getOnlinePlayers();
        Minecraft var10001 = mc;
        double var10000 = Math.pow(this.lastPosX - Minecraft.player.getPosX(), 2.0);
        Minecraft var10002 = mc;
        var10000 += Math.pow(this.lastPosY - Minecraft.player.getPosY(), 2.0);
        var10002 = mc;
        double distance = Math.sqrt(var10000 + Math.pow(this.lastPosZ - Minecraft.player.getPosZ(), 2.0));
        if (distance > 500.0) {
            this.toggle();
        }

        var10001 = mc;
        this.lastPosX = Minecraft.player.getPosX();
        var10001 = mc;
        this.lastPosY = Minecraft.player.getPosY();
        var10001 = mc;
        this.lastPosZ = Minecraft.player.getPosZ();
        if (this.counter2.isReached(800L * (long)players.size())) {
            this.sent.clear();
            this.counter2.reset();
        }

        Iterator var5 = players.iterator();

        Minecraft var12;
        while(var5.hasNext()) {
            String player = (String)var5.next();
            if (!this.sent.contains(player) && !player.equals(mc.session.getProfile().getName()) && this.counter.isReached(1000L)) {
                var12 = mc;
                Minecraft.player.sendChatMessage("/duel " + player);
                this.sent.add(player);
                this.counter.reset();
            }
        }

        var12 = mc;
        Container var10 = Minecraft.player.openContainer;
        if (var10 instanceof ChestContainer chest) {
            Minecraft var10005;
            if (mc.currentScreen.getTitle().getString().contains("Выбор набора (1/1)")) {
                for(int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); ++i) {
                    List<Integer> slotsID = new ArrayList();
                    int index = 0;
                    slotsID.add(index);
                    ++index;
                    Collections.shuffle(slotsID);
                    if (this.counterChoice.isReached(150L)) {
                        if (this.mode.is("Щит")) {
                            var10005 = mc;
                            mc.playerController.windowClick(chest.windowId, 0, 0, ClickType.QUICK_MOVE, Minecraft.player);
                        }

                        if (this.mode.is("Шипы 3")) {
                            var10005 = mc;
                            mc.playerController.windowClick(chest.windowId, 1, 0, ClickType.QUICK_MOVE, Minecraft.player);
                        }

                        if (this.mode.is("Лук")) {
                            var10005 = mc;
                            mc.playerController.windowClick(chest.windowId, 2, 0, ClickType.QUICK_MOVE, Minecraft.player);
                        }

                        if (this.mode.is("Тотемы")) {
                            var10005 = mc;
                            mc.playerController.windowClick(chest.windowId, 3, 0, ClickType.QUICK_MOVE, Minecraft.player);
                        }

                        if (this.mode.is("Нодебафф")) {
                            var10005 = mc;
                            mc.playerController.windowClick(chest.windowId, 4, 0, ClickType.QUICK_MOVE, Minecraft.player);
                        }

                        if (this.mode.is("Шары")) {
                            var10005 = mc;
                            mc.playerController.windowClick(chest.windowId, 5, 0, ClickType.QUICK_MOVE, Minecraft.player);
                        }

                        if (this.mode.is("Классик")) {
                            var10005 = mc;
                            mc.playerController.windowClick(chest.windowId, 6, 0, ClickType.QUICK_MOVE, Minecraft.player);
                        }

                        if (this.mode.is("Читерский рай")) {
                            var10005 = mc;
                            mc.playerController.windowClick(chest.windowId, 7, 0, ClickType.QUICK_MOVE, Minecraft.player);
                        }

                        if (this.mode.is("Незерка")) {
                            var10005 = mc;
                            mc.playerController.windowClick(chest.windowId, 8, 0, ClickType.QUICK_MOVE, Minecraft.player);
                        }

                        this.counterChoice.reset();
                    }
                }
            } else if (mc.currentScreen.getTitle().getString().contains("Настройка поединка") && this.counterTo.isReached(150L)) {
                var10005 = mc;
                mc.playerController.windowClick(chest.windowId, 0, 0, ClickType.QUICK_MOVE, Minecraft.player);
                this.counterTo.reset();
            }
        }

    }

    @Subscribe
    private void onPacket(EventPacket event) {
        if (event.isReceive()) {
            IPacket<?> packet = event.getPacket();
            if (packet instanceof SChatPacket) {
                SChatPacket chat = (SChatPacket)packet;
                String text = chat.getChatComponent().getString().toLowerCase();
                if (text.contains("начало") && text.contains("через") && text.contains("секунд!") || text.equals("дуэли » во время поединка запрещено использовать команды")) {
                    this.toggle();
                }
            }
        }

    }

    private List<String> getOnlinePlayers() {
        Minecraft var10000 = mc;
        return (List)Minecraft.player.connection.getPlayerInfoMap().stream().map(NetworkPlayerInfo::getGameProfile).map(GameProfile::getName).filter((profileName) -> {
            return pattern.matcher(profileName).matches();
        }).collect(Collectors.toList());
    }
}
