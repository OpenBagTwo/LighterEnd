package io.github.openbagtwo.lighterend.config;


import com.mojang.serialization.Codec;
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
  private Config config;

  public ConfigScreen(Screen previous) {
    super(previous, MinecraftClient.getInstance().options, Text.of(LighterEnd.MOD_NAME));
    this.config = Config.loadConfiguration();
  }

  @Override
  protected void addOptions() {
    if (this.body != null) {
      this.body.addSingleOptionEntry(
          SimpleOption.ofBoolean("Generate Modded Biomes",
              this.config.generateBiomes, (value) -> {
                this.config.generateBiomes = value;
              }));
      this.body.addSingleOptionEntry(
          SimpleOption.ofBoolean("Play Modded Music in End Biomes",
              this.config.playEndBiomeMusic, (value) -> {
                this.config.playEndBiomeMusic = value;
              }));
      this.body.addSingleOptionEntry(
          new SimpleOption<>(
              "End Gravity:",
              SimpleOption.constantTooltip(Text.of("Set to 1.0 for vanilla")),
              ConfigScreen::getPercentValueOrOffText,
              SimpleOption.DoubleSliderCallbacks.INSTANCE.withModifier(MathHelper::square,
                  Math::sqrt),
              Codec.doubleRange(0.05, 1.0),
              this.config.endGravity,
              value -> {
                this.config.endGravity = value;
              }));
      this.body.addSingleOptionEntry(
          SimpleOption.ofBoolean("Mod Music Discs Can Be Found in End Cities",
              this.config.musicDiscsInEndCities, (value) -> {
                this.config.musicDiscsInEndCities = value;
              }));
      this.body.addSingleOptionEntry(
          SimpleOption.ofBoolean("Bonemealing Underwater in The End Produces End Vegetation",
              this.config.bonemealUnderwaterInEndMakesEndVegetation, (value) -> {
                this.config.bonemealUnderwaterInEndMakesEndVegetation = value;
              }));
    }
  }

  @Override
  public void removed() {
    try {
      this.config.writeConfigToFile();
    } catch (ConfigException e) {
      LighterEnd.LOGGER.error(String.valueOf(e));
    }
  }

  private static Text getPercentValueOrOffText(Text prefix, double value) {
    return value == 0.0 ? GameOptions.getGenericValueText(prefix, ScreenTexts.OFF) : Text.of("");
  }
}
