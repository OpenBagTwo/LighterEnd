package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.item.equipment.trim.ArmorTrimAssets;
import net.minecraft.item.equipment.trim.ArmorTrimMaterial;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Util;

public class LighterEndTrimming {

  public static final RegistryKey<ArmorTrimMaterial> AURORA = RegistryKey.of(
      RegistryKeys.TRIM_MATERIAL, LighterEnd.of("aurora")
  );

  public static void bootstrap(Registerable<ArmorTrimMaterial> registerable) {

    registerable.register(
        AURORA,
        new ArmorTrimMaterial(
            ArmorTrimAssets.of("aurora"),
            Text.translatable(Util.createTranslationKey("trim_material", AURORA.getValue()))
                .fillStyle(Style.EMPTY.withColor(TextColor.parse("#a791fe").getOrThrow()))
        )
    );
  }

}
