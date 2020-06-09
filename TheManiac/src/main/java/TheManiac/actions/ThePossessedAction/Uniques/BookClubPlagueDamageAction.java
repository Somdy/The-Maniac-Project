package TheManiac.actions.ThePossessedAction.Uniques;

import TheManiac.powers.PlaguePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class BookClubPlagueDamageAction extends AbstractGameAction {
    
    public BookClubPlagueDamageAction(AbstractCreature source, AbstractCreature target) {
        this.source = source;
        this.target = target;
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FAST;
    }
    
    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.target == null) {
                this.isDone = true;
                return;
            }
            
            if (!this.target.hasPower(PlaguePower.POWER_ID)) {
                this.isDone = true;
                return;
            }
            
            int dmg = this.target.getPower(PlaguePower.POWER_ID).amount * 2;
            
            this.addToBot(new VFXAction(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.FIRE)));
            this.target.damage(new DamageInfo(this.source, dmg, DamageInfo.DamageType.THORNS));
            if (this.target.lastDamageTaken > 0) {
                this.addToBot(new GainBlockAction(AbstractDungeon.player, this.target.lastDamageTaken));
            }
        }
        
        this.isDone = true;
    }
}
