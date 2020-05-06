package TheManiac.cards.curses;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

import java.util.ArrayList;

public class Scruple extends AbstractManiacCard {
    public static final String ID = "maniac:Scruple";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/curse/scruple.png";
    private static final CardType TYPE = CardType.CURSE;
    public static final CardColor COLOR = CardColor.CURSE;
    private static final CardRarity RARITY = CardRarity.CURSE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final int COST = -2;
    
    public Scruple() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void triggerWhenDrawn() {
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                Scruple scruple = new Scruple();
                scruple.purgeOnUse = true;
                scruple.isEthereal = true;
                scruple.exhaust = true;
                this.addToBot(new MakeTempCardInDrawPileAction(scruple.makeStatEquivalentCopy(), 1, true, true));
                this.isDone = true;
            }
        });
    }

    @Override
    public void enchant() {
        
    }

    @Override
    public AbstractCard makeCopy() {
        return new Scruple();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();
        card.purgeOnUse = this.purgeOnUse;
        card.isEthereal = this.isEthereal;
        card.exhaust = this.exhaust;
        card.description = (ArrayList) this.description.clone();
        return card;
    }
}
