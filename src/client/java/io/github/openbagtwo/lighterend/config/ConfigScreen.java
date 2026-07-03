package io.github.openbagtwo.lighterend.config;


import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.config.Config.ConfigException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class ConfigScreen extends GameOptionsScreen {

  private OptionListWidget widgets;

  private static String REQUIRES_RESTART = "Restart Minecraft to apply changes";

  public ConfigScreen(Screen previous) {
    super(previous, MinecraftClient.getInstance().options, Text.of(LighterEnd.MOD_NAME));
  }

  @Override
  protected void addOptions() {
    if (this.body != null) {
      this.body.addSingleOptionEntry(
          SimpleOption.ofBoolean(
              "Generate Modded Biomes",
              SimpleOption.constantTooltip(Text.of(REQUIRES_RESTART)),
              LighterEnd.CONFIG.generateBiomes,
              (value) -> {
                LighterEnd.CONFIG.generateBiomes = value;
              }
          )
      );
      this.body.addSingleOptionEntry(
          SimpleOption.ofBoolean(
              "Generate Ores",
              SimpleOption.constantTooltip(Text.of(
                  "Whether to generate redstone and quartz ores (including in vanilla biomes)"
                      + "\n\n" + REQUIRES_RESTART)),
              LighterEnd.CONFIG.generateOres,
              (value) -> {
                LighterEnd.CONFIG.generateOres = value;
              }
          )
      );
      this.body.addSingleOptionEntry(
          SimpleOption.ofBoolean(
              "Play Modded Music in End Biomes",
              LighterEnd.CONFIG.playEndBiomeMusic,
              (value) -> {
                LighterEnd.CONFIG.playEndBiomeMusic = value;
              }
          )
      );
      this.body.addSingleOptionEntry(
          new SimpleOption<>(
              "End Gravity",
              SimpleOption.constantTooltip(
                  Text.of("Set to 1.0 for vanilla" + "\n\n" + REQUIRES_RESTART)
              ),
              (optionText, value) -> GameOptions.getGenericValueText(optionText,
                  Text.of(String.valueOf(.01 * value))),
              new SimpleOption.ValidatingIntSliderCallbacks(5, 100, false),
              MathHelper.floor(100 * LighterEnd.CONFIG.endGravity),
              value -> {
                LighterEnd.CONFIG.endGravity = 0.01 * value;
              }
          )
      );
      this.body.addSingleOptionEntry(
          SimpleOption.ofBoolean(
              "Disable End Gravity While Flying",
              SimpleOption.constantTooltip(
                  Text.of(
                      "Lowering gravity nerfs unpowered glide speed otherwise"
                          + "\n\n" + REQUIRES_RESTART
                  )
              ),
              LighterEnd.CONFIG.disableEndGravityWhileFlying,
              (value) -> {
                LighterEnd.CONFIG.disableEndGravityWhileFlying = value;
              }
          )
      );
      this.body.addSingleOptionEntry(
          SimpleOption.ofBoolean(
              "Modded Plants Can Only Grow in The End",
              LighterEnd.CONFIG.endPlantsOnlyGrowInTheEnd, (value) -> {
                LighterEnd.CONFIG.endPlantsOnlyGrowInTheEnd = value;
              }
          )
      );
      this.body.addSingleOptionEntry(
          SimpleOption.ofBoolean(
              "Mod Music Discs Can Be Found in End Cities",
              SimpleOption.constantTooltip(Text.of(REQUIRES_RESTART)),
              LighterEnd.CONFIG.musicDiscsInEndCities, (value) -> {
                LighterEnd.CONFIG.musicDiscsInEndCities = value;
              }
          )
      );
      this.body.addSingleOptionEntry(
          SimpleOption.ofBoolean(
              "Use Custom Fishing Loot in The End",
              LighterEnd.CONFIG.customEndFishing, (value) -> {
                LighterEnd.CONFIG.customEndFishing = value;
              }
          )
      );
      this.body.addSingleOptionEntry(
          SimpleOption.ofBoolean(
              "Custom Aquatic Vegetation Bonemealing",
              SimpleOption.constantTooltip(Text.of(
                  "With this option enabled, bonemealing underwater in The End will grow modded flora"
              )),
              LighterEnd.CONFIG.bonemealUnderwaterInEndMakesEndVegetation, (value) -> {
                LighterEnd.CONFIG.bonemealUnderwaterInEndMakesEndVegetation = value;
              }
          )
      );
      this.body.addSingleOptionEntry(
          SimpleOption.ofBoolean(
              "End World Spawn",
              SimpleOption.constantTooltip(Text.of(
                  "New worlds will have their spawn points in The End."
                      + "\n\nHighly experimental.\nEnable at your own risk."
              )),
              LighterEnd.CONFIG.endSpawn, (value) -> {
                LighterEnd.CONFIG.endSpawn = value;
              }
          )
      );
    }
  }

  @Override
  public void removed() {
    try {
      LighterEnd.CONFIG.writeConfigToFile();
    } catch (ConfigException e) {
      LighterEnd.LOGGER.error(String.valueOf(e));
    }
  }
}
