/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.ui.mainmenu;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

public class Account {
    public String accountName = "";
    public String accountPassword = "";
    public long dateAdded;
    public ResourceLocation skin;
    public float x;
    public float y;
    private boolean favorite;

    public Account(String accountName) {
        this.accountName = accountName;
        this.dateAdded = System.currentTimeMillis();
        UUID uuid = null;
        try {
            uuid = Account.resolveUUID(accountName);
        } catch (IOException var4) {
            uuid = UUID.randomUUID();
        }
        this.skin = DefaultPlayerSkin.getDefaultSkin(uuid);
        Minecraft.getInstance().getSkinManager().loadProfileTextures(new GameProfile(uuid, accountName), (type, loc, tex) -> {
            if (type == MinecraftProfileTexture.Type.SKIN) {
                this.skin = loc;
            }
        }, true);
    }

    public Account(String accountName, long dateAdded) {
        this.accountName = accountName;
        this.dateAdded = dateAdded;
        UUID uuid = null;
        try {
            uuid = Account.resolveUUID(accountName);
        } catch (IOException var6) {
            uuid = UUID.randomUUID();
        }
        this.skin = DefaultPlayerSkin.getDefaultSkin(uuid);
        Minecraft.getInstance().getSkinManager().loadProfileTextures(new GameProfile(uuid, accountName), (type, loc, tex) -> {
            if (type == MinecraftProfileTexture.Type.SKIN) {
                this.skin = loc;
            }
        }, true);
    }

    public void toggleFavorite() {
        this.favorite = !this.favorite;
    }

    public boolean isFavorite() {
        return this.favorite;
    }

    public static UUID resolveUUID(String name) throws IOException {
        UUID uUID;
        InputStreamReader in = new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openStream(), StandardCharsets.UTF_8);
        try {
            uUID = UUID.fromString(new Gson().fromJson((Reader)in, JsonObject.class).get("id").getAsString().replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
        } catch (Throwable var8) {
            Throwable uuid = var8;
            try {
                try {
                    in.close();
                } catch (Throwable var6) {
                    uuid.addSuppressed(var6);
                }
                throw uuid;
            } catch (Throwable var7) {
                return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
            }
        }
        in.close();
        return uUID;
    }
}

