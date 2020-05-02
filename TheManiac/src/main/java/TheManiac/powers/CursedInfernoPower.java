package TheManiac.powers;

import TheManiac.actions.CursedInfernoAction;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class CursedInfernoPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "maniac:CursedInfernoPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/CursedInfernoPower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/CursedInfernoPower.png";
    private int powers;

    public CursedInfernoPower(int amount, int powers) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = AbstractDungeon.player;
        this.amount = amount;
        this.powers = powers;
        this.type = PowerType.BUFF;
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.SKILL) {
            this.flash();
            if (this.powers > 0) {
                AbstractDungeon.actionManager.addToBottom(new CursedInfernoAction(this.amount, this.powers));
            }
            else {
                AbstractDungeon.actionManager.addToBottom(new CursedInfernoAction(this.amount));
            }
        }
    }

    @Override
    public void updateDescription() {
        if (this.powers > 0) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2] + this.powers + DESCRIPTIONS[3];
        }
        else {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new CursedInfernoPower(amount, powers);
    }
}
