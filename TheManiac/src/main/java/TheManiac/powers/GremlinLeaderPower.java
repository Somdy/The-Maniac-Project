package TheManiac.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class GremlinLeaderPower extends AbstractManiacPower implements CloneablePowerInterface {
    public static final String POWER_ID = "maniac:GremlinLeaderPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/GremlinLeaderPower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/GremlinLeaderPower.png";
    private boolean saveLife = true;
    
    public GremlinLeaderPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.loadImg("GremlinLeader");
        /*this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);*/
        updateDescription();
    }

    @Override
    public void onDeath() {
        if (saveLife) {
            int restoreHP = this.owner.maxHealth / 4;
            int chance = AbstractDungeon.miscRng.random(0, 5);
            if (AbstractDungeon.miscRng.random(10) <= chance) {
                this.flash();
                AbstractDungeon.player.heal(restoreHP, true);
            }
            saveLife = false;
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new GremlinLeaderPower(owner);
    }
}
