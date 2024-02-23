package speakthespiremod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.ui.buttons.PeekButton;

import basemod.ReflectionHacks;

public class PeekButtonPatch {
    @SpirePatch(clz = PeekButton.class, method = "render")
    public static class PeekButtonRenderPatch {
        public static void Postfix(PeekButton self, SpriteBatch sb) {
            boolean isHidden = ReflectionHacks.getPrivate(self, PeekButton.class, "isHidden");
            if (isHidden) {
                return;
            }

            FontHelper.renderFontCentered(sb, FontHelper.smallDialogOptionFont, "\"peek\"", self.hb.cX,
                    self.hb.y - 1.0F * Settings.scale, Settings.CREAM_COLOR);
        }
    }
}
