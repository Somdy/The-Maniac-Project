package TheManiac.actions;

import TheManiac.stances.LimboStance;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;

public class SeekerKnifeAction extends AbstractGameAction {
    private int damage;
    private AbstractCreature source;
    private AbstractMonster targetMonster;

    public SeekerKnifeAction(AbstractCreature source, AbstractMonster targetMonster, int damage) {
        this.source = source;
        this.targetMonster = targetMonster;
        this.damage = damage;
        this.actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        if (this.targetMonster != null) {
            if (AbstractDungeon.player.stance.ID.equals(LimboStance.STANCE_ID)) {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this.source, new CleaveEffect(), 0.25f));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(targetMonster, new DamageInfo(this.source, this.damage, DamageInfo.DamageType.NORMAL), AttackEffect.SLASH_HORIZONTAL));
                for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                    if (monster != this.targetMonster) {
                        monster.damage(new DamageInfo(this.source, this.damage/2, DamageInfo.DamageType.NORMAL));
                    }
                }
            }
            else {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(targetMonster, new DamageInfo(this.source, this.damage, DamageInfo.DamageType.NORMAL), AttackEffect.SLASH_HORIZONTAL));
            }
        }

        this.isDone = true;
    }
}
