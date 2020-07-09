package TheManiac.actions;

import TheManiac.helper.ManiacImageMaster;
import TheManiac.stances.LimboStance;
import TheManiac.vfx.AssassinateEffect;
import TheManiac.vfx.AssassinateLethalEffect;
import TheManiac.vfx.DimScreenEffect;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.ClashEffect;

public class AssassinateAction extends AbstractGameAction {
    private DimScreenEffect dim;
    private AbstractPlayer p;
    
    public AssassinateAction(int amount) {
        this.amount = amount;
        this.p = AbstractDungeon.player;
        this.dim = new DimScreenEffect();
        AbstractDungeon.effectsQueue.add(this.dim);
        this.duration = this.startDuration = 1.25F;
        this.actionType = ActionType.DAMAGE;
    }
    
    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                this.isDone = true;
                return;
            }
            this.target = AbstractDungeon.getMonsters().getRandomMonster(true);
            AbstractDungeon.effectList.add(new AssassinateEffect(p.hb.cX, p.hb.cY, target.hb.cX, target.hb.cY));
        }
        
        this.tickDuration();
        if (this.isDone) {
            AbstractDungeon.effectsQueue.add(new BorderFlashEffect(
                    ManiacImageMaster.ColorDeviator(Color.RED, 0.25F, 0.45F), true));
            AbstractDungeon.effectsQueue.add(new AssassinateLethalEffect(target.hb.cX, target.hb.cY));
            target.damage(new DamageInfo(p, target.maxHealth / 2, DamageInfo.DamageType.NORMAL));
            if (!target.isDeadOrEscaped() && p.stance.ID.equals(LimboStance.STANCE_ID)) {
                this.addToBot(new ApplyPowerAction(target, p, new StrengthPower(target, -amount), -amount));
            }
            this.dim.lighten();
        }
    }
}
