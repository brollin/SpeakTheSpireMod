package speakthespiremod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public class AbstractPlayerPatch {
    @SpirePatch(clz = AbstractPlayer.class, method = "update")
    public static class AbstractPlayerUpdatePatch {
        public static void Postfix(AbstractPlayer self) {
            self.isInKeyboardMode = false;
        }
    }
}
