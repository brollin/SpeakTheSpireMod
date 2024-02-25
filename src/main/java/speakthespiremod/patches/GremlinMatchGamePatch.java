package speakthespiremod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.events.shrines.GremlinMatchGame;
import com.megacrit.cardcrawl.helpers.FontHelper;

import basemod.ReflectionHacks;

public class GremlinMatchGamePatch {
    @SpirePatch(clz = GremlinMatchGame.class, method = "render")
    public static class GremlinMatchGameRenderPatch {
        public static void Postfix(GremlinMatchGame self, SpriteBatch sb) {
            CardGroup cards = ReflectionHacks.getPrivate(self, GremlinMatchGame.class, "cards");
            String[] cardNumbers = { "1", "6", "11", "4", "5", "10", "3", "8", "9", "2", "7", "12" };
            for (int i = 0; i < cards.size(); i++) {
                AbstractCard card = cards.group.get(i);
                FontHelper.renderFontCentered(sb, FontHelper.smallDialogOptionFont, "\"flip " + cardNumbers[i] + "\"",
                        card.hb.cX,
                        card.hb.y, Settings.CREAM_COLOR);
            }
        }
    }
}
