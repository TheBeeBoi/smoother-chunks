package io.thebeeboi.smootherchunks.client.config;

import lombok.Getter;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import blue.endless.jankson.Comment;

@Config(name = "Smoother Chunks") @Config.Gui.Background("minecraft:textures/block/stone.png") @Getter
public class SmootherChunksConfig implements ConfigData {
    @Comment("Duration of the animation in seconds.")
    double duration = 1;

    @Comment("The amount the chunk moves to get to its final position.")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 10)
    int translationAmount = 5;

    @Comment("Type of animation for loading chunks.")
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    LoadAnimation loadAnimation = LoadAnimation.UPWARD;

    @Comment("Disable animating chunks close to you")
    boolean disableNearby = true;

    /*
     * Custom Getters
     */

    /**
     * Gets the config value for translation amount, translating config value to usable value.
     *
     * @return The translation amount as an int 1 to 10 where higher = more translation.
     */
    public double getTranslationAmount() {
        return (double) translationAmount / 5d;
    }
}
