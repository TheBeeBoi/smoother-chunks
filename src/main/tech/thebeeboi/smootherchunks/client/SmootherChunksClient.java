package tech.thebeeboi.smootherchunks.client;

import tech.thebeeboi.smootherchunks.client.config.SmootherChunksConfig;
import lombok.Getter;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT) public class SmootherChunksClient implements ClientModInitializer {
    private static SmootherChunksClient instance;
    @Getter private SmootherChunksConfig config;

    public static SmootherChunksClient get() {return instance;}

    @Override public void onInitializeClient() {
        instance = this;

        AutoConfig.register(SmootherChunksConfig.class, Toml4jConfigSerializer::new);
        config = AutoConfig.getConfigHolder(SmootherChunksConfig.class).getConfig();
    }
}