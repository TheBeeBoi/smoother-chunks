package io.thebeeboi.smootherchunks.mixin;

import io.thebeeboi.smootherchunks.client.handler.ChunkAnimationHandler;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkBuilder.BuiltChunk.class)
public abstract class BuiltChunkMixin {
    @Shadow public abstract BlockPos getOrigin();

    @Inject(
            method = "clear",
            at = @At(value = "TAIL")
    )
    public void onDelete(CallbackInfo ci) {
        ChunkAnimationHandler.get().getLoadedChunks().remove(getOrigin());
    }
}
