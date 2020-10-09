package net.babamod.mineclass.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public class AppliedStatus implements Serializable {
  /** Instance unique pré-initialisée */
  private static AppliedStatus INSTANCE;

  private final HashMap<String, Boolean> dwarf;
  private final HashMap<String, Boolean> elf;
  private final HashMap<String, Boolean> fireDwarf;
  private final HashMap<String, Boolean> naga;

  /** Constructeur privé */
  private AppliedStatus() {
    dwarf = new HashMap<>();
    elf = new HashMap<>();
    fireDwarf = new HashMap<>();
    naga = new HashMap<>();
  }

  /** Point d'accès pour l'instance unique du singleton */
  public static synchronized AppliedStatus getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new AppliedStatus();
    }

    return INSTANCE;
  }

  public void setDwarf(String playerName, boolean dwarf) {
    this.dwarf.put(playerName, dwarf);
  }

  public void setElf(String playerName, boolean elf) {
    this.elf.put(playerName, elf);
  }

  public void setFireDwarf(String playerName, boolean fireDwarf) {
    this.fireDwarf.put(playerName, fireDwarf);
  }

  public void setNaga(String playerName, boolean naga) {
    this.naga.put(playerName, naga);
  }

  public boolean isDwarf(String playerName) {
    return dwarf.getOrDefault(playerName, false);
  }

  public boolean isElf(String playerName) {
    return elf.getOrDefault(playerName, false);
  }

  public boolean isFireDwarf(String playerName) {
    return fireDwarf.getOrDefault(playerName, false);
  }

  public boolean isNaga(String playerName) {
    return naga.getOrDefault(playerName, false);
  }

  public boolean hasAClass(String playerName) {
    return dwarf.getOrDefault(playerName, false)
        || elf.getOrDefault(playerName, false)
        || fireDwarf.getOrDefault(playerName, false)
        || naga.getOrDefault(playerName, false);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AppliedStatus that = (AppliedStatus) o;
    return Objects.equals(dwarf, that.dwarf)
        && Objects.equals(elf, that.elf)
        && Objects.equals(fireDwarf, that.fireDwarf)
        && Objects.equals(naga, that.naga);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dwarf, elf, fireDwarf, naga);
  }

  @Override
  public String toString() {
    return "AppliedStatus{"
        + "dwarf="
        + dwarf
        + ", elf="
        + elf
        + ", fireDwarf="
        + fireDwarf
        + ", naga="
        + naga
        + '}';
  }
}
