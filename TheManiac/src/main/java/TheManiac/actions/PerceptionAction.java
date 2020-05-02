package TheManiac.actions;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.vfx.combat.LightBulbEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;

import java.awt.*;

public class PerceptionAction extends AbstractGameAction {
    private AbstractMonster targetMonster;
    private int drawAmt;
    private int powersToApply;
    private boolean intentAttack = false;
    private boolean intentDebuff = false;
    private boolean intentBuff = false;

    public PerceptionAction(AbstractCreature source, int drawAmt, int powersToApply, AbstractMonster targetMonster) {
        this.duration = 0.0F;
        this.source = source;
        this.drawAmt = drawAmt;
        this.powersToApply = powersToApply;
        this.targetMonster = targetMonster;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (this.targetMonster != null) {
            if (this.targetMonster.intent.equals(AbstractMonster.Intent.ATTACK)) {
                if (!intentAttack) {
                    intentAttack = true;
                }
            }
            if (this.targetMonster.intent.equals(AbstractMonster.Intent.ATTACK_BUFF) || this.targetMonster.intent.equals(AbstractMonster.Intent.ATTACK_DEFEND)) {
                if (!intentAttack) {
                    intentAttack = true;
                }
                if (!intentBuff) {
                    intentBuff = true;
                }
            }
            if (this.targetMonster.intent.equals(AbstractMonster.Intent.ATTACK_DEBUFF)) {
                if (!intentAttack) {
                    intentAttack = true;
                }
                if (!intentDebuff) {
                    intentDebuff = true;
                }
            }
            if (this.targetMonster.intent.equals(AbstractMonster.Intent.BUFF) || this.targetMonster.intent.equals(AbstractMonster.Intent.DEFEND_BUFF)
            || this.targetMonster.intent.equals(AbstractMonster.Intent.DEFEND)) {
                if (!intentBuff) {
                    intentBuff = true;
                }
            }
            if (this.targetMonster.intent.equals(AbstractMonster.Intent.DEBUFF) || this.targetMonster.intent.equals(AbstractMonster.Intent.DEFEND_DEBUFF)
            || this.targetMonster.intent.equals(AbstractMonster.Intent.STRONG_DEBUFF)) {
                if (!intentDebuff) {
                    intentDebuff = true;
                }
            }


            if (intentAttack) {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this.source, new VerticalImpactEffect(this.targetMonster.hb_x, this.targetMonster.hb_y), 0f));
                AbstractDungeon.actionManager.addToBottom(new StunMonsterAction(this.targetMonster, this.source));
            }
            if (intentDebuff) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.source, this.source, new ArtifactPower(this.source, this.powersToApply), this.powersToApply));
            }
            if (intentBuff) {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this.source, new LightBulbEffect(this.source.hb), 0.1f));
                AbstractDungeon.actionManager.addToBottom(new DrawCardAction(this.source, this.drawAmt, false));
            }
        }

        this.isDone = true;
    }
}
