package TheManiac.patches;

import TheManiac.TheManiac;
import TheManiac.cards.the_possessed.ManiacRisksCard;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.CardRewardScreen;

public class HoverRisksCardPatch {
    
    @SpirePatch( clz = CardRewardScreen.class, method = "cardSelectUpdate" )
    public static class selectCardEffect {
        
        @SpireInsertPatch(rloc = 15)
        public static void Insert(CardRewardScreen _instance) {
            for (AbstractCard card : _instance.rewardGroup) {
                if (card.hb.justHovered && card instanceof ManiacRisksCard) {
                    CardCrawlGame.sound.play(TheManiac.makeID("ChooseRisks"));
                }
            }
        }
    }
}
