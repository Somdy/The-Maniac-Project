package TheManiac.patches;

import TheManiac.cards.the_possessed.ManiacRisksCard;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.Soul;

public class ObtainCardPatch {
    
    @SpirePatch( clz = Soul.class, method = "obtain" )
    public static class obtainCardEffectPatch {
        
        @SpireInsertPatch( rloc = 0 )
        public static void Insert(Soul _instance, AbstractCard _card) {
            if (_card instanceof ManiacRisksCard) {
                ((ManiacRisksCard) _card).onObtain();
            }
        }
    }
}
