package boredbrownbear.boredcommands.mixins;

import net.minecraft.entity.player.PlayerAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerAbilities.class)
public interface MixinPlayerAbilitiesAccessor {
    @Accessor("flySpeed") void setFlySpeed(float speed);
}