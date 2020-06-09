package TheManiac.cards.the_possessed.shinies;

import TheManiac.TheManiac;
import TheManiac.relics.PossessedManuscripts;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class WitchHour extends AbstractShiniesCard {
    public static final String ID = TheManiac.makeID("WitchHour");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/shinies/skill/witch_hour.png";
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 2;
    
    public WitchHour() {
        super(ID, IMG_PATH, COST, TYPE, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.getRelic(PossessedManuscripts.ID) != null) {
            for (AbstractRelic relic : p.relics) {
                if (relic instanceof PossessedManuscripts) {
                    relic.flash();
                    ((PossessedManuscripts) relic).modifyActiveAmount(2, 1D);
                    ((PossessedManuscripts) relic).activeEffects.set(2, true);
                    ((PossessedManuscripts) relic).updateEffectDescription();
                }
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WitchHour();
    }
}
