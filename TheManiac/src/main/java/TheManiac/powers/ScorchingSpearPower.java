package TheManiac.powers;

import TheManiac.actions.ScorchingSpearDamageAction;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ScorchingSpearPower extends AbstractPower implements CloneablePowerInterface {
    private static final Logger logger = LogManager.getLogger(DualMachetesPower.class.getName());
    public static final String POWER_ID = "maniac:ScorchingSpearPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/ScorchingSpearPower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/ScorchingSpearPower.png";
    private int powers;
    private boolean applyDebuffs;
    private boolean allowed = true;
    
    public ScorchingSpearPower(int amount, int powers, boolean applyDebuffs) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = AbstractDungeon.player;
        this.amount = amount;
        this.powers = powers;
        this.applyDebuffs = applyDebuffs;
        this.type = PowerType.BUFF;
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount > 0 && target != null && info.type == DamageInfo.DamageType.NORMAL) {
            AbstractDungeon.actionManager.addToBottom(new ScorchingSpearDamageAction(this.amount, this.powers, this.applyDebuffs));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        if (applyDebuffs) {
            this.description += DESCRIPTIONS[2] + this.powers + DESCRIPTIONS[3];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new ScorchingSpearPower(amount, powers, applyDebuffs);
    }
}
