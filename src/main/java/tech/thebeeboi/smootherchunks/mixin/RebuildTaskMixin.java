package tech.thebeeboi.smootherchunks.mixin;

import tech.thebeeboi.smootherchunks.client.handler.ChunkAnimationHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.chunk.BlockBufferBuilderStorage;
import net.minecraft.client.render.chunk.ChunkBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(targets = "net/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk$RebuildTask")
public abstract class RebuildTaskMixin {
    //Parent class
    @Shadow private ChunkBuilder.BuiltChunk field_20839;

    @Inject(
            method = "run",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Set;forEach(Ljava/util/function/Consumer;)V"
            )
    )
    void onChunkUploads(BlockBufferBuilderStorage buffers, CallbackInfoReturnable<CompletableFuture> cir) {
        ChunkAnimationHandler.get().addChunk(field_20839);
    }
}