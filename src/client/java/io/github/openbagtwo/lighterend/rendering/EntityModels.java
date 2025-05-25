package io.github.openbagtwo.lighterend.rendering;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.registries.LighterEndMobs;
import io.github.openbagtwo.lighterend.rendering.models.SilkMothModel;
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

  public static void initialize() {
    EntityModelLayerRegistry.registerModelLayer(EntityModels.SILK_MOTH_MODEL,
        SilkMothModel::getTexturedModelData);
    register(LighterEndMobs.SILK_MOTH.mob, SilkMothRenderer::new);

    EntityModelLayerRegistry.registerModelLayer(makeLayer("silk_moth_baby"),
        () -> SilkMothModel.getTexturedModelData().transform(SilkMothModel.BABY_TRANSFORMER));
  }

  private static void register(EntityType<?> type, Function<Context, MobEntityRenderer> renderer) {
    EntityRendererRegistry.register(type, renderer::apply);
  }

  private static EntityModelLayer makeLayer(String name) {
    return new EntityModelLayer(Identifier.of(LighterEnd.MOD_ID, name), "main");
  }
}
