package me.jellysquid.mods.sodium.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.SoundEvents;
import org.lwjgl.opengl.GL11;

import java.util.function.Consumer;

public abstract class AbstractWidget implements IRenderable, IGuiEventListener {
    protected final FontRenderer font;

    protected AbstractWidget() {
        this.font = Minecraft.getInstance().textRenderer;
    }

    protected void drawString(MatrixStack matrixStack, String str, int x, int y, int color) {
        this.font.draw(matrixStack, str, x, y, color);
    }

    protected void drawRect(int x1, int y1, int x2, int y2, int color) {
        float a = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;

        this.drawQuads(vertices -> addQuad(vertices, x1, y1, x2, y2, a, r, g, b));
    }

    protected void drawQuads(Consumer<IVertexConsumer> consumer) {
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        consumer.accept(bufferBuilder);

        bufferBuilder.end();

        WorldVertexBufferUploader.draw(bufferBuilder);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    protected static void addQuad(IVertexConsumer consumer, int x1, int y1, int x2, int y2, float a, float r, float g, float b) {
        consumer.vertex(x2, y1, 0.0D).color(r, g, b, a).next();
        consumer.vertex(x1, y1, 0.0D).color(r, g, b, a).next();
        consumer.vertex(x1, y2, 0.0D).color(r, g, b, a).next();
        consumer.vertex(x2, y2, 0.0D).color(r, g, b, a).next();
    }

    protected void playClickSound() {
        Minecraft.getInstance().getSoundManager()
                .play(SimpleSound.master(SoundEvents.field_187909_gi, 1.0F));
    }

    protected int getStringWidth(String text) {
        return this.font.getWidth(text);
    }
}
