package suso.event_manage.mixin;

import com.mojang.brigadier.tree.CommandNode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import suso.event_manage.util.ICommandNodeUtil;

import java.util.Map;

@Mixin(CommandNode.class)
public class CommandNodeUtilImpl<S> implements ICommandNodeUtil {
    @Shadow(remap = false) @Final private Map<String, CommandNode<S>> children;

    @Override
    public void removeChild(String key) {
        this.children.remove(key);
    }
}
