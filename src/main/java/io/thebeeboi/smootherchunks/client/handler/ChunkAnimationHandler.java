package io.thebeeboi.smootherchunks.client.handler;

import io.thebeeboi.smootherchunks.SmootherChunks;
import io.thebeeboi.smootherchunks.client.SmootherChunksClient;
import io.thebeeboi.smootherchunks.client.config.LoadAnimation;
import io.thebeeboi.smootherchunks.client.config.SmootherChunksConfig;
import io.thebeeboi.smootherchunks.util.UtilEasing;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.util.math.Vec3i;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class ChunkAnimationHandler {
    /*TODO use Reference2ReferenceLinkedHashMap from FastUtil or just inject the AnimationController directly into BuiltChunk.
     * Need to solve concurrency issue as currently #addChunk is called from both render & worker threads.
     */

    private static final ChunkAnimationHandler instance = new ChunkAnimationHandler();
    private final Map<ChunkBuilder.BuiltChunk, AnimationController> animations = new HashMap<>();
    @Getter
    private final Set<Vec3i> loadedChunks = new HashSet<>();

    public static ChunkAnimationHandler get() {
        return instance;
    }

    public void addChunk(ChunkBuilder.BuiltChunk chunk) {
        Vec3i origin = chunk.getOrigin();
        if (loadedChunks.contains(origin)) return;
        loadedChunks.add(origin);

        Direction direction = null;

        if (SmootherChunksClient.get().getConfig().getLoadAnimation() == LoadAnimation.INWARD
                && MinecraftClient.getInstance().getCameraEntity() != null) {
            BlockPos delta = chunk.getOrigin().subtract(MinecraftClient.getInstance().getCameraEntity().getBlockPos());

            int dX = Math.abs(delta.getX());
            int dZ = Math.abs(delta.getZ());

            if (dX > dZ) {
                if (delta.getX() > 0) direction = Direction.WEST;
                else direction = Direction.EAST;
            } else {
                if (delta.getZ() > 0) direction = Direction.NORTH;
                else direction = Direction.SOUTH;
            }
        }

        animations.putIfAbsent(chunk, new AnimationController(chunk.getOrigin(), direction, System.currentTimeMillis()));
    }

    public void updateChunk(ChunkBuilder.BuiltChunk chunk, MatrixStack stack) {
        SmootherChunksConfig config = SmootherChunksClient.get().getConfig();

        AnimationController controller = animations.get(chunk);
        if (controller == null || MinecraftClient.getInstance().getCameraEntity() == null) return;

        BlockPos finalPos = controller.getFinalPos();

        if (config.isDisableNearby()) {
            double dX = finalPos.getX() - MinecraftClient.getInstance().getCameraEntity().getPos().getX();
            double dZ = finalPos.getZ() - MinecraftClient.getInstance().getCameraEntity().getPos().getZ();
            if (dX * dX + dZ * dZ < 32 * 32) return;
        }

        double completion = (double) (System.currentTimeMillis() - controller.getStartTime()) / config.getDuration() / 1000d;
        completion = UtilEasing.easeOutSine(Math.min(completion, 1.0));

        switch (config.getLoadAnimation()) {
            case DOWNWARD ->
                    stack.translate(0, (finalPos.getY() - completion * finalPos.getY()) * config.getTranslationAmount(), 0);
            case UPWARD ->
                    stack.translate(0, (-finalPos.getY() + completion * finalPos.getY()) * config.getTranslationAmount(), 0);
            case INWARD -> {
                if (controller.getDirection() == null) break;
                Vec3i dirVec = controller.getDirection().getVector();
                double mod = -(200 - UtilEasing.easeInOutSine(completion) * 200) * config.getTranslationAmount();
                stack.translate(dirVec.getX() * mod, 0, dirVec.getZ() * mod);
            }
            case SCALE ->
                //TODO Find a way to scale centered at the middle of the chunk rather than the origin.
                    stack.scale((float) completion, (float) completion, (float) completion);
        }

        if (completion >= 1.0) animations.remove(chunk);
    }

    @AllArgsConstructor @Data
    private static class AnimationController {
        private BlockPos finalPos;
        private Direction direction;
        private long startTime;
    }
}
