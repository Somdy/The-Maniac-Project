package TheManiac.actions;

import TheManiac.powers.ExoticPoisonPower;
import TheManiac.vfx.FlashManiacAtkEffect;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class ExoticPoisonDamageAction extends AbstractGameAction {
    private String attackEffect;
    private boolean pureDmg;
    
    public ExoticPoisonDamageAction(AbstractCreature target, AbstractCreature source, int amount, String effect, boolean pureDmg) {
        this.setValues(target, source, amount);
        this.attackEffect = effect;
        this.pureDmg = pureDmg;
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.3F;
    }
    
    @Override
    public void update() {
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            this.isDone = true;
            return;
        } else {
            if (this.duration == 0.3F && this.target.currentHealth > 0) {
                AbstractDungeon.effectList.add(new FlashManiacAtkEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect, false));
            }
            
            this.tickDuration();
            if (this.isDone) {
                if (this.target.currentHealth > 0) {
                    this.target.tint.color = Color.PURPLE.cpy();
                    this.target.tint.changeColor(Color.WHITE.cpy());
                    if (this.pureDmg) {
                        this.target.damage(new DamageInfo(this.source, this.amount, DamageInfo.DamageType.HP_LOSS));
                    } else {
                        this.target.damage(new DamageInfo(this.source, this.amount, DamageInfo.DamageType.THORNS));
                    }
                }

                AbstractPower power = this.target.getPower(ExoticPoisonPower.POWER_ID);
                if (power != null && this.target == AbstractDungeon.player) {
                    power.amount--;
                    if (power.amount == 0) {
                        this.target.powers.remove(power);
                    } else {
                        power.updateDescription();
                    }
                }

                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }

                this.addToTop(new WaitAction(0.1F));
            }
        }
    }
}
