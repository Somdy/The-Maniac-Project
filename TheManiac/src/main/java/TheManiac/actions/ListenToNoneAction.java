package TheManiac.actions;

import TheManiac.stances.LimboStance;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;

import java.util.Iterator;

public class ListenToNoneAction extends AbstractGameAction {
    public int conditionalAmount;
    public AbstractCard.CardType restrictedType;

    public ListenToNoneAction(int amount, int conditionalAmount, AbstractCard.CardType restrictedType) {
        this.source = AbstractDungeon.player;
        this.target = AbstractDungeon.player;
        this.amount = amount;
        this.conditionalAmount = conditionalAmount;
        this.restrictedType = restrictedType;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.WAIT;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.checkCondition()) {
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this.target, this.source, this.amount));
                if (AbstractDungeon.player.stance.ID.equals(LimboStance.STANCE_ID)) {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this.target, this.source, this.conditionalAmount));
                }
            }
            this.isDone = true;
        }
    }

    private boolean checkCondition() {
        Iterator var1 = AbstractDungeon.player.hand.group.iterator();
        AbstractCard c;
        do {
            if (!var1.hasNext()) {
                return true;
            }
            c = (AbstractCard)var1.next();
        } while(c.type != this.restrictedType);
        return false;
    }
}
