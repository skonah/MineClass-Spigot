package net.babamod.mineclass.classes;

import net.babamod.mineclass.utils.AppliedStatus;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class MineClassFactory {
  /** Instance unique pré-initialisée */
  private static MineClassFactory INSTANCE;

  private final Map<String, MineClass> availableClasses;

  /** Constructeur privé */
  private MineClassFactory() {
    availableClasses = new HashMap<>();
    availableClasses.put("dwarf", new DwarfClass());
    availableClasses.put("elf", new ElfClass());
    availableClasses.put("fire_dwarf", new FireDwarfClass());
    availableClasses.put("ender_elf", new EnderElfClass());
  }

  /** Point d'accès pour l'instance unique du singleton */
  public static synchronized MineClassFactory getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new MineClassFactory();
    }

    return INSTANCE;
  }

  public static boolean isSoulBound(ItemStack itemStack) {
    if (itemStack.getItemMeta() != null && itemStack.getItemMeta().getLore() != null) {
      return itemStack.getItemMeta().getLore().contains("Soulbound");
    }
    return false;
  }

  public static void clearAllClassEffects(Player player) {
    for (PotionEffect activePotionEffect : player.getActivePotionEffects()) {
      if (activePotionEffect.getDuration() > 32766) {
        player.removePotionEffect(activePotionEffect.getType());
      }
    }
  }

  public static void setUnbreakableAndSoulbound(ItemStack itemStack) {
    if (itemStack.getItemMeta() != null) {
      ItemMeta itemMeta = itemStack.getItemMeta();
      itemMeta.setUnbreakable(true);
      itemMeta.setLore(Collections.singletonList("Soulbound"));
      itemStack.setItemMeta(itemMeta);
    }
  }

  public synchronized Set<String> getAvailableClassCodes() {
    return availableClasses.keySet();
  }

  public synchronized Optional<MineClass> getRightClass(Player player) {
    for (Map.Entry<String, MineClass> stringMineClassEntry : availableClasses.entrySet()) {
      if (AppliedStatus.getInstance().getStatus(player).equals(stringMineClassEntry.getKey())
          || stringMineClassEntry.getValue().is(player)) {
        return Optional.of(stringMineClassEntry.getValue());
      }
    }
    return Optional.empty();
  }

  public void reapplyEffectsByCode(String code, Player player) {
    availableClasses.get(code).reapplyEffects(player);
  }

  public void giveItemsForClassByCode(String code, Player player) {
    availableClasses.get(code).giveItems(player);
  }
}
