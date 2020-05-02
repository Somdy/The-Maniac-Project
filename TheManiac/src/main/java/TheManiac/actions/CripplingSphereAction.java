package TheManiac.actions;

import TheManiac.powers.BleedingPower;
import TheManiac.powers.WeaknessPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;

import java.util.Iterator;

public class CripplingSphereAction extends AbstractGameAction {
    private DamageInfo info;
    private AbstractOrb orb;
    private boolean evoke;
    private boolean muteSfx = false;

    public CripplingSphereAction(DamageInfo info, AbstractOrb orb, boolean evoke, int powersToApply) {
        this.info = info;
        this.orb = orb;
        this.evoke = evoke;
        this.amount = powersToApply;
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = AttackEffect.FIRE;
        this.duration = 0.1f;
    }

    @Override
    public void update() {
        AbstractMonster weakestMonster = null;
        Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

        while(var1.hasNext()) {
            AbstractMonster m = (AbstractMonster)var1.next();
            if (!m.isDeadOrEscaped()) {
                if (weakestMonster == null) {
                    weakestMonster = m;
                } else if (m.currentHealth < weakestMonster.currentHealth) {
                    weakestMonster = m;
                }
            }
        }

        this.target = weakestMonster;

        if (!this.shouldCancelAction() && this.target != null) {
            if (this.duration == 0.1f) {
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect, this.muteSfx));
            }
            this.tickDuration();
            if (this.isDone) {
                if (this.attackEffect == AttackEffect.FIRE) {
                    this.target.tint.color = Color.SALMON.cpy();
                }
                else {
                    this.target.tint.color = Color.SCARLET.cpy();
                }
                this.target.tint.changeColor(Color.WHITE.cpy());

                this.target.damage(this.info);
                if (this.evoke) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.target, AbstractDungeon.player, new BleedingPower(this.target, this.amount), this.amount));
                }
                else {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.target, AbstractDungeon.player, new WeaknessPower(this.target, this.amount), this.amount));
                }

                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }
            }
        }
        else {
            this.isDone = true;
        }
    }
}
