package saythespiremod.serializers;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.ValueType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public class PlayerSerializer {
    public static JsonValue toJson(AbstractPlayer player) {
        JsonValue playerJson = new JsonValue(ValueType.object);
        playerJson.addChild("x", new JsonValue(player.hb.cX));
        playerJson.addChild("y", new JsonValue(player.hb.cY));
        playerJson.addChild("orbs", OrbSerializer.toJson(player.orbs));
        return playerJson;
    }
}
