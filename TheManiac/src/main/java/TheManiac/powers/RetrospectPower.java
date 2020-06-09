package TheManiac.powers;

import TheManiac.actions.DetectAction;
import TheManiac.actions.TrackAction;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class RetrospectPower extends AbstractManiacPower implements CloneablePowerInterface {
    public static final String POWER_ID = "maniac:RetrospectPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/RetrospectPower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/RetrospectPower.png";
    
    public RetrospectPower(int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = AbstractDungeon.player;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.loadImg("Retrospect");
        /*this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);*/
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (this.amount > 0 && !AbstractDungeon.player.exhaustPile.isEmpty()) {
            this.flash();
            this.addToBot(new DetectAction(this.amount, AbstractDungeon.player.exhaustPile));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new RetrospectPower(amount);
    }
}
