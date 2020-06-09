package TheManiac.powers.uniques;

import TheManiac.TheManiac;
import TheManiac.powers.AbstractManiacPower;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.RegenPower;

public class SelfrepairPower extends AbstractManiacPower implements CloneablePowerInterface {
    public static final String POWER_ID = TheManiac.makeID("SelfrepairPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public SelfrepairPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.loadImg("Selfrepair");
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && this.amount > 0) {
            this.flash();
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new RegenPower(this.owner, this.amount), this.amount));
        }
        return damageAmount;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.owner.name + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new SelfrepairPower(owner, amount);
    }
}
