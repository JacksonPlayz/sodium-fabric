package me.jellysquid.mods.sodium.mixin.render_layers;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderLayer;
import net.minecraft.client.renderer.RenderLayers;
import net.minecraft.fluid.Fluid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(RenderLayers.class)
public class MixinRenderLayers {
    @Mutable
    @Shadow
    @Final
    private static Map<Block, RenderLayer> BLOCKS;

    @Mutable
    @Shadow
    @Final
    private static Map<Fluid, RenderLayer> FLUIDS;

    static {
        BLOCKS = new Reference2ReferenceOpenHashMap<>(BLOCKS);
        FLUIDS = new Reference2ReferenceOpenHashMap<>(FLUIDS);
    }
}
