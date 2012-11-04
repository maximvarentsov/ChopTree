package me.Iso.ChopTree;

public class ChopTreePlayer {

    private static ChopTree plugin;
    private boolean active;
    private String playerName;
    
    public ChopTreePlayer(ChopTree instance, String playerName) {
        plugin = instance;
        this.playerName = playerName;
        active = getSetting("active");
        if (plugin.playersDb.get(playerName + ".active") == null) {
            addPlayer();
            active = plugin.defaultActive;
        }
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean setting) {
        active = setting;
        plugin.playersDb.set(playerName + ".active", setting);
        plugin.savePlayers();
    }
    
    public void toggleActive() {
        if (active) {
            active = false;
        } else {
            active = true;
        }
        plugin.playersDb.set(playerName + ".active", active);
        plugin.savePlayers();
    }
    
    private boolean getSetting(String setting) {
        boolean value = false;
        if (setting.equalsIgnoreCase("active")) {
            value = plugin.playersDb.getBoolean(playerName + "." + setting, plugin.defaultActive);
        }
        return value;
    }
    
    private void addPlayer() {
        plugin.playersDb.set(playerName + ".active", plugin.defaultActive);
        plugin.savePlayers();
    }

}
