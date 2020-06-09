package TheManiac.actions.ThePossessedAction;

import TheManiac.powers.FragilePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

public class FuryOfAirAction extends AbstractGameAction {
    private DamageInfo info;
    
    public FuryOfAirAction(AbstractCreature target, AbstractCreature source, int amount, DamageInfo info) {
        this.target = target;
        this.source = source;
        this.amount = amount;
        this.info = info;
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.target == null || this.target.isDeadOrEscaped()) {
                this.isDone = true;
                return;
            }
            
            if (this.target.currentBlock > 0){
                
                this.addToBot(new VFXAction(new LightningEffect(this.source.drawX, this.source.drawY), 0.1F));
                this.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));
                this.addToBot(new DamageAction(this.source, this.info, AttackEffect.NONE));
                this.addToBot(new ApplyPowerAction(this.source, this.source, new FragilePower(this.source, this.amount), this.amount));
                
                if (this.target.currentBlock >= this.info.base) {
                    this.isDone = true;
                    return;
                }
            }

            this.addToBot(new VFXAction(new LightningEffect(this.target.drawX, this.target.drawY), 0.1F));
            this.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));
            this.addToBot(new DamageAction(this.target, this.info, AttackEffect.NONE));
            this.addToBot(new ApplyPowerAction(this.target, this.source, new FragilePower(this.target, this.amount), this.amount));
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (!monster.isDeadOrEscaped() && monster.drawX > this.target.drawX) {
                    this.addToBot(new VFXAction(new LightningEffect(monster.drawX, monster.drawY), 0.05F));
                    this.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));
                    this.addToBot(new DamageAction(monster, this.info, AttackEffect.NONE));
                    this.addToBot(new ApplyPowerAction(monster, this.source, new FragilePower(monster, this.amount), this.amount));
                }
            }
        }
        
        this.tickDuration();
    }
}
