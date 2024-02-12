package speakthespiremod.serializers;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.ValueType;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MonsterSerializer {
    public static JsonValue toJson(AbstractMonster monster) {
        JsonValue monsterJson = new JsonValue(ValueType.object);
        monsterJson.addChild("name", new JsonValue(monster.name));
        monsterJson.addChild("x", new JsonValue(monster.hb.cX));
        monsterJson.addChild("y", new JsonValue(monster.hb.cY));
        monsterJson.addChild("currentHealth", new JsonValue(monster.currentHealth));
        return monsterJson;
    }
}
