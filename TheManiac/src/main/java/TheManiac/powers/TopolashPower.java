package TheManiac.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class TopolashPower extends AbstractManiacPower implements CloneablePowerInterface {
    public static final String POWER_ID = "maniac:TopolashPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    public TopolashPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.loadImg("Topolash");
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        this.addToBot(new LoseEnergyAction(this.amount));
        this.flash();
    }

    @Override
    public void updateDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(powerStrings.DESCRIPTIONS[0]);
        for (int i = 0; i < this.amount; i++) {
            sb.append("[E] ");
        }
        if (powerStrings.DESCRIPTIONS[1].isEmpty()) {
            sb.append(LocalizedStrings.PERIOD);
        } else {
            sb.append(powerStrings.DESCRIPTIONS[1]);
        }
        this.description = sb.toString();
    }

    @Override
    public AbstractPower makeCopy() {
        return new TopolashPower(owner, amount);
    }
}
