package TheManiac.actions.ThePossessedAction.Uniques;

import TheManiac.cards.the_possessed.possessed.AbstractPossessedCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class RollNextCardMoveAction extends AbstractGameAction {
    private AbstractCard card;
    
    public RollNextCardMoveAction(AbstractCard card) {
        this.card = card;
    }
    
    @Override
    public void update() {
        if (this.card instanceof AbstractPossessedCard) {
            ((AbstractPossessedCard) this.card).rollNextMove();
        }
        this.isDone = true;
    }
}
