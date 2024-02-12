package speakthespiremod.serializers;

import java.util.ArrayList;

import com.badlogic.gdx.utils.JsonValue;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class CardSerializer {
    public static JsonValue toJson(AbstractCard card) {
        JsonValue cardJson = new JsonValue(JsonValue.ValueType.object);
        cardJson.addChild("name", new JsonValue(card.name));
        cardJson.addChild("x", new JsonValue(card.hb.cX));
        cardJson.addChild("y", new JsonValue(card.hb.cY));
        return cardJson;
    }

    public static JsonValue toJson(ArrayList<AbstractCard> cards) {
        JsonValue cardsJson = new JsonValue(JsonValue.ValueType.array);
        for (AbstractCard card : cards) {
            cardsJson.addChild(CardSerializer.toJson(card));
        }
        return cardsJson;
    }
}
