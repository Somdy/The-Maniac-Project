package TheManiac.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;

public class DoublestrikeSphereAction extends AbstractGameAction {
    private DamageInfo info;
    private AbstractOrb orb;

    public DoublestrikeSphereAction(DamageInfo info, AbstractOrb orb) {
        this.info = info;
        this.orb = orb;
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = AttackEffect.NONE;
    }

    @Override
    public void update() {
        AbstractCreature mo = AbstractDungeon.getRandomMonster();
        if (mo != null) {
            float speedTime = 0.4F / (float)AbstractDungeon.player.orbs.size();
            if (Settings.FAST_MODE) {
                speedTime = 0.0F;
            }

            AbstractDungeon.actionManager.addToBottom(new DamageAction(mo, this.info, AttackEffect.SLASH_HORIZONTAL, true));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(mo, this.info, AttackEffect.SLASH_VERTICAL, true));

            if (this.orb != null) {
                AbstractDungeon.actionManager.addToTop(new VFXAction(new OrbFlareEffect(this.orb, OrbFlareEffect.OrbFlareColor.LIGHTNING), speedTime));
            }
        }

        this.isDone = true;
    }
}
