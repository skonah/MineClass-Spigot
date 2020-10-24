package net.babamod.mineclass.listeners;

import net.babamod.mineclass.Mineclass;
import net.babamod.mineclass.classes.MineClass;
import net.babamod.mineclass.classes.MineClassFactory;
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
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
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
    Optional<MineClass> mineClass = MineClassFactory.getInstance().getRightClass(player);
    if (mineClass.isPresent()) {
      mineClass.get().reapplyEffects(player);
      player.sendMessage(
          String.format("Reminder : You are a %s.", mineClass.get().getCode()));
    } else {
      player.sendMessage(
          "Hello ! The amazing MineClass mod is available on this server ! You can pick a class with the /class command.");
    }
  }

  @EventHandler
  public void on(PlayerItemConsumeEvent event) {
    if (event.getItem().getType().equals(Material.MILK_BUCKET)) {
      if (AppliedStatus.getInstance().hasAClass(event.getPlayer().getName())) {
        new ApplyClassStatusTask(event.getPlayer()).runTaskLater(this.plugin, 10);
      }
    }
  }

  @EventHandler
  public void on(EntityPickupItemEvent event) {
    if (event.getEntity() instanceof Player) {
      Player player = (Player) event.getEntity();
      Optional<MineClass> mineClass = MineClassFactory.getInstance().getRightClass(player);
      if (mineClass.isPresent()) {
        if (mineClass.get().isItemForbidden(event.getItem().getItemStack().getType())) {
          event.setCancelled(true);
        }
        if (mineClass.get().isItemEnchantable(event.getItem().getItemStack().getType())) {
          mineClass.get().enchantItem(event.getItem().getItemStack());
        }
      }
    }
  }

  @EventHandler
  public void on(PlayerDeathEvent event) {
    List<ItemStack> itemStackList =
        event.getDrops().stream()
            .filter(MineClassFactory::isSoulBound)
            .collect(Collectors.toList());
    event.getDrops().removeAll(itemStackList);
    ClassItemPossessed.getInstance().addItems(event.getEntity().getName(), itemStackList);
  }

  @EventHandler
  public void on(PlayerRespawnEvent event) {
    new ApplyClassStatusTask(event.getPlayer()).runTaskLater(this.plugin, 10);
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
    Optional<MineClass> mineClass = MineClassFactory.getInstance().getRightClass(player);
    if (mineClass.isPresent() && AppliedStatus.getInstance().hasAClass(player.getName())) {
      if (event.getCurrentItem() != null
          && mineClass.get().isItemForbidden(event.getCurrentItem().getType())) {
        return true;
      }
      return event.getCursor() != null
          && mineClass.get().isItemForbidden(event.getCursor().getType());
    }
    return false;
  }

  private void enchantItem(InventoryClickEvent event) {
    Player player = (Player) event.getWhoClicked();
    Optional<MineClass> mineClass = MineClassFactory.getInstance().getRightClass(player);
    if (mineClass.isPresent() && AppliedStatus.getInstance().hasAClass(player.getName())) {
      if (event.getCurrentItem() != null && !MineClassFactory.isSoulBound(event.getCurrentItem())) {
        mineClass.get().enchantItem(event.getCurrentItem());
      }
      if (event.getCursor() != null && !MineClassFactory.isSoulBound(event.getCursor())) {
        mineClass.get().enchantItem(event.getCursor());
      }
    }
  }

  @EventHandler
  public void on(BlockDropItemEvent event) {
    Player player = event.getPlayer();
    if (AppliedStatus.getInstance().getStatus(player.getName()).equals("fire_dwarf")) {
      event
          .getItems()
          .forEach(
              item -> SmeltingEngine.getInstance()
                  .smelt(player, event.getBlock().getLocation(), item.getItemStack())
                  .ifPresent(item::setItemStack));
    }
  }

  @EventHandler
  public void on(EntityShootBowEvent event) {
    if (event.getBow() != null && event.getBow().getType().equals(Material.CROSSBOW)) {
      if (event.getEntity() instanceof Player) {
        Player player = (Player) event.getEntity();
        if (event.getProjectile() instanceof AbstractArrow
            && event.getBow().getEnchantments().containsKey(Enchantment.ARROW_INFINITE)) {
          player.getInventory().addItem(new ItemStack(Material.ARROW));
          ((AbstractArrow) event.getProjectile())
              .setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        }
        if (AppliedStatus.getInstance().getStatus(player.getName()).equals("fire_dwarf")) {
          event.getProjectile().setFireTicks(10000);
        }
      }
    }
  }

  @EventHandler
  public void on(EntityDamageEvent event) {
    if (event.getEntity() instanceof Player) {
      Player player = (Player) event.getEntity();
      if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)
          && AppliedStatus.getInstance().getStatus(player.getName()).equals("elf")) {
        event.setCancelled(true);
      }
    }
  }

  @EventHandler
  public void on(FoodLevelChangeEvent event) {
    if (event.getEntity() instanceof Player) {
      Player player = (Player) event.getEntity();
      if (AppliedStatus.getInstance().getStatus(player.getName()).equals("elf")) {
        event.setCancelled(true);
      }
    }
  }
}
