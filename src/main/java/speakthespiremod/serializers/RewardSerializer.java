package speakthespiremod.serializers;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.ValueType;
import com.megacrit.cardcrawl.rewards.RewardItem;

public class RewardSerializer {
    public static JsonValue toJson(RewardItem reward) {
        JsonValue rewardJson = new JsonValue(ValueType.object);
        rewardJson.addChild("type", new JsonValue(reward.type.toString()));
        rewardJson.addChild("x", new JsonValue(reward.hb.cX));
        rewardJson.addChild("y", new JsonValue(reward.hb.cY));
        return rewardJson;
    }
}
