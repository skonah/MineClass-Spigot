package net.babamod.mineclass.listeners;

import net.babamod.mineclass.Mineclass;
import net.babamod.mineclass.classes.ClassWrapper;
import net.babamod.mineclass.utils.AppliedStatus;
import net.babamod.mineclass.utils.ApplyClassStatusTask;
import net.babamod.mineclass.utils.ClassItemPossessed;
import net.babamod.mineclass.utils.SmeltingEngine;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

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
    if (!ClassWrapper.reapplyRightClassEffects(player, true)) {
      player.sendMessage(
          "Hello ! The amazing MineClass mod is available on this server ! You can pick a class with the /class command.");
    }
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
      if (ClassWrapper.isItemEnchantable(player, event.getItem().getItemStack().getType())) {
        ClassWrapper.enchantItem(player, event.getItem().getItemStack());
      }
    }
  }

  @EventHandler
  public void on(PlayerDeathEvent event) {
    List<ItemStack> itemStackList =
        event.getDrops().stream().filter(ClassWrapper::isSoulBound).collect(Collectors.toList());
    event.getDrops().removeAll(itemStackList);
    ClassItemPossessed.getInstance().addItems(event.getEntity().getName(), itemStackList);
  }

  @EventHandler
  public void on(PlayerRespawnEvent event) {
    new ApplyClassStatusTask(this.plugin, event.getPlayer()).runTaskLater(this.plugin, 10);
    ClassItemPossessed.getInstance()
        .getItems(event.getPlayer().getName())
        .forEach(itemStack -> event.getPlayer().getInventory().addItem(itemStack));
    ClassItemPossessed.getInstance().clearItems(event.getPlayer().getName());
  }

  @EventHandler
  public void on(InventoryClickEvent event) {
    if ((event.getAction().equals(InventoryAction.PICKUP_ALL)
            || event.getAction().equals(InventoryAction.PICKUP_HALF)
            || event.getAction().equals(InventoryAction.PICKUP_ONE)
            || event.getAction().equals(InventoryAction.PICKUP_SOME))
        && event.getWhoClicked() instanceof Player) {
      if (isForbiddenItem(event)) {
        event.setCancelled(true);
        return;
      }
      enchantItem(event);
    }

    if ((event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY))
        && event.getWhoClicked() instanceof Player) {
      if (isForbiddenItem(event)) {
        event.setCancelled(true);
        return;
      }
      enchantItem(event);
    }
  }

  private boolean isForbiddenItem(InventoryClickEvent event) {
    Player player = (Player) event.getWhoClicked();
    if (AppliedStatus.getInstance().hasAClass(player.getName())) {
      if (event.getCurrentItem() != null
          && ClassWrapper.isItemForbidden(player, event.getCurrentItem().getType())) {
        return true;
      }
      return event.getCursor() != null
          && ClassWrapper.isItemForbidden(player, event.getCursor().getType());
    }
    return false;
  }

  private void enchantItem(InventoryClickEvent event) {
    Player player = (Player) event.getWhoClicked();
    if (AppliedStatus.getInstance().hasAClass(player.getName())) {
      if (event.getCurrentItem() != null && !ClassWrapper.isSoulBound(event.getCurrentItem())) {
        ClassWrapper.enchantItem(player, event.getCurrentItem());
      }
      if (event.getCursor() != null && !ClassWrapper.isSoulBound(event.getCursor())) {
        ClassWrapper.enchantItem(player, event.getCursor());
      }
    }
  }

  @EventHandler
  public void on(BlockDropItemEvent event) {
    Player player = event.getPlayer();
    if (AppliedStatus.getInstance().isFireDwarf(player.getName())) {
      event
          .getItems()
          .forEach(
              item -> {
                ItemStack smelted =
                    SmeltingEngine.getInstance()
                        .smelt(player, event.getBlock().getLocation(), item.getItemStack());
                if (smelted != null) {
                  item.setItemStack(smelted);
                }
              });
    }
  }

  @EventHandler
  public void on(EntityShootBowEvent event) {
    if (event.getBow() != null && event.getBow().getType().equals(Material.CROSSBOW)) {
      if (event.getEntity() instanceof Player) {
        Player player = (Player) event.getEntity();
        if (event.getBow().getEnchantments().containsKey(Enchantment.ARROW_INFINITE)) {
          player.getInventory().addItem(new ItemStack(Material.ARROW));
          ((AbstractArrow)event.getProjectile()).setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        }
        if (AppliedStatus.getInstance().isFireDwarf(player.getName())) {
          event.getProjectile().setFireTicks(10000);
        }
      }
    }
  }
}
