package TheManiac.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class PuzzledPower extends AbstractManiacPower implements CloneablePowerInterface {
    public static final String POWER_ID = "maniac:PuzzledPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/PuzzledPower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/PuzzledPower.png";
    
    public PuzzledPower(AbstractCreature owner, int turns) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = turns;
        this.type = PowerType.DEBUFF;
        this.loadImg("Puzzled");
        /*this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);*/
        updateDescription();
    }

    @Override
    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_CONFUSION", 0.05F);
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        int modifier = AbstractDungeon.cardRandomRng.random(0, 1);
        if (card.cost >= 1) {
            if (modifier == 0) {
                card.cost += 1;
            } else {
                card.cost -= 1;
            }
            card.costForTurn = card.cost;
            card.isCostModified = true;
            card.freeToPlayOnce = false;
        }
        else if (card.cost == 0) {
            if (modifier == 0) {
                card.cost += 1;
            }
            card.costForTurn = card.cost;
            card.isCostModified = true;
            card.freeToPlayOnce = false;
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.amount == 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new PuzzledPower(owner, amount);
    }
}
