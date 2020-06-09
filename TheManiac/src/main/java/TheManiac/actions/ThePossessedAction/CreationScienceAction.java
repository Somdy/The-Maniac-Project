package TheManiac.actions.ThePossessedAction;

import TheManiac.cards.the_possessed.shinies.AbstractShiniesCard;
import TheManiac.cards.the_possessed.shinies.CreationScience;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;


public class CreationScienceAction extends AbstractGameAction {
    
    public CreationScienceAction() {
        this.startDuration = Settings.ACTION_DUR_FAST;
        this.duration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            
            if (AbstractDungeon.player.masterDeck.isEmpty()) {
                this.isDone = true;
                return;
            }
            
            if (noGloriesInDeck()) {
                this.isDone = true;
                return;
            }
            
            for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                if (card instanceof AbstractShiniesCard && !card.cardID.equals(CreationScience.ID)) {
                    AbstractCard tmp = card.makeCopy();
                    tmp.modifyCostForCombat(-9);
                    tmp.isCostModified = true;
                    this.addToBot(new MakeTempCardInDrawPileAction(tmp, 1, true, true));
                }
            }
        }
        
        this.tickDuration();
    }
    
    private boolean noGloriesInDeck() {
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card instanceof AbstractShiniesCard) {
                return false;
            }
        }
        
        return true;
    }
}
