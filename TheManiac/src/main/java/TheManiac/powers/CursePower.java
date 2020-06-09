package TheManiac.powers;

import TheManiac.cards.curses.Guilty;
import TheManiac.cards.curses.Humiliation;
import TheManiac.cards.curses.Remorse;
import TheManiac.cards.curses.Scruple;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

public class CursePower extends AbstractManiacPower implements CloneablePowerInterface {
    public static final String POWER_ID = "maniac:CursePower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String IMG_PATH_LARGE = "maniacMod/images/powers/CursePower_large.png";
    private static final String IMG_PATH = "maniacMod/images/powers/CursePower.png";
    
    public CursePower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.DEBUFF;
        this.loadImg("Curse");
        /*this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH_LARGE), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_PATH), 0, 0, 32, 32);*/
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type != AbstractCard.CardType.SKILL) {
            this.flash();
            
            AbstractCard c = SpecificCurses().getRandomCard(true);
            this.addToBot(new MakeTempCardInDrawPileAction(c, 1, true, true));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    private CardGroup SpecificCurses() {
        CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        
        tmpGroup.addToTop(new Guilty());
        tmpGroup.addToTop(new Humiliation());
        tmpGroup.addToTop(new Remorse());
        tmpGroup.addToTop(new Scruple());
        
        return tmpGroup;
    }

    @Override
    public AbstractPower makeCopy() {
        return new CursePower(owner);
    }
}
