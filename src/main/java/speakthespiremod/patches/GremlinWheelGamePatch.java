package speakthespiremod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.GremlinWheelGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;

import basemod.ReflectionHacks;

public class GremlinWheelGamePatch {
    @SpirePatch(clz = GremlinWheelGame.class, method = "render")
    public static class GremlinWheelGameRenderPatch {
        public static void Postfix(GremlinWheelGame self, SpriteBatch sb) {
            Hitbox hb = ReflectionHacks.getPrivate(AbstractDungeon.getCurrRoom().event, GremlinWheelGame.class,
                    "buttonHb");

            if (hb != null) {
                FontHelper.renderFontCentered(sb, FontHelper.smallDialogOptionFont, "\"proceed\"", hb.cX,
                        hb.y + hb.height, Settings.CREAM_COLOR);
            }
        }
    }
}
