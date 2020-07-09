package TheManiac.powers;

import TheManiac.TheManiac;
import TheManiac.cards.colorless.skill.Unravel;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class PerceptionPower extends AbstractManiacPower implements CloneablePowerInterface {
    public static final String POWER_ID = TheManiac.makeID("PerceptionPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean upgrade;
    
    public PerceptionPower(AbstractCreature owner, int amount, boolean upgrade) {
        this.name = NAME;
        this.ID = POWER_ID + (upgrade ? "advanced" : "general");
        this.owner = owner;
        this.amount = amount;
        this.upgrade = upgrade;
        this.type = PowerType.BUFF;
        this.loadImg("Ravel");
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!canUnravel()) return;
        this.flash();
        Unravel unravel = new Unravel();
        if (upgrade) unravel.upgrade();
        this.addToBot(new MakeTempCardInDiscardAction(unravel, 1));
    }
    
    private boolean canUnravel() {
        if (!AbstractDungeon.player.powers.isEmpty()) {
            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p instanceof AbstractManiacPower && ((AbstractManiacPower) p).isUnravelling)
                    return false;
            }
        }
        
        return true;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[upgrade ? 2 : 1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new PerceptionPower(owner, amount, upgrade);
    }
}