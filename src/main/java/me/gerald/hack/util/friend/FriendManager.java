package me.gerald.hack.util.friend;

import java.util.ArrayList;
import java.util.List;

public class FriendManager {
    private List<Friend> friends;

    public FriendManager() {
        friends = new ArrayList<>();
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void addFriend(String name) {
        friends.add(new Friend(name));
    }

    public void delFriend(String name) {
        friends.remove(getFriendByName(name));
    }

    public Friend getFriendByName(String name) {
        Friend out = null;
        for(Friend friend : getFriends()) {
            if(friend.getName().equalsIgnoreCase(name)) {
                out = friend;
            }
        }
        return out;
    }

    public boolean isFriend(String name){
        for(Friend f : getFriends()){
            if(f.getName().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }
}
