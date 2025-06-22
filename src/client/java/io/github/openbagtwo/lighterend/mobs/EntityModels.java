package io.github.openbagtwo.lighterend.mobs;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.mobs.models.CubozoaModel;
import io.github.openbagtwo.lighterend.mobs.models.DragonflyModel;
import io.github.openbagtwo.lighterend.mobs.models.EndFishModel;
import io.github.openbagtwo.lighterend.mobs.models.EndSlimeModel;
import io.github.openbagtwo.lighterend.mobs.models.SilkMothModel;
import io.github.openbagtwo.lighterend.mobs.renderers.CubozoaRenderer;
import io.github.openbagtwo.lighterend.mobs.renderers.DragonflyRenderer;
import io.github.openbagtwo.lighterend.mobs.renderers.EndFishRenderer;
import io.github.openbagtwo.lighterend.mobs.renderers.EndSlimeRenderer;
import io.github.openbagtwo.lighterend.mobs.renderers.GlossyMooshroomRenderer;
import io.github.openbagtwo.lighterend.mobs.renderers.SilkMothRenderer;
import io.github.openbagtwo.lighterend.registries.LighterEndMobs;
import java.util.function.Function;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.CowEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.EntityType;

public class EntityModels {

  public static final EntityModelLayer SILK_MOTH_MODEL = makeLayer("silk_moth");
  public static final EntityModelLayer SILK_MOTH_BABY = makeLayer("silk_moth_baby");
  public static final EntityModelLayer DRAGONFLY_MODEL = makeLayer("dragonfly");
  public static final EntityModelLayer END_FISH_MODEL = makeLayer("end_fish");
  public static final EntityModelLayer CUBOZOA_MODEL = makeLayer("cubozoa");
  public static final EntityModelLayer END_SLIME_MODEL = makeLayer("end_slime");
  public static final EntityModelLayer END_SLIME_SHELL_MODEL = makeLayer("end_slime_shell");

  public static final EntityModelLayer MOOSHROOM_MODEL = makeLayer("mooshroom");

  public static void initialize() {
    EntityModelLayerRegistry.registerModelLayer(EntityModels.SILK_MOTH_MODEL,
        SilkMothModel::getTexturedModelData);
    register(LighterEndMobs.SILK_MOTH.mob, SilkMothRenderer::new);

    EntityModelLayerRegistry.registerModelLayer(SILK_MOTH_BABY,
        () -> SilkMothModel.getTexturedModelData().transform(SilkMothModel.BABY_TRANSFORMER));

    EntityModelLayerRegistry.registerModelLayer(EntityModels.DRAGONFLY_MODEL,
        DragonflyModel::getTexturedModelData);
    register(LighterEndMobs.DRAGONFLY.mob, DragonflyRenderer::new);

    EntityModelLayerRegistry.registerModelLayer(EntityModels.END_FISH_MODEL,
        EndFishModel::getTexturedModelData);
    register(LighterEndMobs.END_FISH.mob, EndFishRenderer::new);

    EntityModelLayerRegistry.registerModelLayer(EntityModels.CUBOZOA_MODEL,
        CubozoaModel::getTexturedModelData);
    register(LighterEndMobs.CUBOZOA.mob, CubozoaRenderer::new);

    EntityModelLayerRegistry.registerModelLayer(END_SLIME_MODEL,
        EndSlimeModel::getInnerTexturedModelData);
    EntityModelLayerRegistry.registerModelLayer(
        END_SLIME_SHELL_MODEL,
        EndSlimeModel::getOuterTexturedModelData
    );
    register(LighterEndMobs.END_SLIME.mob, EndSlimeRenderer::new);

    EntityModelLayerRegistry.registerModelLayer(EntityModels.MOOSHROOM_MODEL,
        CowEntityModel::getTexturedModelData);
    register(LighterEndMobs.MOOSHROOM.mob, GlossyMooshroomRenderer::new);
  }

  private static void register(EntityType<?> type, Function<Context, MobEntityRenderer> renderer) {
    EntityRendererRegistry.register(type, renderer::apply);
  }

  private static EntityModelLayer makeLayer(String name) {
    return new EntityModelLayer(LighterEnd.of(name), "main");
  }
}
