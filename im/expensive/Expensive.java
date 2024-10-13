/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive;

import com.google.common.eventbus.EventBus;
import im.expensive.MacroManager;
import im.expensive.command.Command;
import im.expensive.command.CommandDispatcher;
import im.expensive.command.friends.FriendStorage;
import im.expensive.command.impl.AdviceCommandFactoryImpl;
import im.expensive.command.impl.ConsoleLogger;
import im.expensive.command.impl.MinecraftLogger;
import im.expensive.command.impl.MultiLogger;
import im.expensive.command.impl.ParametersFactoryImpl;
import im.expensive.command.impl.PrefixImpl;
import im.expensive.command.impl.StandaloneCommandDispatcher;
import im.expensive.command.impl.feature.ABCommand;
import im.expensive.command.impl.feature.BindCommand;
import im.expensive.command.impl.feature.ConfigCommand;
import im.expensive.command.impl.feature.FriendCommand;
import im.expensive.command.impl.feature.GPSCommand;
import im.expensive.command.impl.feature.HClipCommand;
import im.expensive.command.impl.feature.ListCommand;
import im.expensive.command.impl.feature.MacroCommand;
import im.expensive.command.impl.feature.MemoryCommand;
import im.expensive.command.impl.feature.RCTCommand;
import im.expensive.command.impl.feature.StaffCommand;
import im.expensive.command.impl.feature.VClipCommand;
import im.expensive.command.staffs.StaffStorage;
import im.expensive.config.ConfigStorage;
import im.expensive.config.Parse;
import im.expensive.events.EventKey;
import im.expensive.functions.api.Function;
import im.expensive.functions.api.FunctionRegistry;
import im.expensive.functions.api.WermetistPidor;
import im.expensive.scripts.client.ScriptManager;
import im.expensive.ui.NotificationManager;
import im.expensive.ui.ab.factory.ItemFactoryImpl;
import im.expensive.ui.ab.logic.ActivationLogic;
import im.expensive.ui.ab.model.IItem;
import im.expensive.ui.ab.model.ItemStorage;
import im.expensive.ui.ab.render.Window;
import im.expensive.ui.autobuy.AutoBuyConfig;
import im.expensive.ui.autobuy.AutoBuyHandler;
import im.expensive.ui.dropdown.DropDown;
import im.expensive.ui.mainmenu.AltConfig;
import im.expensive.ui.mainmenu.AltManager;
import im.expensive.ui.styles.Style;
import im.expensive.ui.styles.StyleFactoryImpl;
import im.expensive.ui.styles.StyleManager;
import im.expensive.utils.TPSCalc;
import im.expensive.utils.client.ServerTPS;
import im.expensive.utils.drag.DragManager;
import im.expensive.utils.drag.Dragging;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;
import via.ViaMCP;

public class Expensive {
    public static UserData userData;
    public boolean playerOnServer = false;
    public static final String CLIENT_NAME = "verist solutions";
    private static Expensive instance;
    private FunctionRegistry functionRegistry;
    private ConfigStorage configStorage;
    private CommandDispatcher commandDispatcher;
    private ServerTPS serverTPS;
    private MacroManager macroManager;
    private StyleManager styleManager;
    private final EventBus eventBus = new EventBus();
    private final ScriptManager scriptManager = new ScriptManager();
    private final File clientDir;
    private final File filesDir;
    private AltConfig altConfig;
    private DropDown dropDown;
    private Window autoBuyUI;
    private AutoBuyConfig autoBuyConfig;
    private AutoBuyHandler autoBuyHandler;
    private ViaMCP viaMCP;
    private TPSCalc tpsCalc;
    private ActivationLogic activationLogic;
    private Parse parse;
    private ItemStorage itemStorage;
    public static long initTime;
    private AltManager altManager;
    private final EventKey eventKey;

    public Expensive() {
        this.clientDir = new File(Minecraft.getInstance().gameDir + "\\verist");
        this.filesDir = new File(Minecraft.getInstance().gameDir + "\\verist\\files");
        this.autoBuyConfig = new AutoBuyConfig();
        this.eventKey = new EventKey(-1);
        instance = this;
        if (!this.clientDir.exists()) {
            this.clientDir.mkdirs();
        }
        if (!this.filesDir.exists()) {
            this.filesDir.mkdirs();
        }
        this.clientLoad();
        FriendStorage.load();
        StaffStorage.load();
    }

