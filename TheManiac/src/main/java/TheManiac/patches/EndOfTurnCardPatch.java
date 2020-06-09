package TheManiac.patches;

import TheManiac.cards.the_possessed.ManiacRisksCard;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class EndOfTurnCardPatch {
    
    @SpirePatch( clz = AbstractRoom.class, method = "applyEndOfTurnRelics" )
    public static class applyEndOfTurnCard {
        
        @SpireInsertPatch(rloc = 0)
        public static void Insert(AbstractRoom _instance) {
            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                if (card instanceof ManiacRisksCard) {
                    ((ManiacRisksCard) card).atEndOfTurn(true, false);
                }
            }
            
            for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                if (card instanceof ManiacRisksCard) {
                    ((ManiacRisksCard) card).atEndOfTurn(false, true);
                }
            }
            
            for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                if (card instanceof ManiacRisksCard) {
                    ((ManiacRisksCard) card).atEndOfTurn(false, false);
                }
            }
        }
    }
}
