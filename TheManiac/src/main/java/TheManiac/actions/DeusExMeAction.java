package TheManiac.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class DeusExMeAction extends AbstractGameAction {
    private int damage;
    
    public DeusExMeAction(AbstractCreature target, AbstractCreature source, int damage) {
        this.target = target;
        this.source = source;
        this.damage = damage;
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.target != null) {
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.BLUNT_HEAVY));
                this.target.damage(new DamageInfo(this.source, this.damage, DamageInfo.DamageType.NORMAL));
                if (this.target.lastDamageTaken > 0) {
                    this.addToBot(new GainEnergyAction(2));
                }
            }
            this.isDone = true;
        }
    }
}
