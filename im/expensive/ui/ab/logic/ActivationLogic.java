/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.ab.logic;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import im.expensive.events.EventUpdate;
import im.expensive.ui.ab.logic.AuctionLogic;
import im.expensive.ui.ab.model.ItemStorage;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.item.ItemStack;

public class ActivationLogic {
    private final Minecraft mc;
    public State currentState;
    protected final ItemStorage itemStorage;
    public final List<ItemStack> itemList = new LinkedList<ItemStack>();
    private final AuctionLogic auctionLogic;

    public ActivationLogic(ItemStorage itemStorage, EventBus eventBus) {
        this.itemStorage = itemStorage;
        this.currentState = State.\u041d\u0435\u0410\u043a\u0442\u0438\u0432\u0435\u043d;
        this.auctionLogic = new AuctionLogic(this);
        this.mc = Minecraft.getInstance();
        eventBus.register(this);
    }

    @Subscribe
    private void onUpdate(EventUpdate e) {
        switch (this.currentState) {
            case \u0410\u041a\u0422\u0418\u0412\u0415\u041d: {
                this.processActive();
                break;
            }
        }
    }

    private void processActive() {
        Screen screen = this.mc.currentScreen;
        if (screen instanceof ChestScreen) {
            ChestScreen chestScreen = (ChestScreen)screen;
            this.auctionLogic.processBuy(chestScreen);
        }
    }

    public void toggleState() {
        this.currentState = this.currentState == State.\u0410\u041a\u0422\u0418\u0412\u0415\u041d ? State.\u041d\u0435\u0410\u043a\u0442\u0438\u0432\u0435\u043d : State.\u0410\u041a\u0422\u0418\u0412\u0415\u041d;
    }

    public boolean isActive() {
        return this.currentState == State.\u0410\u041a\u0422\u0418\u0412\u0415\u041d;
    }

    public State getCurrentState() {
        return this.currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public static enum State {
        \u0410\u041a\u0422\u0418\u0412\u0415\u041d,
        \u041d\u0435\u0410\u043a\u0442\u0438\u0432\u0435\u043d;

    }
}

