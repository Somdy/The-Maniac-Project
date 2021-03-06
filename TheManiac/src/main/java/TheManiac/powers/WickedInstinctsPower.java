package TheManiac.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class WickedInstinctsPower extends AbstractManiacPower implements CloneablePowerInterface {
    public static final String POWER_ID = "maniac:WickedInstinctsPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/WickedInstinctsPower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/WickedInstinctsPower.png";

    public WickedInstinctsPower(int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = AbstractDungeon.player;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.loadImg("WickedInstincts");
        /*this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);*/
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        boolean allowed = !AbstractDungeon.player.drawPile.isEmpty();
        
        if (allowed) {
            this.flash();
            if (AbstractDungeon.player.drawPile.getTopCard().type == AbstractCard.CardType.ATTACK) {
                for (int i = 0; i < this.amount; i ++) {
                    AbstractDungeon.actionManager.addToBottom(new PlayTopCardAction
                            (AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng),
                                    false));
                }
            }
            else if (AbstractDungeon.player.drawPile.getTopCard().type == AbstractCard.CardType.SKILL) {
                AbstractDungeon.actionManager.addToBottom(new DrawCardAction(this.owner, this.amount, false));
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + this.amount+ DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new WickedInstinctsPower(amount);
    }
}
