package net.quantumfusion.dashloader.registry;

import io.activej.serializer.annotations.Deserialize;
import io.activej.serializer.annotations.Serialize;
import io.activej.serializer.annotations.SerializeNullable;
import net.quantumfusion.dashloader.atlas.DashSprite;

import java.util.Map;

public class RegistrySpriteData {
    @Serialize(order = 0)
    @SerializeNullable(path = {1})
    @SerializeNullable(path = {0})
    public Map<Long, DashSprite> sprites;

    public RegistrySpriteData(@Deserialize("sprites") Map<Long, DashSprite> sprites) {
        this.sprites = sprites;
    }
}
