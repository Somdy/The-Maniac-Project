package TheManiac.patches;

import TheManiac.cards.the_possessed.ManiacRisksCard;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class CombatVictoryCardPatch {
    
    @SpirePatch( clz = AbstractPlayer.class, method = "onVictory" )
    public static class onVictoryCombat {
        
        @SpireInsertPatch(rloc = 2)
        public static void Insert(AbstractPlayer _instance) {
            if (!AbstractDungeon.player.masterDeck.isEmpty()) {
                for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                    if (card instanceof ManiacRisksCard) {
                        ((ManiacRisksCard) card).onVictoryCombat(true);
                    }
                }
            }
            
            for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                if (card instanceof ManiacRisksCard) {
                    ((ManiacRisksCard) card).onVictoryCombat(false);
                }
            }
        }
    }
}