    private void clientLoad() {
        this.viaMCP = new ViaMCP();
        this.serverTPS = new ServerTPS();
        this.functionRegistry = new FunctionRegistry();
        this.macroManager = new MacroManager();
        this.configStorage = new ConfigStorage();
        this.functionRegistry.init();
        this.initCommands();
        this.initStyles();
        this.altConfig = new AltConfig();
        this.tpsCalc = new TPSCalc();
        WermetistPidor.NOTIFICATION_MANAGER = new NotificationManager();
        this.altManager = new AltManager();
        this.altConfig = new AltConfig();
        try {
            this.autoBuyConfig.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.altConfig.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.configStorage.init();
        } catch (IOException e) {
            System.out.println("Ошибка при подгрузке конфига.");
        }
        try {
            this.macroManager.init();
        } catch (IOException e) {
            System.out.println("Ошибка при подгрузке конфига макросов.");
        }
        DragManager.load();
        this.dropDown = new DropDown(new StringTextComponent("A"));
        this.initAutoBuy();
        this.autoBuyUI = new Window(new StringTextComponent("A"), this.itemStorage);
        this.autoBuyHandler = new AutoBuyHandler();
        this.autoBuyConfig = new AutoBuyConfig();
        this.eventBus.register(this);
        initTime = System.currentTimeMillis();
    }

    public void onKeyPressed(int key) {
        if (this.functionRegistry.getSelfDestruct().unhooked) {
            return;
        }
        this.eventKey.setKey(key);
        this.eventBus.post(this.eventKey);
        this.macroManager.onKeyPressed(key);
        if (key == 344) {
            Minecraft.getInstance().displayGuiScreen(this.dropDown);
        }
        if (this.functionRegistry.getAutoBuyUI().isState() && (Integer)this.functionRegistry.getAutoBuyUI().setting.get() == key) {
            Minecraft.getInstance().displayGuiScreen(this.autoBuyUI);
        }
    }

    private void initAutoBuy() {
        ItemFactoryImpl itemFactory = new ItemFactoryImpl();
        CopyOnWriteArrayList<IItem> items = new CopyOnWriteArrayList<IItem>();
        this.itemStorage = new ItemStorage(items, itemFactory);
        this.activationLogic = new ActivationLogic(this.itemStorage, this.eventBus);
    }

    private void initCommands() {
        Minecraft mc = Minecraft.getInstance();
        MultiLogger logger = new MultiLogger(List.of());
        ArrayList<Command> commands = new ArrayList<Command>();
        PrefixImpl prefix = new PrefixImpl();
        ItemFactoryImpl itemFactory = new ItemFactoryImpl();
        commands.add(new ListCommand(commands, logger));
        commands.add(new FriendCommand(prefix, logger, mc));
        commands.add(new BindCommand(prefix, logger));
        commands.add(new GPSCommand(prefix, logger));
        commands.add(new ConfigCommand(this.configStorage, prefix, logger));
        commands.add(new MacroCommand(this.macroManager, prefix, logger));
        commands.add(new VClipCommand(prefix, logger, mc));
        commands.add(new HClipCommand(prefix, logger, mc));
        commands.add(new StaffCommand(prefix, logger));
        commands.add(new MemoryCommand(logger));
        commands.add(new RCTCommand(logger, mc));
        commands.add(new ABCommand(prefix, logger, itemFactory));
        AdviceCommandFactoryImpl adviceCommandFactory = new AdviceCommandFactoryImpl(logger);
        ParametersFactoryImpl parametersFactory = new ParametersFactoryImpl();
        this.commandDispatcher = new StandaloneCommandDispatcher(commands, adviceCommandFactory, prefix, parametersFactory, logger);
    }

