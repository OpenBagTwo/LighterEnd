package io.github.openbagtwo.lighterend.config;


import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.config.Config.ConfigException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class ConfigScreen extends GameOptionsScreen {

  private OptionListWidget widgets;

  public ConfigScreen(Screen previous) {
    super(previous, MinecraftClient.getInstance().options, Text.of(LighterEnd.MOD_NAME));
  }

  @Override
  protected void addOptions() {
    if (this.body != null) {
      this.body.addSingleOptionEntry(
          SimpleOption.ofBoolean("Generate Modded Biomes",
              LighterEnd.CONFIG.generateBiomes, (value) -> {
                LighterEnd.CONFIG.generateBiomes = value;
              }));
      this.body.addSingleOptionEntry(
          SimpleOption.ofBoolean("Play Modded Music in End Biomes",
              LighterEnd.CONFIG.playEndBiomeMusic, (value) -> {
                LighterEnd.CONFIG.playEndBiomeMusic = value;
              }));
      this.body.addSingleOptionEntry(
          new SimpleOption<>(
              "End Gravity",
              SimpleOption.constantTooltip(Text.of("Set to 1.0 for vanilla")),
              (optionText, value) -> GameOptions.getGenericValueText(optionText,
                  Text.of(String.valueOf(.01 * value))),
              new SimpleOption.ValidatingIntSliderCallbacks(5, 100, false),
              MathHelper.floor(100 * LighterEnd.CONFIG.endGravity),
              value -> {
                LighterEnd.CONFIG.endGravity = 0.01 * value;
              }));
      this.body.addSingleOptionEntry(
          SimpleOption.ofBoolean("Modded plants can only grow in The End",
              LighterEnd.CONFIG.endPlantsOnlyGrowInTheEnd, (value) -> {
                LighterEnd.CONFIG.endPlantsOnlyGrowInTheEnd = value;
              }));
      this.body.addSingleOptionEntry(
          SimpleOption.ofBoolean("Mod Music Discs Can Be Found in End Cities",
              LighterEnd.CONFIG.musicDiscsInEndCities, (value) -> {
                LighterEnd.CONFIG.musicDiscsInEndCities = value;
              }));
      this.body.addSingleOptionEntry(
          SimpleOption.ofBoolean("Bonemealing Underwater in The End Produces End Vegetation",
              LighterEnd.CONFIG.bonemealUnderwaterInEndMakesEndVegetation, (value) -> {
                LighterEnd.CONFIG.bonemealUnderwaterInEndMakesEndVegetation = value;
              }));
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

  private static Text getPercentValueOrOffText(Text prefix, double value) {
    return value == 0.0 ? GameOptions.getGenericValueText(prefix, ScreenTexts.OFF) : Text.of("");
  }
}
