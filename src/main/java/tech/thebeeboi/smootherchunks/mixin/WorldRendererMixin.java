package tech.thebeeboi.smootherchunks.mixin;

import tech.thebeeboi.smootherchunks.client.handler.ChunkAnimationHandler;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.Shader;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
	@Inject(
			method = "renderLayer",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gl/VertexBuffer;bind()V"),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void renderLayerInject(RenderLayer renderLayer, MatrixStack matrices, double cameraX, double cameraY, double cameraZ, Matrix4f positionMatrix, CallbackInfo ci, double cameraZ, Matrix4f positionMatrix, boolean bl, ObjectListIterator objectListIterator, Shader shader, GlUniform glUniform, WorldRenderer.ChunkInfo chunkInfo2, ChunkBuilder.BuiltChunk builtChunk, VertexBuffer vertexBuffer, boolean bl, ObjectListIterator objectListIterator, WorldRenderer.ChunkInfo chunkInfo2, ChunkBuilder.BuiltChunk builtChunk, VertexBuffer vertexBuffer) {
		ChunkAnimationHandler.get().updateChunk(builtChunk, matrixStack);
	}
}
