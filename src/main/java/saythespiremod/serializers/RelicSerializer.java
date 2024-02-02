package saythespiremod.serializers;

import java.util.ArrayList;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.ValueType;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class RelicSerializer {
    public static JsonValue toJson(AbstractRelic relic) {
        JsonValue relicJson = new JsonValue(ValueType.object);
        relicJson.addChild("name", new JsonValue(relic.name));
        relicJson.addChild("x", new JsonValue(relic.hb.cX));
        relicJson.addChild("y", new JsonValue(relic.hb.cY));
        return relicJson;
    }

    public static JsonValue toJson(ArrayList<AbstractRelic> relics) {
        JsonValue relicsJson = new JsonValue(ValueType.array);
        for (AbstractRelic relic : relics) {
            relicsJson.addChild(RelicSerializer.toJson(relic));
        }
        return relicsJson;
    }
}
