package io.github.openbagtwo.lighterend.mobs;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.mobs.models.CrabModel;
import io.github.openbagtwo.lighterend.mobs.models.CubozoaModel;
import io.github.openbagtwo.lighterend.mobs.models.DragonflyModel;
import io.github.openbagtwo.lighterend.mobs.models.EndFishModel;
import io.github.openbagtwo.lighterend.mobs.models.EndSlimeModel;
import io.github.openbagtwo.lighterend.mobs.models.SilkMothModel;
import io.github.openbagtwo.lighterend.mobs.renderers.CrabRenderer;
import io.github.openbagtwo.lighterend.mobs.renderers.CubozoaRenderer;
import io.github.openbagtwo.lighterend.mobs.renderers.DragonflyRenderer;
import io.github.openbagtwo.lighterend.mobs.renderers.EndFishRenderer;
import io.github.openbagtwo.lighterend.mobs.renderers.EndSlimeRenderer;
import io.github.openbagtwo.lighterend.mobs.renderers.GlossyMooshroomRenderer;
import io.github.openbagtwo.lighterend.mobs.renderers.SilkMothRenderer;
import io.github.openbagtwo.lighterend.registries.LighterEndMobs;
import java.util.function.Function;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.animal.cow.CowModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.EntityType;

public class EntityModels {

  public static final ModelLayerLocation SILK_MOTH_MODEL = makeLayer("silk_moth");
  public static final ModelLayerLocation SILK_MOTH_BABY = makeLayer("silk_moth_baby");
  public static final ModelLayerLocation DRAGONFLY_MODEL = makeLayer("dragonfly");
  public static final ModelLayerLocation END_FISH_MODEL = makeLayer("end_fish");
  public static final ModelLayerLocation CUBOZOA_MODEL = makeLayer("cubozoa");
  public static final ModelLayerLocation END_SLIME_MODEL = makeLayer("end_slime");
  public static final ModelLayerLocation END_SLIME_SHELL_MODEL = makeLayer("end_slime_shell");
  public static final ModelLayerLocation MOOSHROOM_MODEL = makeLayer("mooshroom");
  public static final ModelLayerLocation CRAB_MODEL = makeLayer("chorus_crab");
  public static final ModelLayerLocation CRAB_BABY = makeLayer("chorus_crab_baby");

  public static void initialize() {
    ModelLayerRegistry.registerModelLayer(EntityModels.SILK_MOTH_MODEL,
        SilkMothModel::getTexturedModelData);
    register(LighterEndMobs.SILK_MOTH.mob, SilkMothRenderer::new);

    ModelLayerRegistry.registerModelLayer(SILK_MOTH_BABY,
        () -> SilkMothModel.getTexturedModelData().apply(SilkMothModel.BABY_TRANSFORMER));

    ModelLayerRegistry.registerModelLayer(EntityModels.DRAGONFLY_MODEL,
        DragonflyModel::getTexturedModelData);
    register(LighterEndMobs.DRAGONFLY.mob, DragonflyRenderer::new);

    ModelLayerRegistry.registerModelLayer(EntityModels.END_FISH_MODEL,
        EndFishModel::getTexturedModelData);
    register(LighterEndMobs.END_FISH.mob, EndFishRenderer::new);

    ModelLayerRegistry.registerModelLayer(EntityModels.CUBOZOA_MODEL,
        CubozoaModel::getTexturedModelData);
    register(LighterEndMobs.CUBOZOA.mob, CubozoaRenderer::new);

    ModelLayerRegistry.registerModelLayer(END_SLIME_MODEL,
        EndSlimeModel::getInnerTexturedModelData);
    ModelLayerRegistry.registerModelLayer(
        END_SLIME_SHELL_MODEL,
        EndSlimeModel::getOuterTexturedModelData
    );
    register(LighterEndMobs.END_SLIME.mob, EndSlimeRenderer::new);

    ModelLayerRegistry.registerModelLayer(EntityModels.MOOSHROOM_MODEL,
        CowModel::createBodyLayer);
    register(LighterEndMobs.MOOSHROOM.mob, GlossyMooshroomRenderer::new);

    ModelLayerRegistry.registerModelLayer(EntityModels.CRAB_MODEL,
        CrabModel::getTexturedModelData);
    register(LighterEndMobs.CHORUS_CRAB.mob, CrabRenderer::new);

    ModelLayerRegistry.registerModelLayer(CRAB_BABY,
        () -> CrabModel.getTexturedModelData().apply(CrabModel.BABY_TRANSFORMER));
  }

  private static void register(EntityType<?> type, Function<Context, MobRenderer> renderer) {
    EntityRendererRegistry.register(type, renderer::apply);
  }

  private static ModelLayerLocation makeLayer(String name) {
    return new ModelLayerLocation(LighterEnd.of(name), "main");
  }
}
