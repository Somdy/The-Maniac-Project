package TheManiac.cards.the_possessed.risks;

import TheManiac.TheManiac;
import TheManiac.actions.ThePossessedAction.RiskUniversalThrillActions;
import TheManiac.actions.ThePossessedAction.UnwillingSacrificeAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class UnwillingSacrifice extends AbstractRisksCard {
    public static final String ID = TheManiac.makeID("UnwillingSacrifice");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/risks/skill/unwilling_sacrifice.png";
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final int COST = 1;
    
    public UnwillingSacrifice() {
        super(ID, IMG_PATH, COST, TYPE, TARGET);
        this.magicNumber = this.baseMagicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);
        this.addToBot(new UnwillingSacrificeAction(this.magicNumber));
    }

    @Override
    public void thrill(AbstractPlayer p, AbstractMonster m, boolean onUse) {
        if (!onUse)
            this.addToBot(new RiskUniversalThrillActions(p, m, AbstractDungeon.cardRandomRng.random(89), 4, 4, 4, CardTarget.ALL));
    }

    @Override
    public AbstractCard makeCopy() {
        return new UnwillingSacrifice();
    }
}