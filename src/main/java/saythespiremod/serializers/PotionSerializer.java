package saythespiremod.serializers;

import java.util.ArrayList;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.ValueType;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public class PotionSerializer {
    public static JsonValue toJson(AbstractPotion potion) {
        JsonValue potionJson = new JsonValue(ValueType.object);
        potionJson.addChild("name", new JsonValue(potion.name));
        potionJson.addChild("x", new JsonValue(potion.hb.cX));
        potionJson.addChild("y", new JsonValue(potion.hb.cY));
        return potionJson;
    }

    public static JsonValue toJson(ArrayList<AbstractPotion> potions) {
        JsonValue potionsJson = new JsonValue(ValueType.array);
        for (AbstractPotion potion : potions) {
            potionsJson.addChild(PotionSerializer.toJson(potion));
        }
        return potionsJson;
    }
}
