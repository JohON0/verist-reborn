/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.api;

import com.google.common.eventbus.Subscribe;
import im.expensive.Expensive;
import im.expensive.events.EventKey;
import im.expensive.functions.api.Function;
import im.expensive.functions.impl.combat.AntiBot;
import im.expensive.functions.impl.combat.AutoArmor;
import im.expensive.functions.impl.combat.AutoExplosion;
import im.expensive.functions.impl.combat.AutoGapple;
import im.expensive.functions.impl.combat.AutoPotion;
import im.expensive.functions.impl.combat.AutoSwap;
import im.expensive.functions.impl.combat.AutoTotem;
import im.expensive.functions.impl.combat.Backtrack;
import im.expensive.functions.impl.combat.Hitbox;
import im.expensive.functions.impl.combat.KillAura;
import im.expensive.functions.impl.combat.NoEntityTrace;
import im.expensive.functions.impl.combat.NoFriendDamage;
import im.expensive.functions.impl.combat.TriggerBot;
import im.expensive.functions.impl.combat.Velocity;
import im.expensive.functions.impl.hyeta.AirJump;
import im.expensive.functions.impl.hyeta.AspectRatio;
import im.expensive.functions.impl.hyeta.AutoDuel;
import im.expensive.functions.impl.hyeta.BetterChat;
import im.expensive.functions.impl.hyeta.CasinoBit;
import im.expensive.functions.impl.hyeta.ChatHelper;
import im.expensive.functions.impl.hyeta.FakePlayer;
import im.expensive.functions.impl.hyeta.FastEXP;
import im.expensive.functions.impl.hyeta.FreeLook;
import im.expensive.functions.impl.hyeta.ItemRadius;
import im.expensive.functions.impl.hyeta.NoWeb;
import im.expensive.functions.impl.hyeta.Nuker;
import im.expensive.functions.impl.hyeta.PrizrakHat;
import im.expensive.functions.impl.hyeta.SRPSpoofer;
import im.expensive.functions.impl.hyeta.Spider;
import im.expensive.functions.impl.hyeta.nofall;
import im.expensive.functions.impl.misc.AntiAFK;
import im.expensive.functions.impl.misc.AntiPush;
import im.expensive.functions.impl.misc.AutoAccept;
import im.expensive.functions.impl.misc.AutoBuyUI;
import im.expensive.functions.impl.misc.AutoEat;
import im.expensive.functions.impl.misc.AutoFish;
import im.expensive.functions.impl.misc.AutoRespawn;
import im.expensive.functions.impl.misc.AutoTransfer;
import im.expensive.functions.impl.misc.BetterMinecraft;
import im.expensive.functions.impl.misc.BuyHelperS;
import im.expensive.functions.impl.misc.ClientSounds;
import im.expensive.functions.impl.misc.ElytraHelper;
import im.expensive.functions.impl.misc.GriefHelper;
import im.expensive.functions.impl.misc.HitSound;
import im.expensive.functions.impl.misc.ItemScroller;
import im.expensive.functions.impl.misc.ItemSwapFix;
import im.expensive.functions.impl.misc.LeaveTracker;
import im.expensive.functions.impl.misc.NameProtect;
import im.expensive.functions.impl.misc.NoEventDelay;
import im.expensive.functions.impl.misc.RWHelper;
import im.expensive.functions.impl.misc.SelfDestruct;
import im.expensive.functions.impl.misc.Tyanka;
import im.expensive.functions.impl.misc.xCarry;
import im.expensive.functions.impl.movement.AutoPilo2;
import im.expensive.functions.impl.movement.AutoPilot;
import im.expensive.functions.impl.movement.AutoSprint;
import im.expensive.functions.impl.movement.BoatFly;
import im.expensive.functions.impl.movement.DragonFly;
import im.expensive.functions.impl.movement.ElytraFly;
import im.expensive.functions.impl.movement.Fly;
import im.expensive.functions.impl.movement.Jesus;
import im.expensive.functions.impl.movement.LongJump;
import im.expensive.functions.impl.movement.NoClip;
import im.expensive.functions.impl.movement.NoSlow;
import im.expensive.functions.impl.movement.Parkour;
import im.expensive.functions.impl.movement.Phase;
import im.expensive.functions.impl.movement.Speed;
import im.expensive.functions.impl.movement.Speed1;
import im.expensive.functions.impl.movement.Strafe;
import im.expensive.functions.impl.movement.TargetStrafe;
import im.expensive.functions.impl.movement.Timer;
import im.expensive.functions.impl.movement.WaterSpeed;
import im.expensive.functions.impl.player.AutoAuth;
import im.expensive.functions.impl.player.AutoLeave;
import im.expensive.functions.impl.player.AutoTool;
import im.expensive.functions.impl.player.BaseFinder;
import im.expensive.functions.impl.player.ChestStealer;
import im.expensive.functions.impl.player.ClickFrien;
import im.expensive.functions.impl.player.ClickPearl;
import im.expensive.functions.impl.player.FreeCam;
import im.expensive.functions.impl.player.InventoryMove;
import im.expensive.functions.impl.player.ItemCooldown;
import im.expensive.functions.impl.player.KTLeave;
import im.expensive.functions.impl.player.NoInteract;
import im.expensive.functions.impl.player.NoJumpDelay;
import im.expensive.functions.impl.player.NoRotate;
import im.expensive.functions.impl.player.PortalGodMode;
import im.expensive.functions.impl.player.REJOINER;
import im.expensive.functions.impl.render.BabyBoy;
import im.expensive.functions.impl.render.Cape;
import im.expensive.functions.impl.render.Chams;
import im.expensive.functions.impl.render.ChinaHat;
import im.expensive.functions.impl.render.ClickGui;
import im.expensive.functions.impl.render.Crosshair;
import im.expensive.functions.impl.render.DeathEffect;
import im.expensive.functions.impl.render.ESP;
import im.expensive.functions.impl.render.GlassHand;
import im.expensive.functions.impl.render.GlowESP;
import im.expensive.functions.impl.render.HUD;
import im.expensive.functions.impl.render.Hotbar;
import im.expensive.functions.impl.render.InventoryBackround;
import im.expensive.functions.impl.render.ItemPhysic;
import im.expensive.functions.impl.render.JumpCircle;
import im.expensive.functions.impl.render.NoRender;
import im.expensive.functions.impl.render.Particles;
import im.expensive.functions.impl.render.Pointers;
import im.expensive.functions.impl.render.Predictions;
import im.expensive.functions.impl.render.RPC;
import im.expensive.functions.impl.render.SeeInvisibles;
import im.expensive.functions.impl.render.Snow;
import im.expensive.functions.impl.render.StorageESP;
import im.expensive.functions.impl.render.SuperLight;
import im.expensive.functions.impl.render.SwingAnimation;
import im.expensive.functions.impl.render.Themes;
import im.expensive.functions.impl.render.Tracers;
import im.expensive.functions.impl.render.Trails;
import im.expensive.functions.impl.render.Trails2;
import im.expensive.functions.impl.render.ViewModel;
import im.expensive.functions.impl.render.World;
import im.expensive.functions.impl.render.XrayBypass;
import im.expensive.functions.impl.render.block;
import im.expensive.utils.render.font.Font;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FunctionRegistry {
    private final List<Function> functions = new CopyOnWriteArrayList<Function>();
    private SwingAnimation swingAnimation;
    private HUD hud;
    private AutoGapple autoGapple;
    private AutoSprint autoSprint;
    private AspectRatio aspectRatio;
    private Velocity velocity;
    private NoRender noRender;
    private Timer timer;
    private AutoTool autoTool;
    private xCarry xcarry;
    private ElytraHelper elytrahelper;
    private Phase phase;
    private AutoBuyUI autoBuyUI;
    private ItemSwapFix itemswapfix;
    private AutoPotion autopotion;
    private TriggerBot triggerbot;
    private NoJumpDelay nojumpdelay;
    private ClickFrien clickfrien;
    private InventoryMove inventoryMove;
    private ESP esp;
    private AutoTransfer autoTransfer;
    private GriefHelper griefHelper;
    private ItemCooldown itemCooldown;
    private ClickPearl clickPearl;
    private AutoSwap autoSwap;
    private AutoArmor autoArmor;
    private Hitbox hitbox;
    private HitSound hitsound;
    private AntiPush antiPush;
    private FreeCam freeCam;
    private ChestStealer chestStealer;
    private AutoLeave autoLeave;
    private AutoAccept autoAccept;
    private NoEventDelay noEventDelay;
    private AutoRespawn autoRespawn;
    private Fly fly;
    private TargetStrafe targetStrafe;
    private ClientSounds clientSounds;
    private AutoTotem autoTotem;
    private NoSlow noSlow;
    private Pointers pointers;
    private AutoExplosion autoExplosion;
    private NoRotate noRotate;
    private KillAura killAura;
    private AntiBot antiBot;
    private Trails trails;
    private Crosshair crosshair;
    private DeathEffect deathEffect;
    private Strafe strafe;
    private World world;
    private ViewModel viewModel;
    private ElytraFly elytraFly;
    private ChinaHat chinaHat;
    private Snow snow;
    private Particles particles;
    private JumpCircle jumpCircle;
    private ItemPhysic itemPhysic;
    private ClickGui clickGui;
    private Predictions predictions;
    private NoEntityTrace noEntityTrace;
    private NoClip noClip;
    private ItemScroller itemScroller;
    private AutoFish autoFish;
    private StorageESP storageESP;
    private Spider spider;
    private NameProtect nameProtect;
    private NoInteract noInteract;
    private GlassHand glassHand;
    private Tracers tracers;
    private SelfDestruct selfDestruct;
    private Cape cape;
    private LeaveTracker leaveTracker;
    private BoatFly boatFly;
    private AntiAFK antiAFK;
    private PortalGodMode portalGodMode;
    private BetterMinecraft betterMinecraft;
    private Chams chams;
    private Backtrack backtrack;
    private SeeInvisibles seeInvisibles;
    private RWHelper rwHelper;
    private Hotbar hotbar;
    private Tyanka tyanka;
    private InventoryBackround inventoryBackround;

    public void init() {
        Function[] functionArray = new Function[120];
        this.hud = new HUD();
        functionArray[0] = this.hud;
        this.autoGapple = new AutoGapple();
        functionArray[1] = this.autoGapple;
        this.autoSprint = new AutoSprint();
        functionArray[2] = this.autoSprint;
        this.velocity = new Velocity();
        functionArray[3] = this.velocity;
        this.noRender = new NoRender();
        functionArray[4] = this.noRender;
        this.autoTool = new AutoTool();
        functionArray[5] = this.autoTool;
        this.deathEffect = new DeathEffect();
        functionArray[6] = this.deathEffect;
        this.noEventDelay = new NoEventDelay();
        functionArray[7] = this.noEventDelay;
        this.chams = new Chams();
        functionArray[8] = this.chams;
        this.xcarry = new xCarry();
        functionArray[9] = this.xcarry;
        this.seeInvisibles = new SeeInvisibles();
        functionArray[10] = this.seeInvisibles;
        this.elytrahelper = new ElytraHelper();
        functionArray[11] = this.elytrahelper;
        this.phase = new Phase();
        functionArray[12] = this.phase;
        this.itemswapfix = new ItemSwapFix();
        functionArray[13] = this.itemswapfix;
        this.autopotion = new AutoPotion();
        functionArray[14] = this.autopotion;
        this.noClip = new NoClip();
        functionArray[15] = this.noClip;
        this.triggerbot = new TriggerBot();
        functionArray[16] = this.triggerbot;
        this.nojumpdelay = new NoJumpDelay();
        functionArray[17] = this.nojumpdelay;
        this.aspectRatio = new AspectRatio();
        functionArray[18] = this.aspectRatio;
        this.cape = new Cape();
        functionArray[19] = this.cape;
        this.clickfrien = new ClickFrien();
        functionArray[20] = this.clickfrien;
        this.inventoryMove = new InventoryMove();
        functionArray[21] = this.inventoryMove;
        this.esp = new ESP();
        functionArray[22] = this.esp;
        this.autoTransfer = new AutoTransfer();
        functionArray[23] = this.autoTransfer;
        this.griefHelper = new GriefHelper();
        functionArray[24] = this.griefHelper;
        this.autoArmor = new AutoArmor();
        functionArray[25] = this.autoArmor;
        this.hitbox = new Hitbox();
        functionArray[26] = this.hitbox;
        this.hitsound = new HitSound();
        functionArray[27] = this.hitsound;
        this.antiPush = new AntiPush();
        functionArray[28] = this.antiPush;
        this.autoBuyUI = new AutoBuyUI();
        functionArray[29] = this.autoBuyUI;
        this.freeCam = new FreeCam();
        functionArray[30] = this.freeCam;
        this.chestStealer = new ChestStealer();
        functionArray[31] = this.chestStealer;
        this.autoLeave = new AutoLeave();
        functionArray[32] = this.autoLeave;
        this.autoAccept = new AutoAccept();
        functionArray[33] = this.autoAccept;
        this.autoRespawn = new AutoRespawn();
        functionArray[34] = this.autoRespawn;
        this.fly = new Fly();
        functionArray[35] = this.fly;
        this.clientSounds = new ClientSounds();
        functionArray[36] = this.clientSounds;
        this.noSlow = new NoSlow();
        functionArray[37] = this.noSlow;
        this.clickGui = new ClickGui();
        functionArray[38] = this.clickGui;
        this.pointers = new Pointers();
        functionArray[39] = this.pointers;
        this.autoExplosion = new AutoExplosion();
        functionArray[40] = this.autoExplosion;
        this.noRotate = new NoRotate();
        functionArray[41] = this.noRotate;
        this.antiBot = new AntiBot();
        functionArray[42] = this.antiBot;
        this.trails = new Trails();
        functionArray[43] = this.trails;
        this.hotbar = new Hotbar();
        functionArray[44] = this.hotbar;
        this.tyanka = new Tyanka();
        functionArray[45] = this.tyanka;
        this.crosshair = new Crosshair();
        functionArray[46] = this.crosshair;
        this.autoTotem = new AutoTotem();
        functionArray[47] = this.autoTotem;
        this.itemCooldown = new ItemCooldown();
        functionArray[48] = this.itemCooldown;
        this.killAura = new KillAura(this.autopotion);
        functionArray[49] = this.killAura;
        this.clickPearl = new ClickPearl(this.itemCooldown);
        functionArray[50] = this.clickPearl;
        this.autoSwap = new AutoSwap(this.autoTotem);
        functionArray[51] = this.autoSwap;
        this.targetStrafe = new TargetStrafe(this.killAura);
        functionArray[52] = this.targetStrafe;
        this.strafe = new Strafe(this.targetStrafe, this.killAura);
        functionArray[53] = this.strafe;
        this.swingAnimation = new SwingAnimation(this.killAura);
        functionArray[54] = this.swingAnimation;
        this.chinaHat = new ChinaHat(this.chinaHat);
        functionArray[55] = this.chinaHat;
        this.world = new World();
        functionArray[56] = this.world;
        this.viewModel = new ViewModel();
        functionArray[57] = this.viewModel;
        this.elytraFly = new ElytraFly();
        functionArray[58] = this.elytraFly;
        this.snow = new Snow();
        functionArray[59] = this.snow;
        this.particles = new Particles();
        functionArray[60] = this.particles;
        this.jumpCircle = new JumpCircle();
        functionArray[61] = this.jumpCircle;
        this.itemPhysic = new ItemPhysic();
        functionArray[62] = this.itemPhysic;
        this.predictions = new Predictions();
        functionArray[63] = this.predictions;
        this.noEntityTrace = new NoEntityTrace();
        functionArray[64] = this.noEntityTrace;
        this.itemScroller = new ItemScroller();
        functionArray[65] = this.itemScroller;
        this.autoFish = new AutoFish();
        functionArray[66] = this.autoFish;
        this.storageESP = new StorageESP();
        functionArray[67] = this.storageESP;
        this.spider = new Spider();
        functionArray[68] = this.spider;
        this.timer = new Timer();
        functionArray[69] = this.timer;
        this.nameProtect = new NameProtect();
        functionArray[70] = this.nameProtect;
        this.noInteract = new NoInteract();
        functionArray[71] = this.noInteract;
        this.glassHand = new GlassHand();
        functionArray[72] = this.glassHand;
        this.tracers = new Tracers();
        functionArray[73] = this.tracers;
        this.selfDestruct = new SelfDestruct();
        functionArray[74] = this.selfDestruct;
        this.leaveTracker = new LeaveTracker();
        functionArray[75] = this.leaveTracker;
        this.antiAFK = new AntiAFK();
        functionArray[76] = this.antiAFK;
        this.portalGodMode = new PortalGodMode();
        functionArray[77] = this.portalGodMode;
        this.betterMinecraft = new BetterMinecraft();
        functionArray[78] = this.betterMinecraft;
        this.backtrack = new Backtrack();
        functionArray[79] = this.backtrack;
        functionArray[80] = new Trails2();
        functionArray[81] = new LongJump();
        functionArray[82] = new BaseFinder();
        functionArray[83] = new ItemRadius();
        functionArray[84] = new SRPSpoofer();
        functionArray[85] = new Nuker();
        functionArray[86] = new NoWeb();
        functionArray[87] = new CasinoBit();
        functionArray[88] = new BetterChat();
        functionArray[89] = new WaterSpeed();
        functionArray[90] = new XrayBypass();
        functionArray[91] = new KTLeave();
        functionArray[92] = new FastEXP();
        functionArray[93] = new AutoEat();
        functionArray[94] = new nofall();
        functionArray[95] = new PrizrakHat();
        functionArray[96] = new ChatHelper();
        functionArray[97] = new AutoDuel();
        functionArray[98] = new AirJump();
        functionArray[99] = new Spider();
        functionArray[100] = new FakePlayer();
        functionArray[101] = new FreeLook();
        functionArray[102] = new Parkour();
        functionArray[103] = new GlowESP();
        functionArray[104] = new AutoPilot();
        functionArray[105] = new RWHelper();
        functionArray[106] = new RPC();
        functionArray[107] = new Jesus();
        functionArray[108] = new Speed();
        functionArray[109] = new DragonFly();
        functionArray[110] = new AutoPilo2();
        functionArray[111] = new REJOINER();
        functionArray[112] = new AutoAuth();
        functionArray[113] = new NoFriendDamage();
        functionArray[114] = new BabyBoy();
        functionArray[115] = new BuyHelperS();
        functionArray[116] = new SuperLight();
        functionArray[117] = new Speed1();
        functionArray[118] = new Themes();
        functionArray[119] = new block();
        this.registerAll(functionArray);
        Expensive.getInstance().getEventBus().register(this);
    }

    private void registerAll(Function ... Functions2) {
        Arrays.sort(Functions2, Comparator.comparing(Function::getName));
        this.functions.addAll(List.of((Object[])Functions2));
    }

    public List<Function> getSorted(Font font, float size) {
        return this.functions.stream().sorted((f1, f2) -> Float.compare(font.getWidth(f2.getName(), size), font.getWidth(f1.getName(), size))).toList();
    }

    @Subscribe
    private void onKey(EventKey e) {
        if (this.selfDestruct.unhooked) {
            return;
        }
        for (Function Function23 : this.functions) {
            if (Function23.getBind() != e.getKey()) continue;
            Function23.toggle();
        }
    }

    public List<Function> getFunctions() {
        return this.functions;
    }

    public SwingAnimation getSwingAnimation() {
        return this.swingAnimation;
    }

    public HUD getHud() {
        return this.hud;
    }

    public AutoGapple getAutoGapple() {
        return this.autoGapple;
    }

    public AutoSprint getAutoSprint() {
        return this.autoSprint;
    }

    public AspectRatio getAspectRatio() {
        return this.aspectRatio;
    }

    public Velocity getVelocity() {
        return this.velocity;
    }

    public NoRender getNoRender() {
        return this.noRender;
    }

    public Timer getTimer() {
        return this.timer;
    }

    public AutoTool getAutoTool() {
        return this.autoTool;
    }

    public xCarry getXcarry() {
        return this.xcarry;
    }

    public ElytraHelper getElytrahelper() {
        return this.elytrahelper;
    }

    public Phase getPhase() {
        return this.phase;
    }

    public AutoBuyUI getAutoBuyUI() {
        return this.autoBuyUI;
    }

    public ItemSwapFix getItemswapfix() {
        return this.itemswapfix;
    }

    public AutoPotion getAutopotion() {
        return this.autopotion;
    }

    public TriggerBot getTriggerbot() {
        return this.triggerbot;
    }

    public NoJumpDelay getNojumpdelay() {
        return this.nojumpdelay;
    }

    public ClickFrien getClickfrien() {
        return this.clickfrien;
    }

    public InventoryMove getInventoryMove() {
        return this.inventoryMove;
    }

    public ESP getEsp() {
        return this.esp;
    }

    public AutoTransfer getAutoTransfer() {
        return this.autoTransfer;
    }

    public GriefHelper getGriefHelper() {
        return this.griefHelper;
    }

    public ItemCooldown getItemCooldown() {
        return this.itemCooldown;
    }

    public ClickPearl getClickPearl() {
        return this.clickPearl;
    }

    public AutoSwap getAutoSwap() {
        return this.autoSwap;
    }

    public AutoArmor getAutoArmor() {
        return this.autoArmor;
    }

    public Hitbox getHitbox() {
        return this.hitbox;
    }

    public HitSound getHitsound() {
        return this.hitsound;
    }

    public AntiPush getAntiPush() {
        return this.antiPush;
    }

    public FreeCam getFreeCam() {
        return this.freeCam;
    }

    public ChestStealer getChestStealer() {
        return this.chestStealer;
    }

    public AutoLeave getAutoLeave() {
        return this.autoLeave;
    }

    public AutoAccept getAutoAccept() {
        return this.autoAccept;
    }

    public NoEventDelay getNoEventDelay() {
        return this.noEventDelay;
    }

    public AutoRespawn getAutoRespawn() {
        return this.autoRespawn;
    }

    public Fly getFly() {
        return this.fly;
    }

    public TargetStrafe getTargetStrafe() {
        return this.targetStrafe;
    }

    public ClientSounds getClientSounds() {
        return this.clientSounds;
    }

    public AutoTotem getAutoTotem() {
        return this.autoTotem;
    }

    public NoSlow getNoSlow() {
        return this.noSlow;
    }

    public Pointers getPointers() {
        return this.pointers;
    }

    public AutoExplosion getAutoExplosion() {
        return this.autoExplosion;
    }

    public NoRotate getNoRotate() {
        return this.noRotate;
    }

    public KillAura getKillAura() {
        return this.killAura;
    }

    public AntiBot getAntiBot() {
        return this.antiBot;
    }

    public Trails getTrails() {
        return this.trails;
    }

    public Crosshair getCrosshair() {
        return this.crosshair;
    }

    public DeathEffect getDeathEffect() {
        return this.deathEffect;
    }

    public Strafe getStrafe() {
        return this.strafe;
    }

    public World getWorld() {
        return this.world;
    }

    public ViewModel getViewModel() {
        return this.viewModel;
    }

    public ElytraFly getElytraFly() {
        return this.elytraFly;
    }

    public ChinaHat getChinaHat() {
        return this.chinaHat;
    }

    public Snow getSnow() {
        return this.snow;
    }

    public Particles getParticles() {
        return this.particles;
    }

    public JumpCircle getJumpCircle() {
        return this.jumpCircle;
    }

    public ItemPhysic getItemPhysic() {
        return this.itemPhysic;
    }

    public ClickGui getClickGui() {
        return this.clickGui;
    }

    public Predictions getPredictions() {
        return this.predictions;
    }

    public NoEntityTrace getNoEntityTrace() {
        return this.noEntityTrace;
    }

    public NoClip getNoClip() {
        return this.noClip;
    }

    public ItemScroller getItemScroller() {
        return this.itemScroller;
    }

    public AutoFish getAutoFish() {
        return this.autoFish;
    }

    public StorageESP getStorageESP() {
        return this.storageESP;
    }

    public Spider getSpider() {
        return this.spider;
    }

    public NameProtect getNameProtect() {
        return this.nameProtect;
    }

    public NoInteract getNoInteract() {
        return this.noInteract;
    }

    public GlassHand getGlassHand() {
        return this.glassHand;
    }

    public Tracers getTracers() {
        return this.tracers;
    }

    public SelfDestruct getSelfDestruct() {
        return this.selfDestruct;
    }

    public Cape getCape() {
        return this.cape;
    }

    public LeaveTracker getLeaveTracker() {
        return this.leaveTracker;
    }

    public BoatFly getBoatFly() {
        return this.boatFly;
    }

    public AntiAFK getAntiAFK() {
        return this.antiAFK;
    }

    public PortalGodMode getPortalGodMode() {
        return this.portalGodMode;
    }

    public BetterMinecraft getBetterMinecraft() {
        return this.betterMinecraft;
    }

    public Chams getChams() {
        return this.chams;
    }

    public Backtrack getBacktrack() {
        return this.backtrack;
    }

    public SeeInvisibles getSeeInvisibles() {
        return this.seeInvisibles;
    }

    public RWHelper getRwHelper() {
        return this.rwHelper;
    }

    public Hotbar getHotbar() {
        return this.hotbar;
    }

    public Tyanka getTyanka() {
        return this.tyanka;
    }

    public InventoryBackround getInventoryBackround() {
        return this.inventoryBackround;
    }
}

