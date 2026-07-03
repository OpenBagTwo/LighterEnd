package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Rarity;

public class LighterEndMusicDiscs {

  public static Item ENDSEEKER = registerDisc("endseeker");
  public static Item EO_DRACONA = registerDisc("eo_dracona");
  public static Item GRASPING_AT_STARS = registerDisc("grasping_at_stars");
  public static Item STRANGE_AND_ALIEN = registerDisc("strange_and_alien");

  public static Item registerDisc(String trackName) {
    LighterEndSounds.registerReference("music_disc." + trackName);

    Settings settings = new Settings()
        .translationKey("item." + LighterEnd.MOD_ID + ".music_disc_" + trackName)
        .maxCount(1)
        .rarity(Rarity.RARE)
        .jukeboxPlayable(
            RegistryKey.of(RegistryKeys.JUKEBOX_SONG, LighterEnd.of(trackName))
        );

    return LighterEndItems.register("music_disc_" + trackName, settings);
  }

  public static void initialize() {
  }

}
