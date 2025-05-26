package io.github.openbagtwo.lighterend.mobs;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.mobs.models.CubozoaModel;
import io.github.openbagtwo.lighterend.mobs.models.DragonflyModel;
import io.github.openbagtwo.lighterend.mobs.models.EndFishModel;
import io.github.openbagtwo.lighterend.mobs.models.SilkMothModel;
import io.github.openbagtwo.lighterend.mobs.renderers.CubozoaRenderer;
import io.github.openbagtwo.lighterend.mobs.renderers.DragonflyRenderer;
import io.github.openbagtwo.lighterend.mobs.renderers.EndFishRenderer;
import io.github.openbagtwo.lighterend.mobs.renderers.SilkMothRenderer;
import io.github.openbagtwo.lighterend.registries.LighterEndMobs;
import java.util.function.Function;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;

public class EntityModels {

  public static final EntityModelLayer SILK_MOTH_MODEL = makeLayer("silk_moth");
  public static final EntityModelLayer DRAGONFLY_MODEL = makeLayer("dragonfly");
  public static final EntityModelLayer END_FISH_MODEL = makeLayer("end_fish");
  public static final EntityModelLayer CUBOZOA_MODEL = makeLayer("cubozoa");

  public static void initialize() {
    EntityModelLayerRegistry.registerModelLayer(EntityModels.SILK_MOTH_MODEL,
        SilkMothModel::getTexturedModelData);
    register(LighterEndMobs.SILK_MOTH.mob, SilkMothRenderer::new);

    EntityModelLayerRegistry.registerModelLayer(makeLayer("silk_moth_baby"),
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
  }

  private static void register(EntityType<?> type, Function<Context, MobEntityRenderer> renderer) {
    EntityRendererRegistry.register(type, renderer::apply);
  }

  private static EntityModelLayer makeLayer(String name) {
    return new EntityModelLayer(Identifier.of(LighterEnd.MOD_ID, name), "main");
  }
}
