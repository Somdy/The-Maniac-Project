package TheManiac.actions;

import TheManiac.powers.BleedingPower;
import TheManiac.powers.InfernoFlamePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CursedInfernoAction extends AbstractGameAction {
    private final boolean applyPowers;
    private int powers;

    public CursedInfernoAction(int damage) {
        this.source = AbstractDungeon.player;
        this.amount = damage;
        this.applyPowers = false;
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public CursedInfernoAction(int damage, int powers) {
        this.source = AbstractDungeon.player;
        this.amount = damage;
        this.powers = powers;
        this.applyPowers = true;
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (!monster.isDead && !monster.isDying) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(monster, new DamageInfo(this.source, this.amount, DamageInfo.DamageType.THORNS), AttackEffect.FIRE));
                }
                if (this.applyPowers) {
                    if (monster.lastDamageTaken > 0 && !monster.isDeadOrEscaped()) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, this.source, new InfernoFlamePower(monster, this.powers), this.powers));
                    }
                }
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        this.isDone = true;
    }
}
