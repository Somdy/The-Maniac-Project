package TheManiac.cards.the_possessed.risks;

import TheManiac.TheManiac;
import TheManiac.actions.ThePossessedAction.GlacialMysteriesAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawReductionPower;

public class GlacialMysteries extends AbstractRisksCard {
    public static final String ID = TheManiac.makeID("GlacialMysteries");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/risks/skill/glacial_mysteries.png";
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final int COST = 3;
    
    public GlacialMysteries() {
        super(ID, IMG_PATH, COST, TYPE, TARGET);
        this.isInAutoplay = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);
        this.addToBot(new GlacialMysteriesAction());
    }

    @Override
    public void thrill(AbstractPlayer p, AbstractMonster m, boolean onUse) {
        if (onUse)
            this.addToBot(new ApplyPowerAction(p, p, new DrawReductionPower(p, 1), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new GlacialMysteries();
    }
}
