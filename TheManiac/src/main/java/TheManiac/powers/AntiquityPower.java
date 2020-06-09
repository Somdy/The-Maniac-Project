package TheManiac.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class AntiquityPower extends AbstractManiacPower implements CloneablePowerInterface {
    public static final String POWER_ID = "maniac:AntiquityPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/AntiquityPower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/AntiquityPower.png";

    public AntiquityPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.canGoNegative = true;
        this.loadImg("Antiquity");
        /*this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);*/
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;

        if (this.amount == 0) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }

        if (this.amount > 100) {
            this.amount = 100;
        }
        if (this.amount < -100) {
            this.amount = -100;
        }
    }

    @Override
    public void reducePower(int reduceAmount) {
        this.fontScale = 8.0F;
        this.amount -= reduceAmount;

        if (this.amount == 0) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }

        if (this.amount > 100) {
            this.amount = 100;
        }
        if (this.amount < -100) {
            this.amount = -100;
        }
    }

    @Override
    public void updateDescription() {
        if (this.amount > 0) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
            this.type = PowerType.BUFF;
        }
        else {
            int decrement = -this.amount;
            this.description = DESCRIPTIONS[1] + decrement + DESCRIPTIONS[2];
            this.type = PowerType.DEBUFF;
        }
    }

    @Override
    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_FOCUS", 0.05F);
    }

    @Override
    public AbstractPower makeCopy() {
        return new AntiquityPower(owner, amount);
    }
}
