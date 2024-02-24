package speakthespiremod.patches;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.runHistory.RunHistoryScreen;
import com.megacrit.cardcrawl.screens.stats.RunData;

import basemod.ReflectionHacks;

public class RunHistoryScreenPatch {
    @SpirePatch(clz = RunHistoryScreen.class, method = "render")
    public static class RunHistoryScreenRenderPatch {
        public static void Postfix(RunHistoryScreen self, SpriteBatch sb) {
            Hitbox nextHb = ReflectionHacks.getPrivate(CardCrawlGame.mainMenuScreen.runHistoryScreen,
                    RunHistoryScreen.class,
                    "nextHb");
            Hitbox prevHb = ReflectionHacks.getPrivate(CardCrawlGame.mainMenuScreen.runHistoryScreen,
                    RunHistoryScreen.class,
                    "prevHb");

            int runIndex = ReflectionHacks.getPrivate(CardCrawlGame.mainMenuScreen.runHistoryScreen,
                    RunHistoryScreen.class,
                    "runIndex");
            ArrayList<RunData> filteredRuns = ReflectionHacks.getPrivate(CardCrawlGame.mainMenuScreen.runHistoryScreen,
                    RunHistoryScreen.class,
                    "filteredRuns");
            int filteredRunsSize = filteredRuns.size();

            if (nextHb != null && runIndex < filteredRunsSize - 1) {
                FontHelper.renderFontCentered(sb, FontHelper.smallDialogOptionFont, "\"next\"", nextHb.cX,
                        nextHb.cY, Settings.CREAM_COLOR);
            }

            if (prevHb != null && runIndex > 0) {
                FontHelper.renderFontCentered(sb, FontHelper.smallDialogOptionFont, "\"previous\"", prevHb.cX,
                        prevHb.cY, Settings.CREAM_COLOR);
            }
        }
    }
}
