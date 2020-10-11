package net.babamod.mineclass.classes;

import net.babamod.mineclass.utils.AppliedStatus;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collections;

public class ClassWrapper {

  public static void reapplyRightClassEffects(Player player, boolean sendReminder) {
    if (AppliedStatus.getInstance().isDwarf(player.getName()) || DwarfClass.is(player)) {
      AppliedStatus.getInstance().setDwarf(player.getName(), true);
      DwarfClass.reapplyEffects(player);
      if (sendReminder) {
        player.sendMessage("Reminder : You are a dwarf.");
      }
    }
    if (AppliedStatus.getInstance().isElf(player.getName()) || ElfClass.is(player)) {
      AppliedStatus.getInstance().setElf(player.getName(), true);
      ElfClass.reapplyEffects(player);
      if (sendReminder) {
        player.sendMessage("Reminder : You are an elf.");
      }
    }
    if (AppliedStatus.getInstance().isFireDwarf(player.getName()) || FireDwarfClass.is(player)) {
      AppliedStatus.getInstance().setFireDwarf(player.getName(), true);
      FireDwarfClass.reapplyEffects(player);
      if (sendReminder) {
        player.sendMessage("Reminder : You are a fire dwarf.");
      }
    }
    if (AppliedStatus.getInstance().isNaga(player.getName()) || NagaClass.is(player)) {
      AppliedStatus.getInstance().setNaga(player.getName(), true);
      NagaClass.reapplyEffects(player);
      if (sendReminder) {
        player.sendMessage("Reminder : You are a naga.");
      }
    }
  }

  public static void clearAllClassEffects(Player player) {
    for (PotionEffect activePotionEffect : player.getActivePotionEffects()) {
      if (activePotionEffect.getDuration() > 32766) {
        player.removePotionEffect(activePotionEffect.getType());
      }
    }
  }

  public static boolean isItemForbidden(Player player, Material type) {
    if (AppliedStatus.getInstance().isDwarf(player.getName())) {
      return DwarfClass.isItemForbidden(type);
    }
    if (AppliedStatus.getInstance().isElf(player.getName())) {
      return ElfClass.isItemForbidden(type);
    }
    if (AppliedStatus.getInstance().isFireDwarf(player.getName())) {
      return FireDwarfClass.isItemForbidden(type);
    }
    if (AppliedStatus.getInstance().isNaga(player.getName())) {
      return NagaClass.isItemForbidden(type);
    }
    return false;
  }

  public static boolean isItemEnchantable(Player player, Material type) {
    if (AppliedStatus.getInstance().isDwarf(player.getName())) {
      return DwarfClass.isItemEnchantable(type);
    }
    if (AppliedStatus.getInstance().isElf(player.getName())) {
      return ElfClass.isItemEnchantable(type);
    }
    if (AppliedStatus.getInstance().isFireDwarf(player.getName())) {
      return FireDwarfClass.isItemEnchantable(type);
    }
    if (AppliedStatus.getInstance().isNaga(player.getName())) {
      return NagaClass.isItemEnchantable(type);
    }
    return false;
  }

  public static void enchantItem(Player player, ItemStack itemStack) {
    if (AppliedStatus.getInstance().isDwarf(player.getName())) {
      if (DwarfClass.isItemEnchantable(itemStack.getType())) {
        setUnbreakableAndSoulbound(itemStack);
        DwarfClass.enchantItem(itemStack);
      }
    }
    if (AppliedStatus.getInstance().isElf(player.getName())) {
      if (ElfClass.isItemEnchantable(itemStack.getType())) {
        setUnbreakableAndSoulbound(itemStack);
        ElfClass.enchantItem(itemStack);
      }
    }
    if (AppliedStatus.getInstance().isFireDwarf(player.getName())) {
      if (FireDwarfClass.isItemEnchantable(itemStack.getType())) {
        setUnbreakableAndSoulbound(itemStack);
        FireDwarfClass.enchantItem(itemStack);
      }
    }
    if (AppliedStatus.getInstance().isNaga(player.getName())) {
      if (NagaClass.isItemEnchantable(itemStack.getType())) {
        setUnbreakableAndSoulbound(itemStack);
        NagaClass.enchantItem(itemStack);
      }
    }
  }

  public static void removeAllEnchantments(ItemStack itemStack) {
    itemStack.getEnchantments().keySet().forEach(itemStack::removeEnchantment);
    removeUnbreakableAndSoulbound(itemStack);
  }

  public static void setUnbreakableAndSoulbound(ItemStack itemStack) {
    if (itemStack.getItemMeta() != null) {
      ItemMeta itemMeta = itemStack.getItemMeta();
      itemMeta.setUnbreakable(true);
      itemMeta.setLore(Collections.singletonList("Soulbound"));
      itemStack.setItemMeta(itemMeta);
    }
  }

  public static void removeUnbreakableAndSoulbound(ItemStack itemStack) {
    if (itemStack.getItemMeta() != null) {
      ItemMeta itemMeta = itemStack.getItemMeta();
      itemMeta.setUnbreakable(false);
      itemMeta.setLore(new ArrayList<>());
      itemStack.setItemMeta(itemMeta);
    }
  }

  public static boolean isSoulBound(ItemStack itemStack) {
    if (itemStack.getItemMeta() != null && itemStack.getItemMeta().getLore() != null) {
      return itemStack.getItemMeta().getLore().contains("Soulbound");
    }
    return false;
  }
}
