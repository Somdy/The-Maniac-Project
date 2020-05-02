package TheManiac.powers;

import TheManiac.cards.colorless.attack.Blades;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BladeArtistPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "maniac:BladeArtistPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/BladeArtistPower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/BladeArtistPower.png";
    
    public BladeArtistPower(int times) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = AbstractDungeon.player;
        this.amount = times;
        this.type = PowerType.BUFF;
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);
        this.updateBlades();
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        this.updateBlades();
    }
    
    private void updateBlades() {
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card instanceof Blades) {
                ((Blades) card).updateUses(this.amount);
            }
        }
        
        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
            if (card instanceof Blades) {
                ((Blades) card).updateUses(this.amount);
            }
        }
        
        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if (card instanceof Blades) {
                ((Blades) card).updateUses(this.amount);
            }
        }
        
        for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
            if (card instanceof Blades) {
                ((Blades) card).updateUses(this.amount);
            }
        }
    }

    @Override
    public void onDrawOrDiscard() {
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card instanceof Blades) {
                ((Blades) card).updateUses(this.amount);
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new BladeArtistPower(amount);
    }
}
