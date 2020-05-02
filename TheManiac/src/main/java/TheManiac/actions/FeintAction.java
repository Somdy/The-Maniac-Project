package TheManiac.actions;

import TheManiac.powers.WeaknessPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class FeintAction extends AbstractGameAction {
    private int drawAmt;
    private int powersToApply;
    private AbstractMonster targetMonster;

    public FeintAction(AbstractCreature source, int damage, int powersToApply, int drawAmt, AbstractMonster targetMonster) {
        this.duration = 0.0F;
        this.actionType = ActionType.WAIT;
        this.source = source;
        this.amount = damage;
        this.powersToApply = powersToApply;
        this.drawAmt = drawAmt;
        this.targetMonster = targetMonster;
    }

    @Override
    public void update() {
        if (this.targetMonster != null) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(targetMonster, new DamageInfo(this.source, this.amount, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_LIGHT));

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }

            if (this.targetMonster.getIntentBaseDmg() >= 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.targetMonster, this.source, new WeaknessPower(this.targetMonster, this.powersToApply), this.powersToApply));
            }
            else {
                AbstractDungeon.actionManager.addToBottom(new DrawCardAction(this.source, this.drawAmt, false));
            }
        }

        this.isDone = true;
    }
}
