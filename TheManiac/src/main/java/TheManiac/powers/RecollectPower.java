package TheManiac.powers;

import TheManiac.actions.RecollectAction;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class RecollectPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "maniac:RecollectPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/RecollectPower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/RecollectPower.png";
    public boolean modifyCosts;

    public RecollectPower(AbstractCreature owner, int amount, boolean modifyCosts) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.modifyCosts = modifyCosts;
        this.type = PowerType.BUFF;
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        if (this.amount > 0 && AbstractDungeon.player.exhaustPile.size() > 0) {
            AbstractDungeon.actionManager.addToBottom(new RecollectAction(this.owner, this.amount, this.modifyCosts));
        }
    }

    @Override
    public void updateDescription() {
        if (this.amount > 1) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
        }
        else {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new RecollectPower(owner, amount, modifyCosts);
    }
}
