package TheManiac.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

public class ShadowContactPower extends AbstractManiacPower implements CloneablePowerInterface {
    public static final String POWER_ID = "maniac:ShadowContactPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/ShadowContactPower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/ShadowContactPower.png";
    
    public ShadowContactPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.loadImg("ShadowContact");
        /*this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);*/
        updateDescription();
    }
    
    @Override
    public void atStartOfTurnPostDraw() {
        if (!AbstractDungeon.player.drawPile.isEmpty() && this.amount > 0) {
            try {
                for (int i = 0; i < this.amount; i++) {
                    AbstractCard card = AbstractDungeon.player.drawPile.getRandomCard(AbstractDungeon.cardRng);
                    if (card.type != AbstractCard.CardType.CURSE && card.type != AbstractCard.CardType.STATUS) {
                        AbstractCard tmp = card.makeCopy();
                        if (card.upgraded) {
                            tmp.upgrade();
                        }
                        tmp.purgeOnUse = true;
                        tmp.isEthereal = true;
                        if (AbstractDungeon.player.hand.size() < 10) {
                            this.flash();
                            this.addToBot(new MakeTempCardInHandAction(tmp, true, true));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Unable to make copies of cards in draw pile. Shadow Contact catch: " + e);
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount;
        this.description += (this.amount > 1) ? DESCRIPTIONS[2] : DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ShadowContactPower(owner, amount);
    }
}
