package suso.event_manage.state_handlers.primatica;

import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;

public class PrimaticaExplosionBehavior extends ExplosionBehavior {
    @Override
    public boolean canDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float power) {
        return state.isIn(TagKey.of(Registries.BLOCK.getKey(), new Identifier("suso:primatica_breakable")));
    }
}
