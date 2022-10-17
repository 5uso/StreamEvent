package suso.event_manage.mixin;

import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import suso.event_manage.util.ICommandNodeUtil;

import java.util.Map;

@Mixin(CommandNode.class)
public class CommandNodeUtilImpl<S> implements ICommandNodeUtil {
    @Shadow(remap = false) @Final private Map<String, CommandNode<S>> children;

    @Shadow @Final private Map<String, LiteralCommandNode<S>> literals;

    @Shadow @Final private Map<String, ArgumentCommandNode<S, ?>> arguments;

    @Override
    public void removeChild(String key) {
        this.children.remove(key);
        this.literals.remove(key);
        this.arguments.remove(key);
    }
}
