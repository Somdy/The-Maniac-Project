package TheManiac.cards.the_possessed.shinies;

import TheManiac.TheManiac;
import TheManiac.actions.TrackAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Scavenger extends AbstractShiniesCard {
    public static final String ID = TheManiac.makeID("Scavenger");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/shinies/skill/scavenger.png";
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 1;
    
    public Scavenger() {
        super(ID, IMG_PATH, COST, TYPE, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new TrackAction(-1, p.discardPile));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Scavenger();
    }
}
