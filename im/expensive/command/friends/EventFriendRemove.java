/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.command.friends;

public class EventFriendRemove {
    private final String friendName;

    public EventFriendRemove(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendName() {
        return this.friendName;
    }
}

