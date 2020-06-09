package TheManiac.powers;

import TheManiac.cards.status.TheUndigested;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SalivaPower extends AbstractManiacPower implements CloneablePowerInterface {
    public static final String POWER_ID = "maniac:SalivaPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/SalivaPower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/SalivaPower.png";
    private AbstractCreature source;
    
    public SalivaPower(AbstractCreature source, AbstractCreature target, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.source = source;
        this.owner = target;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.loadImg("Saliva");
        /*this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);*/
        updateDescription();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (target == this.source) {
            this.owner.damage(new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS));
            this.amount--;
            if (this.amount == 0) {
                this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            }
            this.updateDescription();
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card.cardID.equals(TheUndigested.ID)) {
            if (this.amount > 2) {
                this.amount -= 2;
            } else {
                this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.source.name + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new SalivaPower(source, owner, amount);
    }
}
