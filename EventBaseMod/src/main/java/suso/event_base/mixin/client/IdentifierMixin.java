package suso.event_base.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Identifier.class) @Environment(EnvType.CLIENT)
public abstract class IdentifierMixin {
    @ModifyVariable(
            method = "<init>(Ljava/lang/String;Ljava/lang/String;)V",
            at = @At("HEAD"),
            ordinal = 1,
            argsOnly = true
    )
    private static String injectPathLang(String path) {
        if(path.contains("/lang/")) {
            String code = MinecraftClient.getInstance().getLanguageManager().getLanguage();
            String effective = (code.startsWith("es"  ) || code.startsWith("gl_")
                             || code.startsWith("ca_" ) || code.startsWith("eu_")
                             || code.startsWith("ast_")) ? "es" : "en";
            path = path.replace("/lang/", effective);
        }

        return path;
    }
}
