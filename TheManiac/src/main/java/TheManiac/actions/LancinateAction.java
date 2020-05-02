package TheManiac.actions;

import TheManiac.powers.BleedingPower;
import TheManiac.powers.WeaknessPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class LancinateAction extends AbstractGameAction {
    private DamageInfo damageInfo;
    private String powerToApply;
    private int powerAmount;
    private String targetPower;

    public LancinateAction(AbstractCreature target, String powerToApply, String targetPower, DamageInfo damageInfo, int powerAmount) {
        this.actionType = ActionType.DAMAGE;
        this.target = target;
        this.damageInfo = damageInfo;
        this.powerAmount = powerAmount;
        this.targetPower = targetPower;
        this.powerToApply = powerToApply;
    }

    @Override
    public void update() {
        if (this.target != null && this.target.hasPower(targetPower)) {
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1, false));
        }

        AbstractDungeon.actionManager.addToBottom(new DamageAction(this.target, this.damageInfo, AttackEffect.SLASH_HORIZONTAL));
        if (this.powerToApply.equals(BleedingPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.target, AbstractDungeon.player, new BleedingPower(this.target, this.powerAmount), this.powerAmount));
        }
        else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.target, AbstractDungeon.player, new WeaknessPower(this.target, this.powerAmount), this.powerAmount));
        }

        this.isDone = true;
    }
}
