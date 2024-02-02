package saythespiremod.serializers;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.ValueType;
import com.megacrit.cardcrawl.helpers.Hitbox;

public class HitboxSerializer {
    public static JsonValue toJson(Hitbox hitbox) {
        JsonValue hitboxJson = new JsonValue(ValueType.object);
        hitboxJson.addChild("x", new JsonValue(hitbox.cX));
        hitboxJson.addChild("y", new JsonValue(hitbox.cY));
        return hitboxJson;
    }
}