    private void initStyles() {
        StyleFactoryImpl styleFactory = new StyleFactoryImpl();
        ArrayList<Style> styles = new ArrayList<Style>();
        styles.add(styleFactory.createStyle("\u041a\u043e\u0441\u043c\u0438\u0447\u0435\u0441\u043a\u0438\u0439", new Color(4, 180, 255), new Color(16, 31, 255)));
        styles.add(styleFactory.createStyle("\u041a\u0440\u043e\u0432\u0430\u0432\u044b\u0439", new Color(197, 55, 109), new Color(106, 27, 29)));
        styles.add(styleFactory.createStyle("\u041f\u0440\u0438\u0437\u0440\u0430\u0447\u043d\u044b\u0439", new Color(0, 223, 113), new Color(0, 52, 232)));
        styles.add(styleFactory.createStyle("\u041c\u0430\u043b\u0438\u043d\u043e\u0432\u044b\u0439", new Color(255, 219, 187), new Color(255, 58, 154)));
        styles.add(styleFactory.createStyle("\u041e\u0433\u043d\u0435\u043d\u043d\u044b\u0439", new Color(255, 197, 63), new Color(255, 31, 87)));
        styles.add(styleFactory.createStyle("\u041b\u0430\u0432\u0430\u043d\u0434\u043e\u0432\u044b\u0439", new Color(163, 187, 250), new Color(66, 57, 155)));
        styles.add(styleFactory.createStyle("\u0424\u0438\u043e\u043b\u0435\u0442\u043e\u0432\u043e \u0433\u043e\u043b\u0443\u0431\u043e\u0439", new Color(79, 62, 255), new Color(6, 241, 255)));
        styles.add(styleFactory.createStyle("\u041a\u043e\u043d\u0444\u0435\u0442\u043d\u044b\u0439", new Color(242, 8, 254), new Color(14, 207, 224)));
        styles.add(styleFactory.createStyle("\u041c\u043e\u0445\u0438\u0442\u043e", new Color(97, 210, 187), new Color(255, 255, 255)));
        styles.add(styleFactory.createStyle("\u041a\u0440\u0430\u0441\u0438\u0432\u0435\u043d\u044c\u043a\u0438\u0439", new Color(213, 43, 43), new Color(84, 214, 216)));
        styles.add(styleFactory.createStyle("\u041a\u0440\u0430\u0441\u0438\u0432\u0435\u043d\u044c\u043a\u0438\u04391", new Color(155, 98, 98), new Color(255, 255, 255)));
        styles.add(styleFactory.createStyle("\u0418\u0437\u0443\u043c\u0440\u0443\u0434\u043d\u044b\u0435 \u0433\u043b\u0430\u0437\u0430", new Color(99, 255, 3), new Color(255, 255, 255)));
        this.styleManager = new StyleManager(styles, (Style)styles.get(0));
    }

    public Dragging createDrag(Function module, String name, float x, float y) {
        DragManager.draggables.put(name, new Dragging(module, name, x, y));
        return DragManager.draggables.get(name);
    }

    public boolean isPlayerOnServer() {
        return this.playerOnServer;
    }

    public FunctionRegistry getFunctionRegistry() {
        return this.functionRegistry;
    }

    public ConfigStorage getConfigStorage() {
        return this.configStorage;
    }

    public CommandDispatcher getCommandDispatcher() {
        return this.commandDispatcher;
    }

    public ServerTPS getServerTPS() {
        return this.serverTPS;
    }

    public MacroManager getMacroManager() {
        return this.macroManager;
    }

    public StyleManager getStyleManager() {
        return this.styleManager;
    }

    public EventBus getEventBus() {
        return this.eventBus;
    }

    public ScriptManager getScriptManager() {
        return this.scriptManager;
    }

    public File getClientDir() {
        return this.clientDir;
    }

    public File getFilesDir() {
        return this.filesDir;
    }

    public AltConfig getAltConfig() {
        return this.altConfig;
    }

    public DropDown getDropDown() {
        return this.dropDown;
    }

    public Window getAutoBuyUI() {
        return this.autoBuyUI;
    }

    public AutoBuyConfig getAutoBuyConfig() {
        return this.autoBuyConfig;
    }

    public AutoBuyHandler getAutoBuyHandler() {
        return this.autoBuyHandler;
    }

    public ViaMCP getViaMCP() {
        return this.viaMCP;
    }

    public TPSCalc getTpsCalc() {
        return this.tpsCalc;
    }

    public ActivationLogic getActivationLogic() {
        return this.activationLogic;
    }

    public Parse getParse() {
        return this.parse;
    }

    public ItemStorage getItemStorage() {
        return this.itemStorage;
    }

    public AltManager getAltManager() {
        return this.altManager;
    }

    public EventKey getEventKey() {
        return this.eventKey;
    }

    public static Expensive getInstance() {
        return instance;
    }

    public static class UserData {
        final String user;
        final int uid;

        public String getUser() {
            return this.user;
        }

        public int getUid() {
            return this.uid;
        }

        public UserData(String user, int uid) {
            this.user = user;
            this.uid = uid;
        }
    }
}

