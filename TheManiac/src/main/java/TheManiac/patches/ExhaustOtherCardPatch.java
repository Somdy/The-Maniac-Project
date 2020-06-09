package TheManiac.patches;

import TheManiac.cards.the_possessed.ManiacRisksCard;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ExhaustOtherCardPatch {
    
    @SpirePatch( clz = CardGroup.class, method = "moveToExhaustPile" )
    public static class onMoveToExhaustPile {
        
        @SpireInsertPatch(rloc = 0)
        public static void Insert(CardGroup _instance, AbstractCard _card) {
            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                if (card instanceof ManiacRisksCard) {
                    ((ManiacRisksCard) card).onExhaustOtherCard(_card, true);
                }
            }
            
            for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                if (card instanceof ManiacRisksCard) {
                    ((ManiacRisksCard) card).onExhaustOtherCard(_card, false);
                }
            }

            for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                if (card instanceof ManiacRisksCard) {
                    ((ManiacRisksCard) card).onExhaustOtherCard(_card, false);
                }
            }
        }
    }
}
