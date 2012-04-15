package me.Iso.ChopTree;

public class ChopTreePlayer {

    private static ChopTree plugin;
    private boolean active;
    private String playerName;
    
    public ChopTreePlayer(ChopTree instance, String playerName) {
        plugin = instance;
        active = getSetting("active");
        this.playerName = playerName;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean setting) {
        active = setting;
    }
    
    public void toggleActive() {
        if (active) {
            active = false;
        } else {
            active = true;
        }
    }
    
    private boolean getSetting(String setting) {
        boolean value = false;
        if (setting.equalsIgnoreCase("active")) {
            value = plugin.playersDb.getBoolean(playerName + "." + setting, plugin.defaultActive);
        }
        return value;
    }

}
