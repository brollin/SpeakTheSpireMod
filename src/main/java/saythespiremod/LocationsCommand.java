package saythespiremod;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.devcommands.ConsoleCommand;

public class LocationsCommand extends ConsoleCommand {
    public LocationsCommand() {
        maxExtraTokens = 0;
        minExtraTokens = 0;
        requiresPlayer = false;
        simpleCheck = true;
    }

    @Override
    public void execute(String[] tokens, int depth) {
        System.out.println("Hello, world!");
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            SayTheSpireMod.logger.info("Found monster: " + m.name + " at " + m.hb.cX + ", " + m.hb.cY);
        }
    }
}
