package TheManiac.powers;

import TheManiac.vfx.CorruardianOrbsEffect;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CorruardianSelfDestructionPower extends AbstractPower implements CloneablePowerInterface {
    public static final Logger logger = LogManager.getLogger(CorruardianSelfDestructionPower.class.getName());
    public static final String POWER_ID = "maniac:CorruardianSelfDestruction";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/DeadEndPower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/DeadEndPower.png";
    public CorruardianOrbsEffect orbs;
    
    public CorruardianSelfDestructionPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.owner.name + DESCRIPTIONS[1];
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        this.orbs = new CorruardianOrbsEffect(this.owner.drawX, this.owner.drawY);
        this.addToBot(new VFXAction(this.orbs));
    }

    @Override
    public AbstractPower makeCopy() {
        return new CorruardianSelfDestructionPower(owner);
    }
}
