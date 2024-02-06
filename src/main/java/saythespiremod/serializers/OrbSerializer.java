package saythespiremod.serializers;

import java.util.ArrayList;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.ValueType;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class OrbSerializer {
    public static JsonValue toJson(AbstractOrb orb) {
        JsonValue orbJson = new JsonValue(ValueType.object);
        orbJson.addChild("name", new JsonValue(orb.name));
        orbJson.addChild("x", new JsonValue(orb.hb.cX));
        orbJson.addChild("y", new JsonValue(orb.hb.cY));
        return orbJson;
    }

    public static JsonValue toJson(ArrayList<AbstractOrb> orbs) {
        JsonValue orbsJson = new JsonValue(ValueType.array);
        for (AbstractOrb orb : orbs) {
            orbsJson.addChild(OrbSerializer.toJson(orb));
        }
        return orbsJson;
    }
}
