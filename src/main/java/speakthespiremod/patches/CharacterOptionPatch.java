package speakthespiremod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;

public class CharacterOptionPatch {
    @SpirePatch(clz = CharacterOption.class, method = "render")
    public static class CharacterOptionRenderPatch {
        public static void Postfix(CharacterOption self, SpriteBatch sb) {
            if (self.name.length() < 4) {
                return;
            }

            // remove the proceeding "The " from the character name and wrap in quotes
            String briefCharacterName = "\"" + self.name.substring(4) + "\"";

            FontHelper.renderFontCentered(sb, FontHelper.smallDialogOptionFont, briefCharacterName, self.hb.cX,
                    self.hb.y - 13.0F * Settings.scale, Settings.CREAM_COLOR);
        }
    }
}
