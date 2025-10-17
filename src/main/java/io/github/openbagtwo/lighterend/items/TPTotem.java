package io.github.openbagtwo.lighterend.items;

import net.minecraft.item.Item;
import net.minecraft.util.Rarity;

public class TPTotem extends Item {

  public TPTotem(Settings settings) {
    super(
        settings
            .maxCount(1)
            .rarity(Rarity.UNCOMMON)
    );
  }
}
