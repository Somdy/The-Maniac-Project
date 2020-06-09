package TheManiac.patches.MinionPatches;

import TheManiac.minions.AbstractManiacMinion;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.lang.reflect.Field;

public class MinionUnhoveredPatch {
    
    @SpirePatch( clz = AbstractPlayer.class, method = "updateTargetArrowWithKeyboard")
    public static class NotAllowMinionTarget {
        
        @SpirePostfixPatch
        public static void Posfix(AbstractPlayer _instance) throws Exception {
            Field hoveredMonster = AbstractPlayer.class.getDeclaredField("hoveredMonster");
            hoveredMonster.setAccessible(true);
            
            if (hoveredMonster.get(_instance) instanceof AbstractManiacMinion) {
                hoveredMonster.set(_instance, null);
            }
        }
    }
    
    @SpirePatch( clz = AbstractCard.class, method = "cardPlayable")
    public static class NotPlayOnMinions {
        
        public static SpireReturn<Boolean> Prefix(AbstractCard _instance, AbstractMonster m) {
            if (((_instance.target == AbstractCard.CardTarget.ENEMY || _instance.target == AbstractCard.CardTarget.SELF_AND_ENEMY) && m instanceof AbstractManiacMinion)) {
                _instance.cantUseMessage = "...?";
                return SpireReturn.Return(false);
            }
            
            return SpireReturn.Continue();
        }
    }
}
