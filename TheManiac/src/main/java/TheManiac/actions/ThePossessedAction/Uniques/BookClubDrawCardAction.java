package TheManiac.actions.ThePossessedAction.Uniques;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;

public class BookClubDrawCardAction extends AbstractGameAction {
    private DamageInfo info;
    
    public BookClubDrawCardAction(DamageInfo info) {
        this.info = info;
        this.startDuration = Settings.ACTION_DUR_FAST;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.WAIT;
    }
    
    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            
            for (AbstractCard card : DrawCardAction.drawnCards) {
                if (card.costForTurn >= 2) {
                    this.addToBot(new DamageAllEnemiesAction(info.owner, DamageInfo.createDamageMatrix(info.base, false, false), info.type, AttackEffect.FIRE));
                }
            }
            
        }
        
        this.isDone = true;
    }
}
