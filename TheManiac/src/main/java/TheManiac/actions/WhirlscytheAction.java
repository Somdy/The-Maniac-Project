package TheManiac.actions;

import TheManiac.stances.LimboStance;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;

public class WhirlscytheAction extends AbstractGameAction {
    private boolean freeToPlayOnce;
    private DamageInfo.DamageType damageType;
    private int energyOnUse;
    private boolean upgraded;

    public WhirlscytheAction(boolean freeToPlayOnce, int energyOnUse, boolean upgraded) {
        this.damageType = DamageInfo.DamageType.HP_LOSS;
        this.freeToPlayOnce = freeToPlayOnce;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
        this.upgraded = upgraded;
        this.attackEffect = AttackEffect.SLASH_HORIZONTAL;
    }

    @Override
    public void update() {
        int effect = EnergyPanel.totalCount;
        int damageEffect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
            damageEffect = this.energyOnUse;
        }

        if (AbstractDungeon.player.hasRelic("Chemical X")) {
            effect += 2;
            damageEffect += 2;
            AbstractDungeon.player.getRelic("Chemical X").flash();
        }

        if (this.upgraded) {
            effect += 1;
            damageEffect += 1;
        }

        if (AbstractDungeon.player.stance.ID.equals(LimboStance.STANCE_ID)) {
            if (AbstractDungeon.player.hasPower(StrengthPower.POWER_ID)) {
                effect += AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount;
            }
        }

        if (effect > 0) {
            for (int i = 0; i < effect; i ++) {
                if (i == 0) {
                    AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_WHIRLWIND"));
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new DieDieDieEffect(), 0.25F));
                }

                AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_HEAVY"));
                AbstractDungeon.actionManager.addToBottom(new RandomEnemyLoseHPAction(AbstractDungeon.player, damageEffect, this.attackEffect));
            }

            if (!this.freeToPlayOnce) {
                AbstractDungeon.player.energy.use(EnergyPanel.totalCount);
            }
        }

        this.isDone = true;
    }
}
