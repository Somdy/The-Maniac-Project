package TheManiac.cards.the_possessed.uncertainties;

import TheManiac.TheManiac;
import TheManiac.powers.PlaguePower;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class MysterySeasoning extends AbstractUncertaintiesCard {
    public static final String ID = TheManiac.makeID("MysterySeasoning");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/uncertainties/skill/mystery_seasoning.png";
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final int COST = 1;
    private static final TooltipInfo INFO = new TooltipInfo(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[1]);
    
    public MysterySeasoning() {
        super(ID, IMG_PATH, COST, TYPE, TARGET, INFO);
        this.combatCounter = 0;
        this.counter = 0;
        
        this.magicNumber = this.baseMagicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.counter++;
        
        if (this.combatCounter != 3) {
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                for (int i = 0; i < this.counter; i++) {
                    this.addToBot(new ApplyPowerAction(mo, p, new PlaguePower(mo, p, this.magicNumber), this.magicNumber));
                }
            }
        } else {
            this.combatCounter = 0;
            for (int i = 0; i < this.counter; i++) {
                this.addToBot(new ApplyPowerAction(p, p, new PlaguePower(p, p, this.magicNumber), this.magicNumber));
            }
        }
        
        this.combatCounter++;
    }

    @Override
    public AbstractCard makeCopy() {
        return new MysterySeasoning();
    }
}
