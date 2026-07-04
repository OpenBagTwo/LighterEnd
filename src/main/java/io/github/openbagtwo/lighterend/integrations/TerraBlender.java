package io.github.openbagtwo.lighterend.integrations;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.registries.LighterEndBiomes;
import io.github.openbagtwo.lighterend.world.gen.LighterEndWorldGen;
import terrablender.api.EndBiomeRegistry;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.SurfaceRuleManager.RuleCategory;
import terrablender.api.TerraBlenderApi;

public class TerraBlender implements TerraBlenderApi {

  @Override
  public void onTerraBlenderInitialized() {
    if (!LighterEnd.CONFIG.generateBiomes()) {
      return;
    }

    EndBiomeRegistry.registerHighlandsBiome(LighterEndBiomes.GLOWING_GRASSLAND, 10);
    EndBiomeRegistry.registerMidlandsBiome(LighterEndBiomes.GLOWING_GRASSLAND, 10);
    EndBiomeRegistry.registerIslandBiome(LighterEndBiomes.GLOWING_GRASSLAND, 5);

    EndBiomeRegistry.registerHighlandsBiome(LighterEndBiomes.BLOSSOM_FOREST, 5);

    EndBiomeRegistry.registerHighlandsBiome(LighterEndBiomes.SHADOW_FOREST, 5);

    EndBiomeRegistry.registerHighlandsBiome(LighterEndBiomes.FOGGY_MUSHROOMLANDS, 2);

    EndBiomeRegistry.registerHighlandsBiome(LighterEndBiomes.UMBRELLA_JUNGLE, 5);
    EndBiomeRegistry.registerMidlandsBiome(LighterEndBiomes.UMBRELLA_JUNGLE, 3);

    EndBiomeRegistry.registerHighlandsBiome(LighterEndBiomes.UMBRA_VALLEY, 5);
    EndBiomeRegistry.registerMidlandsBiome(LighterEndBiomes.UMBRA_VALLEY, 5);

    EndBiomeRegistry.registerHighlandsBiome(LighterEndBiomes.SULPHUR_SPRINGS, 2);
    EndBiomeRegistry.registerMidlandsBiome(LighterEndBiomes.SULPHUR_SPRINGS, 2);

    EndBiomeRegistry.registerHighlandsBiome(LighterEndBiomes.MEGALAKE, 1);

    EndBiomeRegistry.registerIslandBiome(LighterEndBiomes.STARFIELD, 1);
    EndBiomeRegistry.registerEdgeBiome(LighterEndBiomes.STARFIELD, 2);

    SurfaceRuleManager.addSurfaceRules(
        RuleCategory.END,
        LighterEnd.MOD_ID,
        biomes -> LighterEndWorldGen.updateSurfaceRules(biomes)
    );

    LighterEnd.LOGGER.info("Registered " + LighterEnd.MOD_NAME + "'s biomes with TerraBlender");
  }
}
