package TheManiac.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ExterminatorAction extends AbstractGameAction {
    private DamageInfo info;
    
    public ExterminatorAction(AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
    }
    
    @Override
    public void update() {
        this.isDone = true;
        if (this.target != null && this.target.currentHealth > 0) {
            int count = 0;
            for (AbstractCard card : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
                count++;
            }
            
            count--;
            
            for (int i = 0; i < count; i++) {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(this.target, this.info, this.attackEffect));
            }
        }
    }
}
