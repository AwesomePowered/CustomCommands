package net.poweredbyawesome.customcommands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
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
                if (s.startsWith("/") || s.startsWith("\\")) {
                    if (s.startsWith("/")) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), format(s.replace("/", ""), ev.getPlayer()));
                    }
                    if (s.startsWith("\\")) {
                        ev.getPlayer().performCommand(format(s.replace("\\", ""), ev.getPlayer()));
                    }
                } else {
                    if (s.contains("<*>")) {
                        String multiplier = s.split("<*>")[1];
                        if (StringUtils.isNumeric(multiplier)) {
                            for (int i = 0; i < Integer.valueOf(s.split("<*>")[1]); i++) {
                                ev.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', format(s.replace("<*>","").replace(multiplier, ""), ev.getPlayer())));
                            }
                        }
                    } else {
                        ev.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', format(s, ev.getPlayer())));
                    }
                }
            }
        }
    }

    public String format(String message, Player player) {
        message = message.replace("<player>", player.getName());
        message = message.replace("<health>", String.valueOf(player.getHealth()));
        message = message.replace("<hunger>", String.valueOf(player.getFoodLevel()));
        return message;
    }
}