package TheManiac.powers;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.cards.maniac_blue.attack.Strike_Maniac;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DualMachetesPower extends AbstractPower implements CloneablePowerInterface {
    private static final Logger logger = LogManager.getLogger(DualMachetesPower.class.getName());
    public static final String POWER_ID = "maniac:DualMachetesPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/DualMachetesPower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/DualMachetesPower.png";
    private int powers;
    private boolean playDouble;
    private boolean allowed = true;
    private AbstractCard tmp;
    private AbstractCard doubleTmp;
    
    public DualMachetesPower(int amount, int powers, boolean playDouble) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = AbstractDungeon.player;
        this.amount = amount;
        this.powers = powers;
        this.playDouble = playDouble;
        this.type = PowerType.BUFF;
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        allowed = true;
        Strike_Maniac strike_maniac = new Strike_Maniac();
        strike_maniac.setAdditionalValues(this.amount, this.powers);
        tmp = strike_maniac.makeStatEquivalentCopy();
        doubleTmp = strike_maniac.makeStatEquivalentCopy();
        AbstractMonster m = (AbstractMonster)action.target;
        
        if (card.cardID.equals(Strike_Maniac.ID) && card.purgeOnUse) {
            logger.info("Shall stopping playing Strikes");
            allowed = false;
        }
        else if (m == null || m.isDeadOrEscaped()) {
            logger.info("No available target. Shall stopping playing Strikes");
            allowed = false;
        }
        
        if (card.hasTag(AbstractCard.CardTags.STRIKE) && allowed) {
            AbstractDungeon.player.limbo.addToBottom(tmp);
            tmp.current_x = this.owner.hb_x;
            tmp.current_y = this.owner.hb_y;
            tmp.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
            tmp.target_y = Settings.HEIGHT / 2.0F;
            
            tmp.calculateCardDamage(m);
            tmp.purgeOnUse = true;
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, 0, true, true), true);
            if (playDouble) {
                AbstractDungeon.player.limbo.addToBottom(doubleTmp);
                doubleTmp.current_x = this.owner.hb_x;
                doubleTmp.current_y = this.owner.hb_y;
                doubleTmp.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                doubleTmp.target_y = Settings.HEIGHT / 2.0F;

                doubleTmp.calculateCardDamage(m);
                doubleTmp.purgeOnUse = true;
                AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(doubleTmp, m, 0, true, true), true);
            }
        }
    }

    @Override
    public void updateDescription() {
        if (playDouble) {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[3] + this.powers + DESCRIPTIONS[4];
        }
        else {
            this.description = DESCRIPTIONS[0] + this.amount +DESCRIPTIONS[2] + this.powers + DESCRIPTIONS[4];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new DualMachetesPower(amount, powers, playDouble);
    }
}
