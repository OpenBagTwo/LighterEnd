package io.github.openbagtwo.lighterend.mixin.client;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.Optionull;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.sounds.Music;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class EndBiomeMusicMixin {

  @Shadow
  public @Nullable Screen screen;

  @Shadow
  public @Nullable LocalPlayer player;

  @Shadow
  public Gui gui;

  @Shadow
  public GameRenderer gameRenderer;


  @Inject(method = "getSituationalMusic", at = @At("HEAD"), cancellable = true)
  public void checkForEndMusic(CallbackInfoReturnable<Music> cir) {
    Music musicSound = Optionull.map(this.screen, Screen::getBackgroundMusic);
    Camera camera = this.gameRenderer.getMainCamera();
    if (
        LighterEnd.CONFIG.playEndBiomeMusic()
            && musicSound == null
            && this.player != null
            && camera != null
    ) {
      Level world = this.player.level();
      if (
          world.dimension() == Level.END
              && !this.gui.getBossOverlay().shouldPlayMusic()
      ) {
        Music biomeMusic = camera.attributeProbe()
            .getValue(EnvironmentAttributes.BACKGROUND_MUSIC, 1.0F).select(
                this.player.getAbilities().instabuild && this.player.getAbilities().mayfly,
                this.player.isUnderWater()
            ).orElse(null);
        if (biomeMusic != null && biomeMusic.replaceCurrentMusic()) {
          cir.setReturnValue(
              new Music(
                  biomeMusic.sound(),
                  biomeMusic.minDelay(),
                  biomeMusic.maxDelay(),
                  false
              )
          );
        }
      }
    }
  }
}
