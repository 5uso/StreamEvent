package suso.event_base.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.Shader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import suso.event_base.custom.render.CustomRender;

import java.util.List;
import java.util.Objects;

@Mixin(DeathScreen.class)
public abstract class DeathScreenMixin extends Screen {
    @Shadow private Text scoreText;
    @Shadow @Final private List<ButtonWidget> buttons;
    @Shadow @Final private Text message;
    @Shadow @Final private boolean isHardcore;
    @Shadow @Nullable protected abstract Style getTextComponentUnderMouse(int mouseX);

    private ButtonWidget theButton;
    private Text replacedTitle;

    protected DeathScreenMixin(Text title) {
        super(title);
    }

    @Inject(
            method = "init",
            at = @At("TAIL")
    )
    private void editInit(CallbackInfo ci) {
        this.scoreText = Text.empty();

        this.remove(this.buttons.get(0));
        this.remove(this.buttons.get(1));
        this.buttons.clear();

        theButton = new ButtonWidget(0, 0, 0, 0, Text.translatable(this.isHardcore ? "deathScreen.spectate" : "deathScreen.respawn"), (button) -> {
            this.client.player.requestRespawn();
            this.client.setScreen(null);
        });
        this.buttons.add(this.addDrawableChild(theButton));

        replacedTitle = Text.translatable(isHardcore ? "deathScreen.title.hardcore" : "deathScreen.title").formatted(Formatting.BOLD);
    }

    @Inject(
            method = "render",
            at = @At("HEAD"),
            cancellable = true
    )
    private void replaceRender(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Screen s = this;
        MinecraftClient client = MinecraftClient.getInstance();
        int guiScale = client.options.getGuiScale().getValue();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        CustomRender.setCurrentDrawShader(CustomRender.getDeathShader());
        DrawableHelper.fill(matrices, 0, 0, s.width, s.height, 0);
        //s.fillGradient(matrices, 0, 0, s.width, s.height, 0x60500000, 0xA0803030);
        RenderSystem.disableBlend();
        CustomRender.setCurrentDrawShader(null);

        matrices.push();
        matrices.translate(s.width / 2.0, s.height / 2.0, 0.0);
        matrices.scale(8.0f / guiScale, 8.0f / guiScale, 8.0f);
        drawCenteredText(matrices, s.textRenderer, replacedTitle, 0, -400 / 8 / 4, 0x00FFFFFF);
        matrices.pop();

        if (this.message != null) {
            drawCenteredText(matrices, s.textRenderer, this.message, s.width / 2, 85, 0x00FFFFFF);
        }

        drawCenteredText(matrices, s.textRenderer, this.scoreText, s.width / 2, 100, 0x00FFFFFF);
        if (this.message != null && mouseY > 85) {
            Objects.requireNonNull(s.textRenderer);
            if (mouseY < 85 + 9) {
                Style style = this.getTextComponentUnderMouse(mouseX);
                s.renderTextHoverEffect(matrices, style, mouseX, mouseY);
            }
        }

        matrices.push();
        matrices.translate(s.width / 2.0, s.height / 2.0, 0.0);
        matrices.scale(4.0f / guiScale, 4.0f / guiScale, 4.0f);

        theButton.x = -50;
        theButton.y = 0;
        theButton.width = 100;
        theButton.height = 20;

        super.render(matrices, (mouseX - s.width / 2) / 4 * guiScale, (mouseY - s.height / 2) / 4 * guiScale, delta);

        theButton.x = width / 2 - 200 / guiScale;
        theButton.y = height / 2;
        theButton.width = 100 * 4 / guiScale;
        theButton.height = 20 * 4 / guiScale;

        matrices.pop();

        ci.cancel();
    }
}
