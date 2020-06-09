package TheManiac.actions.ThePossessedAction;

import TheManiac.cards.the_possessed.possessed.BookClub;
import TheManiac.powers.PlaguePower;
import TheManiac.vfx.FlashManiacAtkEffect;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class PlagueDamageAction extends AbstractGameAction {
    private String attackEffect;
    
    public PlagueDamageAction(AbstractCreature target, AbstractCreature source, int amount) {
        this.setValues(target, source, amount);
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
                AbstractDungeon.effectList.add(new FlashManiacAtkEffect(this.target.hb.cX, this.target.hb.cY, "PLAGUE", false));
            }

            this.tickDuration();
            if (this.isDone) {
                if (this.target.currentHealth > 0) {
                    this.target.tint.color = Color.RED.cpy();
                    this.target.tint.changeColor(Color.WHITE.cpy());
                    this.target.damage(new DamageInfo(this.source, this.amount, DamageInfo.DamageType.THORNS));
                    if (this.target.lastDamageTaken > 0 && this.source == AbstractDungeon.player && playerHasConviction()) {
                        this.addToBot(new HealAction(this.source, this.target, this.target.lastDamageTaken, 0.05F));
                    }
                }

                AbstractPower power = this.target.getPower(PlaguePower.POWER_ID);
                if (power != null) {
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
    
    private boolean playerHasConviction() {
        AbstractPlayer player = AbstractDungeon.player;
        
        if (!player.masterDeck.isEmpty() && player.masterDeck.contains(new BookClub())) {
            return true;
        }
        
        if (!player.drawPile.isEmpty() && player.drawPile.contains(new BookClub())) {
            return true;
        }
        
        if (!player.discardPile.isEmpty() && player.discardPile.contains(new BookClub())) {
            return true;
        }
        
        if (!player.exhaustPile.isEmpty() && player.exhaustPile.contains(new BookClub())) {
            return true;
        }
        
        return false;
    }
}
