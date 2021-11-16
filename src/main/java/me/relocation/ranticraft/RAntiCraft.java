package me.relocation.ranticraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class RAntiCraft extends JavaPlugin implements Listener {

    private Set<Material> blockedMaterials;
    private String permission;
    private List<String> noPermissionMessage;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.blockedMaterials = new HashSet<>();
        this.permission = getConfig().getString("bypass-permission");
        this.noPermissionMessage = colorizeList(getConfig().getStringList("no-permission-message"));

        for (String material : getConfig().getStringList("blocked-materials")) {
            this.blockedMaterials.add(Material.valueOf(material));
        }

        Bukkit.getPluginManager().registerEvents(this, this);

    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {

        final Player player = (Player) event.getWhoClicked();
        final ItemStack item = event.getCurrentItem();

        if (!this.blockedMaterials.contains(item.getType())) {
            return;
        }

        if (player.hasPermission(permission)) {
            return;
        }

        event.setCancelled(true);

        for (String line : this.noPermissionMessage) {
            player.sendMessage(line);
        }

    }

    public List<String> colorizeList(List<String> list) {

        for (int i = 0; i < list.size(); i++) {
            list.set(i, ChatColor.translateAlternateColorCodes('&', list.get(i)));
        }

        return list;
    }

}
