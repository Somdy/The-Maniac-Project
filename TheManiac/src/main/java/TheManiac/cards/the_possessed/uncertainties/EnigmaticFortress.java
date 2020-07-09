package TheManiac.cards.the_possessed.uncertainties;

import TheManiac.TheManiac;
import TheManiac.cards.temp.FortressExpedition;
import TheManiac.helper.ManiacUtils;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class EnigmaticFortress extends AbstractUncertaintiesCard {
    public static final String ID = TheManiac.makeID("EnigmaticFortress");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/uncertainties/skill/enigmatic_fortress.png";
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final int COST = 1;
    
    public EnigmaticFortress() {
        super(ID, IMG_PATH, COST, TYPE, TARGET);
        this.purgeOnUse = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> expeditions = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            FortressExpedition c = new FortressExpedition(i + 1);
            expeditions.add(c);
        }

        expeditions = ManiacUtils.randomizeOrder(expeditions);
        
        this.addToBot(new ChooseOneAction(expeditions));
    }

    @Override
    public AbstractCard makeCopy() {
        return new EnigmaticFortress();
    }
}