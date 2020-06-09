package TheManiac.cards.the_possessed.shinies;

import TheManiac.TheManiac;
import TheManiac.actions.ThePossessedAction.CreationScienceAction;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CreationScience extends AbstractShiniesCard {
    public static final String ID = TheManiac.makeID("CreationScience");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/shinies/skill/creation_science.png";
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 2;
    private static final TooltipInfo INFO = new TooltipInfo(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[1]);
    
    public CreationScience() {
        super(ID, IMG_PATH, COST, TYPE, TARGET, INFO);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new CreationScienceAction());
    }

    @Override
    public AbstractCard makeCopy() {
        return new CreationScience();
    }
}
