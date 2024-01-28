package saythespiremod;

import java.util.ArrayList;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public class SayTheSpireApi {
    public static String getMonsterData() {
        ArrayList<String> monsterData = new ArrayList<String>();
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            SayTheSpireMod.logger.debug("Found monster: " + m.name + " at " + m.hb.cX + ", " + m.hb.cY);
            monsterData
                    .add("{\"name\": \"" + m.name + "\", \"x\": " + m.hb.cX + ", \"y\": " + m.hb.cY
                            + "}");
        }

        return "{\"monsters\": [" + String.join(",", monsterData) + "]}";
    }

    public static String getPotionData() {
        ArrayList<String> potionData = new ArrayList<String>();
        for (AbstractPotion p : AbstractDungeon.player.potions) {
            SayTheSpireMod.logger.info("Found potion: " + p.name + " at " + p.hb.cX + ", " + p.hb.cY);
            potionData
                    .add("{\"name\": \"" + p.name + "\", \"x\": " + p.hb.cX + ", \"y\": " + p.hb.cY
                            + "}");
        }

        return "{\"potions\": [" + String.join(",", potionData) + "]}";
    }

}
