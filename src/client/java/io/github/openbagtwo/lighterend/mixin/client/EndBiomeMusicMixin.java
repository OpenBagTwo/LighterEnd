package io.github.openbagtwo.lighterend.mixin.client;

import java.util.Optional;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MusicInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Nullables;
import net.minecraft.util.collection.Pool;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
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


  @Inject(method = "getMusicInstance", at = @At("HEAD"), cancellable = true)
  public void checkForEndMusic(CallbackInfoReturnable<MusicInstance> cir) {
    MusicSound musicSound = Nullables.map(this.currentScreen, Screen::getMusic);
    if (musicSound == null && this.player != null) {
      World world = this.player.getWorld();
      if (
          world.getRegistryKey() == World.END
              && !this.inGameHud.getBossBarHud().shouldPlayDragonMusic()
      ) {
        RegistryEntry<Biome> registryEntry = world.getBiome(this.player.getBlockPos());
        Biome biome = registryEntry.value();
        Optional<Pool<MusicSound>> biomeMusic = biome.getMusic();
        if (biomeMusic.isPresent()) {
          float f = biome.getMusicVolume();
          Optional<MusicSound> music = biomeMusic.get().getOrEmpty(world.random);
          cir.setReturnValue(new MusicInstance(music.orElse(null), f));
        } else {
          cir.setReturnValue(
              new MusicInstance(
                  new MusicSound(SoundEvents.MUSIC_END, 6000, 24000, false)
              )
          );
        }
        cir.cancel();
      }
    }

  }

}
