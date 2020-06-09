package TheManiac.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SwellingPower extends AbstractManiacPower implements CloneablePowerInterface {
    public static final String POWER_ID = "maniac:SwellingPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/SwellingPower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/SwellingPower.png";
    
    public SwellingPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.loadImg("Swelling");
        /*this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);*/
        updateDescription();
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (this.amount > 0 && info.type == DamageInfo.DamageType.NORMAL) {
            this.flash();
            if (damageAmount < this.amount) {
                this.amount -= damageAmount;
                return 0;
            }
            else {
                damageAmount -= this.amount;
                this.amount = 0;
                this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
                return damageAmount;
            }
        }
        return super.onAttackedToChangeDamage(info, damageAmount);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.owner.name + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new SwellingPower(owner, amount);
    }
}
