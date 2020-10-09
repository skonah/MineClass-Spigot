package net.babamod.mineclass.utils;

import net.babamod.mineclass.Mineclass;
import net.babamod.mineclass.classes.ClassWrapper;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
      player.sendMessage("You picked up " + event.getItem().getItemStack().getType());
      if (ClassWrapper.isItemForbidden(player, event.getItem().getItemStack().getType())) {
        event.setCancelled(true);
      }
      player.sendMessage(
          String.valueOf(
              ClassWrapper.isItemEnchantable(player, event.getItem().getItemStack().getType())));
      if (ClassWrapper.isItemEnchantable(player, event.getItem().getItemStack().getType())) {
        player.sendMessage("Enchantable item !");
        ClassWrapper.enchantItem(player, event.getItem());
      }
    }
  }

  @EventHandler
  public void on(PlayerDeathEvent event) {
    List<ItemStack> itemStackList =
        event.getDrops().stream().filter(ClassWrapper::isSoulBound).collect(Collectors.toList());
    event.getDrops().removeAll(itemStackList);
    ClassWrapper.removePlayerClassItem(event.getEntity());
  }

  @EventHandler
  public void on(PlayerRespawnEvent event) {
    ClassWrapper.givePlayerClassItem(event.getPlayer());
    new ApplyClassStatusTask(this.plugin, event.getPlayer()).runTaskLater(this.plugin, 10);
  }

  @EventHandler
  public void on(PlayerDropItemEvent event) {
    if (ClassWrapper.isSoulBound(event.getItemDrop().getItemStack())) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void on(InventoryClickEvent event) {
    System.out.println("InventoryClickEvent triggered !");
    System.out.println("-----------------------------------");
    System.out.println(event.getAction());
    System.out.println(event.getClick());
    System.out.println(event.getClickedInventory());
    System.out.println(event.getCurrentItem());
    System.out.println(event.getCursor());
    System.out.println(event.getSlotType());
    System.out.println(Arrays.toString(event.getHandlers().getRegisteredListeners()));
    System.out.println(event.getHotbarButton());
    System.out.println(event.getRawSlot());
    System.out.println(event.getSlot());
    System.out.println("-----------------------------------");

    if (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
      if (event.getCurrentItem() != null && ClassWrapper.isSoulBound(event.getCurrentItem())) {
        event.setCancelled(true);
        return;
      }
      if (event.getCursor() != null && ClassWrapper.isSoulBound(event.getCursor())) {
        event.setCancelled(true);
      }
    }
  }

  /*
  IMPOSSIBLE DE GIVER LES ITEMS DE CLASSES.
   */
}
