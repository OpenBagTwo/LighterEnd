package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Rarity;

public class LighterEndMusicDiscs {

  public static Item ENDSEEKER = registerDisc("endseeker");
  public static Item EO_DRACONA = registerDisc("eo_dracona");
  public static Item GRASPING_AT_STARS = registerDisc("grasping_at_stars");
  public static Item STRANGE_AND_ALIEN = registerDisc("strange_and_alien");

  public static Item registerDisc(String trackName) {
    LighterEndSounds.registerReference("music_disc." + trackName);

    Properties settings = new Properties()
        .overrideDescription("item." + LighterEnd.MOD_ID + ".music_disc_" + trackName)
        .stacksTo(1)
        .rarity(Rarity.RARE)
        .jukeboxPlayable(
            ResourceKey.create(Registries.JUKEBOX_SONG, LighterEnd.of(trackName))
        );

    return LighterEndItems.register("music_disc_" + trackName, settings);
  }

  public static void initialize() {
  }

}
