package speakthespiremod.serializers;

import java.util.ArrayList;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.ValueType;
import com.megacrit.cardcrawl.shop.StoreRelic;

import basemod.ReflectionHacks;

public class StoreRelicSerializer {
    public static JsonValue toJson(StoreRelic relic) {
        JsonValue relicJson = RelicSerializer.toJson(relic.relic);

        int slot = ReflectionHacks.getPrivate(relic, StoreRelic.class, "slot");
        relicJson.addChild("slot", new JsonValue(slot));

        return relicJson;
    }

    public static JsonValue toJson(ArrayList<StoreRelic> relics) {
        JsonValue relicsJson = new JsonValue(ValueType.array);
        for (StoreRelic relic : relics) {
            relicsJson.addChild(StoreRelicSerializer.toJson(relic));
        }
        return relicsJson;
    }
}
