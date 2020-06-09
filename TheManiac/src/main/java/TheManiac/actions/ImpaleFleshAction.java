package TheManiac.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;

public class ImpaleFleshAction extends AbstractGameAction {
    private boolean takeDamage;

    public ImpaleFleshAction(AbstractCreature source, AbstractCreature target, int damage, boolean takeDamage) {
        this.source = source;
        this.target = target;
        this.amount = damage;
        this.takeDamage = takeDamage;
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (!this.target.isDying && this.target.currentHealth > 0 && !this.target.isEscaping) {
                this.target.damage(new DamageInfo(this.source, this.amount, DamageInfo.DamageType.NORMAL));
                if (this.takeDamage) {
                    if (this.target.lastDamageTaken > 0) {
                        int feedBack = this.target.lastDamageTaken / 2;
                        if (feedBack > 0) {
                            if (!Settings.FAST_MODE) {
                                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.2f));
                            }
                            AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(this.source.hb.cX, this.source.hb.cY - 40.0F * Settings.scale,
                                    Settings.GOLD_COLOR.cpy()), 0.1F));
                            this.source.damage(new DamageInfo(this.source, feedBack, DamageInfo.DamageType.NORMAL));
                        }
                    }
                }
            }
        }

        this.tickDuration();
    }
}