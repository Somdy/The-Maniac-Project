package TheManiac.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;

public class ShatterAction extends AbstractGameAction {
    private AbstractMonster target;
    private DamageInfo info;
    
    public ShatterAction(AbstractMonster m, DamageInfo info) {
        this.target = m;
        this.info = info;
        this.actionType = ActionType.DAMAGE;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            if (target == null || info == null) {
                this.isDone = true;
                return;
            }
            
            if (target.isDeadOrEscaped()) {
                this.isDone = true;
                return;
            }
            AbstractDungeon.effectList.add(new VerticalImpactEffect(target.hb.cX + target.hb.width / 4F,
                    target.hb.cY - target.hb.height / 4.0F));
            target.loseBlock();
        }
        
        this.tickDuration();
        
        if (this.isDone) {
            target.damage(info);
            if (target.lastDamageTaken > 0 && !AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.effectList.add(new CleaveEffect());
                int dmg = target.lastDamageTaken;
                for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                    if (mo != target)
                    mo.damage(new DamageInfo(info.owner, dmg, DamageInfo.DamageType.NORMAL));
                }
            }
            this.addToTop(new WaitAction(0.1F));
        }
    }
}