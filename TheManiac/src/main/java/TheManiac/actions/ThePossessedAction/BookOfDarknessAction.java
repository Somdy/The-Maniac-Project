package TheManiac.actions.ThePossessedAction;

import TheManiac.cards.the_possessed.risks.AbstractRisksCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.FlyingOrbEffect;

public class BookOfDarknessAction extends AbstractGameAction {
    private int damage;
    private int leastUnblocked;
    
    public BookOfDarknessAction(AbstractCreature source, int damage, int leastUnblocked) {
        this.source = source;
        this.damage = damage;
        this.leastUnblocked = leastUnblocked;
        this.startDuration = Settings.ACTION_DUR_FAST;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.DAMAGE;
    }
    
    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            int temp = AbstractDungeon.getMonsters().monsters.size();
            for (int i = 0; i < temp; i++) {
                if (!AbstractDungeon.getMonsters().monsters.get(i).isDying && AbstractDungeon.getMonsters().monsters.get(i).currentHealth > 0 &&
                        !AbstractDungeon.getMonsters().monsters.get(i).isEscaping) {
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(
                            AbstractDungeon.getMonsters().monsters.get(i).hb.cX,
                            AbstractDungeon.getMonsters().monsters.get(i).hb.cY, AttackEffect.FIRE));
                }
            }
        }
        
        this.tickDuration();
        if (this.isDone) {
            int backlash = 0;
            int monsters = AbstractDungeon.getMonsters().monsters.size();
            int dmg = this.damage;
            if (AbstractDungeon.player.masterDeck != null) {
                for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                    if (card instanceof AbstractRisksCard) {
                        dmg++;
                    }
                }
            }
            
            for (int i = 0; i < monsters; i++) {
                AbstractMonster target = AbstractDungeon.getMonsters().monsters.get(i);
                
                if (!target.isDying && target.currentHealth > 0 && !target.isEscaping) {
                    
                    target.damage(new DamageInfo(this.source, dmg, DamageInfo.DamageType.NORMAL));
                    
                    if (target.lastDamageTaken > 0) {
                        backlash += target.lastDamageTaken;
                    }
                }
            }
            
            if (backlash < this.leastUnblocked) {
                this.addToBot(new WaitAction(0.1F));
                
                for (int i = 0; i < monsters; i++) {
                    AbstractMonster target = AbstractDungeon.getMonsters().monsters.get(i);
                    if (!target.isDying && target.currentHealth > 0 && !target.isEscaping) {
                        for (int j = 0; j < target.lastDamageTaken / 2; j++) {
                            this.addToBot(new VFXAction(new FlyingOrbEffect(target.hb.cX, target.hb.cY)));
                        }
                    }
                }
                
                this.addToBot(new LoseHPAction(this.source, this.source, backlash, AttackEffect.FIRE));
            }
        }
    }
}
