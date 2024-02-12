package speakthespiremod.serializers;

import java.util.ArrayList;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.ValueType;
import com.megacrit.cardcrawl.shop.StorePotion;

import basemod.ReflectionHacks;

public class StorePotionSerializer {
    public static JsonValue toJson(StorePotion potion) {
        JsonValue potionJson = PotionSerializer.toJson(potion.potion);

        int slot = ReflectionHacks.getPrivate(potion, StorePotion.class, "slot");
        potionJson.addChild("slot", new JsonValue(slot));

        return potionJson;
    }

    public static JsonValue toJson(ArrayList<StorePotion> potions) {
        JsonValue potionsJson = new JsonValue(ValueType.array);
        for (StorePotion potion : potions) {
            potionsJson.addChild(StorePotionSerializer.toJson(potion));
        }
        return potionsJson;
    }
}
