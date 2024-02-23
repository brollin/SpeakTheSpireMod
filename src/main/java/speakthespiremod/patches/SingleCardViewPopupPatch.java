package speakthespiremod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

import basemod.ReflectionHacks;

public class SingleCardViewPopupPatch {
    @SpirePatch(clz = SingleCardViewPopup.class, method = "render")
    public static class SingleCardViewPopupRenderPatch {
        public static void Postfix(SingleCardViewPopup self, SpriteBatch sb) {
            Hitbox nextHb = ReflectionHacks.getPrivate(CardCrawlGame.cardPopup, SingleCardViewPopup.class,
                    "nextHb");
            Hitbox prevHb = ReflectionHacks.getPrivate(CardCrawlGame.cardPopup, SingleCardViewPopup.class,
                    "prevHb");

            AbstractCard nextCard = ReflectionHacks.getPrivate(CardCrawlGame.cardPopup, SingleCardViewPopup.class,
                    "nextCard");
            AbstractCard prevCard = ReflectionHacks.getPrivate(CardCrawlGame.cardPopup, SingleCardViewPopup.class,
                    "prevCard");

            if (nextHb != null && nextCard != null) {
                FontHelper.renderFontCentered(sb, FontHelper.smallDialogOptionFont, "\"next\"", nextHb.cX,
                        nextHb.cY, Settings.CREAM_COLOR);
            }

            if (prevHb != null && prevCard != null) {
                FontHelper.renderFontCentered(sb, FontHelper.smallDialogOptionFont, "\"previous\"", prevHb.cX,
                        prevHb.cY, Settings.CREAM_COLOR);
            }
        }
    }
}
