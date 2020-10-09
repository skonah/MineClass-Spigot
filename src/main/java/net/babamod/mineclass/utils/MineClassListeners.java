package net.babamod.mineclass.utils;

import net.babamod.mineclass.Mineclass;
import net.babamod.mineclass.classes.ClassWrapper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class MineClassListeners implements Listener {

    private final Mineclass plugin;

    public MineClassListeners(Mineclass plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ClassWrapper.reapplyRightClassEffects(player, true);
    }

    @EventHandler
    public void on(PlayerItemConsumeEvent event) {
        if (event.getItem().getType().equals(Material.MILK_BUCKET)) {
            if (AppliedStatus.getInstance().hasAClass(event.getPlayer().getName())) {
                new ApplyClassStatusTask(this.plugin, event.getPlayer()).runTaskLater(this.plugin, 10);
            }
        }
    }

    @EventHandler
    public void on(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (ClassWrapper.isItemForbidden(player, event.getItem().getItemStack().getType())) {
                event.setCancelled(true);
            }
        }
    }
}
