package TheManiac.powers;

import TheManiac.actions.ThePossessedAction.PlagueDamageAction;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class PlaguePower extends AbstractManiacPower implements CloneablePowerInterface {
    public static final String POWER_ID = "maniac:PlaguePower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private AbstractCreature source;
    
    public PlaguePower(AbstractCreature owner, AbstractCreature source, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.source = source;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.loadImg("Plague");
        this.isTurnBased = true;
        updateDescription();
    }

    @Override
    public int onHeal(int healAmount) {
        return MathUtils.floor(healAmount * 0.5F);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.flashWithoutSound();
            this.addToBot(new PlagueDamageAction(this.owner, this.source, this.amount));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
        
        if (this.owner == AbstractDungeon.player) {
            this.description += DESCRIPTIONS[2];
        } else {
            this.description += DESCRIPTIONS[1];
        }
        
        this.description += this.amount + DESCRIPTIONS[3];
    }

    @Override
    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_POISON", 0.05F);
    }

    @Override
    public AbstractPower makeCopy() {
        return new PlaguePower(owner, source, amount);
    }
}
