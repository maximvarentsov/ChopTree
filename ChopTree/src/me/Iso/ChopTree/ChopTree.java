package me.Iso.ChopTree;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ChopTree extends JavaPlugin {
    
    private final ChopTreeBlockListener blockListener = new ChopTreeBlockListener(this);
    public final List <String> options = new LinkedList <String> ();
    public final List <String> chunks = new LinkedList <String> ();
    public final List <Integer> mats = new LinkedList <Integer> ();
    public final List <Player> chunkmsg = new LinkedList <Player> ();
    public final HashMap<String, Boolean> players = new HashMap<String, Boolean> ();
    public final HashMap<Player, Block[]> trees = new HashMap<Player, Block[]> ();
    private FileConfiguration config;
    protected boolean defaultActive;
    protected boolean useAnything;
    protected boolean moreDamageToTools;
    protected boolean interruptIfToolBreaks;
    protected boolean logsMoveDown;
    protected boolean onlyTrees;
    protected String[] allowedTools;
    private File playerFile;
    protected FileConfiguration playersDb;
	
    @Override
    public void onEnable() {
    	PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(blockListener, this);
        loadConfig();
        playersDb = getPlayers();
    }
    
    @Override
    public void onDisable() {
    }
	
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("ChopTree") || commandLabel.equalsIgnoreCase("ct")) {
            if (Array.getLength(args) == 0) {
                // List Settings
                if (!sender.hasPermission("choptree.commands.choptree.info")) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
                    return false;
                }
                sender.sendMessage(ChatColor.GOLD + "ChopTree2 v" + getDescription().getVersion() + " : Rewritten by ellbristow");
                sender.sendMessage(ChatColor.GRAY + "===========================================");
                sender.sendMessage(ChatColor.GOLD + "ActiveByDefault : " + ChatColor.GRAY + defaultActive);
                sender.sendMessage(ChatColor.GOLD + "UseAnything : " + ChatColor.GRAY + useAnything);
                sender.sendMessage(ChatColor.GOLD + "MoreDamageToTools : " + ChatColor.GRAY + moreDamageToTools);
                sender.sendMessage(ChatColor.GOLD + "InterruptIfToolBreaks : " + ChatColor.GRAY + interruptIfToolBreaks);
                sender.sendMessage(ChatColor.GOLD + "LogsMoveDown : " + ChatColor.GRAY + logsMoveDown);
                sender.sendMessage(ChatColor.GOLD + "OnlyTrees : " + ChatColor.GRAY + onlyTrees);
                if (useAnything) {
                    sender.sendMessage(ChatColor.GOLD + "AllowedTools : " + ChatColor.GRAY + "ANYTHING!");
                } else {
                    sender.sendMessage(ChatColor.GOLD + "AllowedTools : " + ChatColor.GRAY + arrayToString(allowedTools,","));
                }
            } else {
                if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("r")) {
                    // Reload settings from file
                    if (!sender.hasPermission("choptree.commands.choptree.reload")) {
                        sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
                        return false;
                    }
                    loadConfig();
                    sender.sendMessage(ChatColor.GREEN + "Reloaded settings from properties file.");
                } else if (args[0].equalsIgnoreCase("toggle")) {
                    if (args.length == 1) {
                        // List toggleable options
                        sender.sendMessage(ChatColor.RED + "You must specify an option to toggle!");
                        sender.sendMessage(ChatColor.GRAY + "(ActiveByDefault|UseAnything|MoreDamageToTools|InterruptIfToolBreaks|LogsMoveDown|OnlyTrees)");
                        return false;
                    } else {
                        // Toggle Specific Option
                        if (!sender.hasPermission("choptree.commands.choptree.toggle." + args[1].toLowerCase())) {
                            sender.sendMessage(ChatColor.RED + "You do not have permission to toggle that setting!");
                            return false;
                        }
                        if (args[1].equalsIgnoreCase("ActiveByDefault")) {
                            if (defaultActive) {
                                defaultActive = false;
                                config.set("ActiveByDefault", false);
                            } else {
                                defaultActive = true;
                                config.set("ActiveByDefault", true);
                            }
                            sender.sendMessage(ChatColor.GOLD + "ActiveByDefault set to : " + ChatColor.GRAY + defaultActive);
                        } else if (args[1].equalsIgnoreCase("UseAnything")) {
                            if (useAnything) {
                                useAnything = false;
                                config.set("UseAnything", false);
                            } else {
                                useAnything = true;
                                config.set("UseAnything", true);
                            }
                            sender.sendMessage(ChatColor.GOLD + "UseAnything set to : " + ChatColor.GRAY + useAnything);
                        } else if (args[1].equalsIgnoreCase("MoreDamageToTools")) {
                            if (moreDamageToTools) {
                                moreDamageToTools = false;
                                config.set("MoreDamageToTools", false);
                            } else {
                                moreDamageToTools = true;
                                config.set("MoreDamageToTools", true);
                            }
                            sender.sendMessage(ChatColor.GOLD + "MoreDamageToTools set to : " + ChatColor.GRAY + moreDamageToTools);
                        } else if (args[1].equalsIgnoreCase("InterruptIfToolBreaks")) {
                            if (interruptIfToolBreaks) {
                                interruptIfToolBreaks = false;
                                config.set("InterruptIfToolBreaks", false);
                            } else {
                                interruptIfToolBreaks = true;
                                config.set("InterruptIfToolBreaks", true);
                            }
                            sender.sendMessage(ChatColor.GOLD + "InterruptIfToolBreaks set to : " + ChatColor.GRAY + interruptIfToolBreaks);
                        } else if (args[1].equalsIgnoreCase("LogsMoveDown")) {
                            if (logsMoveDown) {
                                logsMoveDown = false;
                                config.set("LogsMoveDown", false);
                            } else {
                                logsMoveDown = true;
                                config.set("LogsMoveDown", true);
                            }
                            sender.sendMessage(ChatColor.GOLD + "LogsMoveDown set to : " + ChatColor.GRAY + logsMoveDown);
                        } else if (args[1].equalsIgnoreCase("OnlyTrees")) {
                            if (onlyTrees) {
                                onlyTrees = false;
                                config.set("OnlyTrees", false);
                            } else {
                                onlyTrees = true;
                                config.set("OnlyTrees", true);
                            }
                            sender.sendMessage(ChatColor.GOLD + "OnlyTrees set to : " + ChatColor.GRAY + onlyTrees);
                        }
                        saveConfig();
                    }
                }
            }
        }
        return true;
    }
    
    private String arrayToString(String[] array, String separator) {
        String outString = "";
        for (String string : array) {
            if (!"".equals(outString)) {
                outString += separator;
            }
            outString += string;
        }
        return outString;
    }
    
    public void loadConfig() {
        config = getConfig();
        defaultActive = config.getBoolean("ActiveByDefault", true);
        config.set("ActiveByDefault", defaultActive);
        useAnything = config.getBoolean("UseAnything", false);
        config.set("UseAnything", useAnything);
        allowedTools = config.getString("AllowedTools", "WOOD_AXE,STONE_AXE,IRON_AXE,GOLD_AXE,DIAMOND_AXE").split(",");
        config.set("AllowedTools", arrayToString(allowedTools,","));
        moreDamageToTools = config.getBoolean("MoreDamageToTools", false);
        config.set("MoreDamageToTools", moreDamageToTools);
        interruptIfToolBreaks = config.getBoolean("InterruptIfToolBreaks", false);
        config.set("InterruptIfToolBreaks", interruptIfToolBreaks);
        logsMoveDown = config.getBoolean("LogsMoveDown", false);
        config.set("LogsMoveDown", logsMoveDown);
        onlyTrees = config.getBoolean("OnlyTrees", true);
        config.set("OnlyTrees", onlyTrees);
        saveConfig();
    }
    
    public void loadPlayers() {
        if (playerFile == null) {
            playerFile = new File(getDataFolder(),"players.yml");
        }
        playersDb = YamlConfiguration.loadConfiguration(playerFile);
    }

    public FileConfiguration getPlayers() {
        if (playersDb == null) {
            loadPlayers();
        }
        return playersDb;
    }

    public void savePlayers() {
        if (playersDb == null || playerFile == null) {
            return;
        }
        try {
            playersDb.save(playerFile);
        } catch (IOException ex) {
            String message = "Could not save " + playerFile;
            getLogger().severe(message);
        }
    }
    
}
