package net.babamod.mineclass.classes;

import net.babamod.mineclass.utils.AppliedStatus;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

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
}
