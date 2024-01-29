package saythespiremod;

import java.util.ArrayList;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SayTheSpireApi {
    public static void setupRoutes(SimpleServer server) {
        server.createGetEndpoint("/monsters", () -> {
            ArrayList<String> monsterData = new ArrayList<String>();
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                SayTheSpireMod.logger.info("Found monster: " + m.name + " at " + m.hb.cX + ", " + m.hb.cY);
                monsterData
                        .add("{\"name\": \"" + m.name + "\", \"x\": " + m.hb.cX + ", \"y\": " + m.hb.cY
                                + "}");
            }
            String responseBody = "{\"monsters\": [" + String.join(",", monsterData) + "]}";
            return responseBody;
        });

        server.createGetEndpoint("/potions", () -> {
            ArrayList<String> potionData = new ArrayList<String>();
            for (AbstractPotion p : AbstractDungeon.player.potions) {
                SayTheSpireMod.logger.info("Found potion: " + p.name + " at " + p.hb.cX + ", " + p.hb.cY);
                potionData
                        .add("{\"name\": \"" + p.name + "\", \"x\": " + p.hb.cX + ", \"y\": " + p.hb.cY
                                + "}");
            }

            return "{\"potions\": [" + String.join(",", potionData) + "]}";
        });

        server.createGetEndpoint("/relics", () -> {
            ArrayList<String> relicData = new ArrayList<String>();
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                SayTheSpireMod.logger.info("Found relic: " + r.name + " at " + r.hb.cX + ", " + r.hb.cY);
                relicData
                        .add("{\"name\": \"" + r.name + "\", \"x\": " + r.hb.cX + ", \"y\": " + r.hb.cY
                                + "}");
            }

            return "{\"relics\": [" + String.join(",", relicData) + "]}";
        });
    }
}
