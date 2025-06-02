package io.github.openbagtwo.lighterend.world.gen;

import static net.minecraft.world.gen.surfacebuilder.MaterialRules.STONE_DEPTH_FLOOR;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.biome;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.block;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.condition;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.noiseThreshold;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.sequence;

import io.github.openbagtwo.lighterend.config.Config;
import io.github.openbagtwo.lighterend.registries.LighterEndBiomes;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.world.gen.noise.NoiseParameters;
import net.fabricmc.fabric.api.biome.v1.TheEndBiomes;
import net.minecraft.world.gen.surfacebuilder.MaterialRules.MaterialRule;

public class LighterEndWorldGen {

  public static void modifyWorldGen(Config config) {
    TheEndBiomes.addHighlandsBiome(LighterEndBiomes.BLOSSOM_FOREST, 0.5);
  }

  public static MaterialRule updateSurfaceRules() {
    return sequence(
        condition(
            STONE_DEPTH_FLOOR,
            sequence(
                condition(
                    biome(LighterEndBiomes.BLOSSOM_FOREST),
                    sequence(
                        condition(
                            noiseThreshold(NoiseParameters.END_MOSS_SURFACE, -0.5, 0.5),
                            block(LighterEndBlocks.END_MOSS.getDefaultState())
                        )
                    )
                )
            )
        )
    );
  }

}
