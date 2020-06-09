package TheManiac.powers;

import TheManiac.TheManiac;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FoilsPower extends AbstractManiacPower implements CloneablePowerInterface {
    public static final String POWER_ID = TheManiac.makeID("FoilsPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    public FoilsPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.loadImg("Foils");
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (this.amount > 0 && this.owner != null) {
            this.flash();
            this.addToBot(new GainBlockAction(this.owner, this.amount));
            this.amount--;
        }
        if (this.amount == 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new FoilsPower(owner, amount);
    }
}
