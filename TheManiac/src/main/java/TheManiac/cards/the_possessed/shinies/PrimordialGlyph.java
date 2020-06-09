package TheManiac.cards.the_possessed.shinies;

import TheManiac.TheManiac;
import TheManiac.actions.ThePossessedAction.GlyphAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PrimordialGlyph extends AbstractShiniesCard {
    public static final String ID = TheManiac.makeID("PrimordialGlyph");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/shinies/skill/primordial_glyph.png";
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 2;
    
    public PrimordialGlyph() {
        super(ID, IMG_PATH, COST, TYPE, TARGET);
        this.magicNumber = this.baseMagicNumber = 6;
        this.maniacExtraMagicNumber = this.maniacBaseExtraMagicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GlyphAction(this.magicNumber, this.maniacExtraMagicNumber, false));
    }

    @Override
    public AbstractCard makeCopy() {
        return new PrimordialGlyph();
    }
}
