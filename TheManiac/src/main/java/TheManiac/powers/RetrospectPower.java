package TheManiac.powers;

import TheManiac.actions.TrackAction;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class RetrospectPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "maniac:RetrospectPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/RetrospectPower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/RetrospectPower.png";
    private int cardsToTrack;
    private int cardsRequire;
    
    public RetrospectPower(int numCards, int cardsToPlay, int cardsRequire) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = AbstractDungeon.player;
        this.amount = cardsToPlay;
        this.cardsRequire = cardsRequire;
        this.cardsToTrack = numCards;
        this.type = PowerType.BUFF;
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void onExhaust(AbstractCard card) {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            flashWithoutSound();
            if (this.amount == 0) {
                try {
                    AbstractDungeon.actionManager.addToBottom(new TrackAction(this.cardsToTrack, AbstractDungeon.player.exhaustPile));
                } catch (Exception e) {
                    System.out.println("Unable to Track exhaust pile by Retrospect. Report this if you see it." + e);
                    e.printStackTrace();
                }
                this.cardsToTrack++;
                this.amount = this.cardsRequire;
            }
            else {
                this.amount--;
            }
        }
        
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + this.cardsToTrack + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new RetrospectPower(cardsToTrack, amount, cardsRequire);
    }
}
