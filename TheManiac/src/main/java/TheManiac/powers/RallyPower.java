package TheManiac.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class RallyPower extends AbstractManiacPower implements CloneablePowerInterface {
    public static final String POWER_ID = "maniac:RallyPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/RallyPower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/RallyPower.png";
    
    public RallyPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.loadImg("Rally");
        /*this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);*/
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (this.amount > 0 && (card.costForTurn == 0 || card.energyOnUse == 0)) {
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(this.owner, this.amount, false));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount;
        if (this.amount > 1) {
            this.description += DESCRIPTIONS[2];
        }
        else {
            this.description += DESCRIPTIONS[1];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new RallyPower(owner, amount);
    }
}
