package saythespiremod;

import java.util.List;
import java.util.Map;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;

import saythespiremod.serializers.MonsterSerializer;
import saythespiremod.serializers.PlayerSerializer;
import saythespiremod.serializers.PotionSerializer;
import saythespiremod.serializers.RelicSerializer;
import saythespiremod.serializers.RewardSerializer;

public class SayTheSpireApi {
    public static void setupRoutes(SimpleServer server) {
        server.createGetEndpoint("/player", (Map<String, List<String>> requestParameters) -> {
            JsonValue playerJson = PlayerSerializer.toJson(AbstractDungeon.player);
            SayTheSpireMod.logger.debug("player json: " + playerJson.toJson(OutputType.json));
            return playerJson.toJson(OutputType.json);
        });

        server.createGetEndpoint("/monsters", (Map<String, List<String>> requestParameters) -> {
            JsonValue monstersJson = new JsonValue(JsonValue.ValueType.array);
            for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                monstersJson.addChild(MonsterSerializer.toJson(monster));
            }
            SayTheSpireMod.logger.debug("monster json: " + monstersJson.toJson(OutputType.json));
            return monstersJson.toJson(OutputType.json);
        });

        server.createGetEndpoint("/potions", (Map<String, List<String>> requestParameters) -> {
            JsonValue potionsJson = new JsonValue(JsonValue.ValueType.array);
            for (AbstractPotion potion : AbstractDungeon.player.potions) {
                potionsJson.addChild(PotionSerializer.toJson(potion));
            }
            SayTheSpireMod.logger.debug("potion json: " + potionsJson.toJson(OutputType.json));
            return potionsJson.toJson(OutputType.json);
        });

        server.createGetEndpoint("/relics", (Map<String, List<String>> requestParameters) -> {
            JsonValue relicsJson = new JsonValue(JsonValue.ValueType.array);
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                relicsJson.addChild(RelicSerializer.toJson(relic));
            }
            SayTheSpireMod.logger.debug("relic json: " + relicsJson.toJson(OutputType.json));
            return relicsJson.toJson(OutputType.json);
        });

        server.createGetEndpoint("/rewards", (Map<String, List<String>> requestParameters) -> {
            JsonValue rewardsJson = new JsonValue(JsonValue.ValueType.array);
            for (RewardItem reward : AbstractDungeon.combatRewardScreen.rewards) {
                rewardsJson.addChild(RewardSerializer.toJson(reward));
            }
            SayTheSpireMod.logger.debug("reward json: " + rewardsJson.toJson(OutputType.json));
            return rewardsJson.toJson(OutputType.json);
        });

        server.createGetEndpoint("/bossRelics", (Map<String, List<String>> requestParameters) -> {
            JsonValue bossRelicsJson = new JsonValue(JsonValue.ValueType.array);
            for (AbstractRelic bossRelic : AbstractDungeon.bossRelicScreen.relics) {
                bossRelicsJson.addChild(RelicSerializer.toJson(bossRelic));
            }
            SayTheSpireMod.logger.debug("bossRelic json: " + bossRelicsJson.toJson(OutputType.json));
            return bossRelicsJson.toJson(OutputType.json);
        });

        server.createPostEndpoint("/usePotion", (Map<String, List<String>> requestParameters) -> {
            int potionIndex = Integer.parseInt(requestParameters.get("index").get(0));
            String operation = requestParameters.get("operation").get(0);
            AbstractPotion potion = AbstractDungeon.player.potions.get(potionIndex);

            if (potion == null) {
                return "Potion not found";
            }

            if (operation.equals("use") && potion.canUse()) {
                potion.use(AbstractDungeon.player);
                AbstractDungeon.topPanel.destroyPotion(potion.slot);
                return "Used potion " + potion.name;
            }

            if (operation.equals("discard") && potion.canDiscard()) {
                AbstractDungeon.topPanel.destroyPotion(potion.slot);
                return "Discarded potion " + potion.name;
            }

            return "Potion not used or discarded";
        });
    }
}
