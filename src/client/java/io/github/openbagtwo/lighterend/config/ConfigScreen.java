package io.github.openbagtwo.lighterend.config;


import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.config.Config.ConfigException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

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
          SimpleOption.ofBoolean("Mod Music Discs Can Be Found in End Cities",
              this.config.musicDiscsInEndCities, (value) -> {
                this.config.musicDiscsInEndCities = value;
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
}
