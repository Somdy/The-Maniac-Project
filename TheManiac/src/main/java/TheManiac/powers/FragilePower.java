package TheManiac.powers;

import TheManiac.TheManiac;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FragilePower extends AbstractManiacPower implements CloneablePowerInterface {
    public static final String POWER_ID = TheManiac.makeID("FragilePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public FragilePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.loadImg("Fragile");
        updateDescription();
    }

    @Override
    public float modifyBlockOnGaining(float blockAmount) {
        return Math.abs(blockAmount - this.amount);
    }

    @Override
    public void atStartOfTurn() {
        if (this.amount <= 1) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
        }
    }

    @Override
    public void updateDescription() {
        if (this.owner == AbstractDungeon.player) {
            this.description = DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[0] + this.owner.name + DESCRIPTIONS[2];
        }
        
        this.description += this.amount + DESCRIPTIONS[3] + DESCRIPTIONS[4];
    }

    @Override
    public AbstractPower makeCopy() {
        return new FragilePower(owner, amount);
    }
}
