package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
import net.minecraft.world.item.equipment.trim.TrimMaterial;

public class LighterEndTrimming {

  public static final ResourceKey<TrimMaterial> AURORA = ResourceKey.create(
      Registries.TRIM_MATERIAL, LighterEnd.of("aurora")
  );

  public static void bootstrap(BootstrapContext<TrimMaterial> registerable) {

    registerable.register(
        AURORA,
        new TrimMaterial(
            MaterialAssetGroup.create("aurora"),
            Component.translatable(Util.makeDescriptionId("trim_material", AURORA.identifier()))
                .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#a791fe").getOrThrow()))
        )
    );
  }

}
