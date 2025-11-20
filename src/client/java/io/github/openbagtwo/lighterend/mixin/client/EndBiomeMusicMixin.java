package io.github.openbagtwo.lighterend.mixin.client;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.sound.MusicSound;
import net.minecraft.util.Nullables;
import net.minecraft.world.World;
import net.minecraft.world.attribute.EnvironmentAttributes;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class EndBiomeMusicMixin {

  @Shadow
  public @Nullable Screen currentScreen;

  @Shadow
  public @Nullable ClientPlayerEntity player;

  @Shadow
  public InGameHud inGameHud;

  @Shadow
  public GameRenderer gameRenderer;


  @Inject(method = "getMusicInstance", at = @At("HEAD"), cancellable = true)
  public void checkForEndMusic(CallbackInfoReturnable<MusicSound> cir) {
    MusicSound musicSound = Nullables.map(this.currentScreen, Screen::getMusic);
    Camera camera = this.gameRenderer.getCamera();
    if (
        LighterEnd.CONFIG.playEndBiomeMusic()
            && musicSound == null
            && this.player != null
            && camera != null
    ) {
      World world = this.player.getEntityWorld();
      if (
          world.getRegistryKey() == World.END
              && !this.inGameHud.getBossBarHud().shouldPlayDragonMusic()
      ) {
        MusicSound biomeMusic = camera.getEnvironmentAttributeInterpolator()
            .get(EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO, 1.0F).getCurrent(
                this.player.getAbilities().creativeMode && this.player.getAbilities().allowFlying,
                this.player.isSubmergedInWater()
            ).orElse(null);
        if (biomeMusic != null && biomeMusic.replaceCurrentMusic()) {
          cir.setReturnValue(
              new MusicSound(
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
