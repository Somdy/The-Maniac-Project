package TheManiac.powers;

import TheManiac.relics.BrokenHorn;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class GremlinLeaderUpgradePower extends AbstractManiacPower implements CloneablePowerInterface {
    public static final String POWER_ID = "maniac:GremlinLeaderUpgradePower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/GremlinLeaderPower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/GremlinLeaderPower.png";
    private int powers = 0;
    
    public GremlinLeaderUpgradePower() {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = AbstractDungeon.player;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.loadImg("GremlinLeader");
        /*this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);*/
        updateDescription();
    }

    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        try {
            AbstractRelic relic = AbstractDungeon.player.getRelic(BrokenHorn.ID);
            if (relic.counter > 10) {
                powers = relic.counter / 10;
            } else {
                powers = 1;
            }
        } catch (Exception e) {
            System.out.println("Not accessible to Horn to increase damage. Report this if you see it.");
            e.printStackTrace();
        }
        
        if (type == DamageInfo.DamageType.NORMAL && powers > 0) {
            damage += powers;
            //System.out.println("Increase damage by: " + powers);
        }
        
        return damage;
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType damageType) {
        try {
            AbstractRelic relic = AbstractDungeon.player.getRelic(BrokenHorn.ID);
            if (relic.counter > 6) {
                powers = relic.counter / 6;
            } else {
                powers = 1;
            }
        } catch (Exception e) {
            System.out.println("Not accessible to Horn to reduce damage. Report this if you see it.");
            e.printStackTrace();
        }
        
        if (damageType == DamageInfo.DamageType.NORMAL && powers > 0) {
            damage = damage * ( 1 - powers * 0.02F);
            //System.out.println("Decrease damage to: " + damage);
        }
        
        return damage;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new GremlinLeaderUpgradePower();
    }
}
