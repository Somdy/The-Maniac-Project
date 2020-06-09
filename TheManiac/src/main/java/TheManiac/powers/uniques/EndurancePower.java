package TheManiac.powers.uniques;

import TheManiac.TheManiac;
import TheManiac.powers.AbstractManiacPower;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class EndurancePower extends AbstractManiacPower implements CloneablePowerInterface {
    public static final String POWER_ID = TheManiac.makeID("EndurancePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    public EndurancePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.loadImg("Endurance");
        updateDescription();
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        if (damage > 0 && type == DamageInfo.DamageType.NORMAL) {
            damage -= this.amount;
        }
        
        return damage;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.owner.name + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new EndurancePower(owner, amount);
    }
}
