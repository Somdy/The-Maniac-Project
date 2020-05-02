package TheManiac.powers;

import TheManiac.TheManiac;
import TheManiac.cards.status.TheUndigested;
import TheManiac.monsters.enemies.Mawelling;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DigestPower extends AbstractPower implements CloneablePowerInterface {
    public static final Logger logger = LogManager.getLogger(DigestPower.class.getName());
    public static final String POWER_ID = "maniac:DigestPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/DigestPower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/DigestPower.png";
    
    public DigestPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card.cardID.equals(TheUndigested.ID)) {
            this.amount++;
            updateDescription();
        }
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power.ID.equals(SwellingPower.POWER_ID) && target == this.owner) {
            if (this.owner instanceof Mawelling) {
                ((Mawelling) this.owner).totalSwelling += power.amount;
                logger.info("Current total Swelling Mawelling gained is " + ((Mawelling) this.owner).totalSwelling);
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.owner.name + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new DigestPower(owner, amount);
    }
}
