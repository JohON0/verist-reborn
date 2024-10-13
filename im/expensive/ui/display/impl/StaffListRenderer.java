/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.display.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import im.expensive.Expensive;
import im.expensive.command.staffs.StaffStorage;
import im.expensive.events.EventDisplay;
import im.expensive.events.EventUpdate;
import im.expensive.ui.display.ElementRenderer;
import im.expensive.ui.display.ElementUpdater;
import im.expensive.ui.styles.Style;
import im.expensive.utils.client.IMinecraft;
import im.expensive.utils.drag.Dragging;
import im.expensive.utils.render.ColorUtils;
import im.expensive.utils.render.DisplayUtils;
import im.expensive.utils.render.font.Fonts;
import im.expensive.utils.text.GradientUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameType;
import ru.hogoshi.Animation;

public class StaffListRenderer
implements ElementRenderer,
ElementUpdater {
    private final Dragging dragging;
    private final ResourceLocation logo = new ResourceLocation("expensive/xz/staff1.png");
    private final float iconSize = 10.0f;
    private final List<Staff> staffPlayers = new ArrayList<Staff>();
    private final Pattern namePattern = Pattern.compile("^\\w{3,16}$");
    private final Pattern prefixMatches = Pattern.compile(".*(mod|der|adm|help|wne|\u0445\u0435\u043b\u043f|\u0430\u0434\u043c|\u043f\u043e\u0434\u0434\u0435\u0440\u0436\u043a\u0430|\u043a\u0443\u0440\u0430|own|taf|curat|dev|supp|yt|\u0441\u043e\u0442\u0440\u0443\u0434).*");
    private final Animation animation = new Animation();
    private float width;
    private float height;

    @Override
    public void update(EventUpdate e) {
        this.staffPlayers.clear();
        for (ScorePlayerTeam team : Minecraft.world.getScoreboard().getTeams().stream().sorted(Comparator.comparing(Team::getName)).toList()) {
            Staff staff;
            String name = team.getMembershipCollection().toString().replaceAll("[\\[\\]]", "");
            boolean vanish = true;
            for (NetworkPlayerInfo info : mc.getConnection().getPlayerInfoMap()) {
                if (!info.getGameProfile().getName().equals(name)) continue;
                vanish = false;
            }
            if (!this.namePattern.matcher(name).matches()) continue;
            if (name.equals(Minecraft.player.getName().getString())) continue;
            if (!vanish && (this.prefixMatches.matcher(team.getPrefix().getString().toLowerCase(Locale.ROOT)).matches() || StaffStorage.isStaff(name))) {
                staff = new Staff(team.getPrefix(), name, false, Status.NONE);
                this.staffPlayers.add(staff);
            }
            if (!vanish || team.getPrefix().getString().isEmpty()) continue;
            staff = new Staff(team.getPrefix(), name, true, Status.VANISHED);
            this.staffPlayers.add(staff);
        }
    }

    @Override
    public void render(EventDisplay eventDisplay) {
        float posX = this.dragging.getX();
        float posY = this.dragging.getY();
        float padding = 5.0f;
        float fontSize = 6.5f;
        MatrixStack ms = eventDisplay.getMatrixStack();
        StringTextComponent name = GradientUtil.gradient("Employees");
        Style style = Expensive.getInstance().getStyleManager().getCurrentStyle();
        this.drawStyledRect(posX, posY, this.width, this.height, 4.0f, 220);
        DisplayUtils.drawImage(this.logo, posX + padding, posY + padding, 10.0f, 10.0f, ColorUtils.rgb(255, 255, 255));
        Fonts.sfui.drawText(ms, "Active Staff", posX + 20.0f, posY + padding + 1.5f, ColorUtils.rgb(255, 255, 255), 6.5f);
        float maxWidth = Fonts.sfMedium.getWidth(name, fontSize) + padding * 2.0f;
        float localHeight = fontSize + padding * 2.0f;
        DisplayUtils.drawRectVerticalW(posX + 18.0f, (posY += fontSize + padding * 2.0f) - 13.5f, 1.0, 14.0, 3, ColorUtils.rgba(255, 255, 255, 191));
        posY += 3.5f;
        for (Staff f : this.staffPlayers) {
            ITextComponent prefix = f.getPrefix();
            float prefixWidth = Fonts.sfMedium.getWidth(prefix, fontSize);
            String staff = (prefix.getString().isEmpty() ? "" : " ") + f.getName();
            float nameWidth = Fonts.sfMedium.getWidth(staff, fontSize);
            float localWidth = prefixWidth + nameWidth + Fonts.sfMedium.getWidth(f.getStatus().string, fontSize) + padding * 3.0f;
            Fonts.sfMedium.drawText(ms, prefix, posX + padding, posY, fontSize, 255);
            Fonts.sfMedium.drawText(ms, staff, posX + padding + prefixWidth, posY, ColorUtils.rgb(255, 255, 255), fontSize);
            Fonts.sfMedium.drawText(ms, f.getStatus().string, posX + this.width - padding - Fonts.sfMedium.getWidth(f.getStatus().string, fontSize), posY, f.getStatus().color, fontSize);
            if (localWidth > maxWidth) {
                maxWidth = localWidth;
            }
            posY += fontSize + padding;
            localHeight += fontSize + padding;
        }
        this.width = Math.max(maxWidth, 80.0f);
        this.height = localHeight + 2.5f;
        this.dragging.setWidth(this.width);
        this.dragging.setHeight(this.height);
    }

    private void drawStyledRect(float x, float y, float width, float height, float radius, int alpha) {
        DisplayUtils.drawRoundedRect(x, y, width, height, radius, ColorUtils.rgba(21, 21, 21, alpha));
    }

    public StaffListRenderer(Dragging dragging) {
        this.dragging = dragging;
    }

    public static class Staff {
        ITextComponent prefix;
        String name;
        boolean isSpec;
        Status status;

        public void updateStatus() {
            for (NetworkPlayerInfo info : IMinecraft.mc.getConnection().getPlayerInfoMap()) {
                if (!info.getGameProfile().getName().equals(this.name)) continue;
                if (info.getGameType() == GameType.SPECTATOR) {
                    return;
                }
                this.status = Status.NONE;
                return;
            }
            this.status = Status.VANISHED;
        }

        public Staff(ITextComponent prefix, String name, boolean isSpec, Status status2) {
            this.prefix = prefix;
            this.name = name;
            this.isSpec = isSpec;
            this.status = status2;
        }

        public ITextComponent getPrefix() {
            return this.prefix;
        }

        public String getName() {
            return this.name;
        }

        public boolean isSpec() {
            return this.isSpec;
        }

        public Status getStatus() {
            return this.status;
        }

        public void setPrefix(ITextComponent prefix) {
            this.prefix = prefix;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setSpec(boolean isSpec) {
            this.isSpec = isSpec;
        }

        public void setStatus(Status status2) {
            this.status = status2;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Staff)) {
                return false;
            }
            Staff other = (Staff)o;
            if (!other.canEqual(this)) {
                return false;
            }
            if (this.isSpec() != other.isSpec()) {
                return false;
            }
            ITextComponent this$prefix = this.getPrefix();
            ITextComponent other$prefix = other.getPrefix();
            if (this$prefix == null ? other$prefix != null : !this$prefix.equals(other$prefix)) {
                return false;
            }
            String this$name = this.getName();
            String other$name = other.getName();
            if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
                return false;
            }
            Status this$status = this.getStatus();
            Status other$status = other.getStatus();
            return !(this$status == null ? other$status != null : !((Object)((Object)this$status)).equals((Object)other$status));
        }

        protected boolean canEqual(Object other) {
            return other instanceof Staff;
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            result = result * 59 + (this.isSpec() ? 79 : 97);
            ITextComponent $prefix = this.getPrefix();
            result = result * 59 + ($prefix == null ? 43 : $prefix.hashCode());
            String $name = this.getName();
            result = result * 59 + ($name == null ? 43 : $name.hashCode());
            Status $status = this.getStatus();
            result = result * 59 + ($status == null ? 43 : ((Object)((Object)$status)).hashCode());
            return result;
        }

        public String toString() {
            return "StaffListRenderer.Staff(prefix=" + this.getPrefix() + ", name=" + this.getName() + ", isSpec=" + this.isSpec() + ", status=" + this.getStatus() + ")";
        }
    }

    public static enum Status {
        NONE("", -1),
        VANISHED("V", ColorUtils.rgb(254, 68, 68));

        public final String string;
        public final int color;

        private Status(String string2, int color) {
            this.string = string2;
            this.color = color;
        }
    }
}

