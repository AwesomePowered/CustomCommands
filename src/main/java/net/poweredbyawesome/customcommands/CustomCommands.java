package net.poweredbyawesome.customcommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class CustomCommands extends JavaPlugin implements Listener {

    public ArrayList<String> cmds = new ArrayList<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        loadCommands();
    }

    public void loadCommands() {
        for (String s : getConfig().getKeys(false)) {
            cmds.add(s.toLowerCase());
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent ev) {
        String command = ev.getMessage().replace("/","");
        if (cmds.contains(command)) {
            ev.setCancelled(true);
            for (String s : getConfig().getStringList(command)) {
                ev.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', s));
            }
        }
    }
}