package TheManiac.cards.status;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.monsters.enemies.Mawelling;
import TheManiac.powers.SwellingPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class TheUndigested extends AbstractManiacCard {
    public static final String ID = "maniac:TheUndigested";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/status/the_undigested.png";
    private static final CardType TYPE = CardType.STATUS;
    public static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final int COST = 1;
    
    public TheUndigested() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 2;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster mo : (AbstractDungeon.getMonsters()).monsters) {
            if (mo instanceof Mawelling && !mo.isDying) {
                if (mo.getPower(SwellingPower.POWER_ID) != null) {
                    (mo.getPower(SwellingPower.POWER_ID)).amount += this.magicNumber;
                    (mo.getPower(SwellingPower.POWER_ID)).updateDescription();
                } else {
                    this.addToBot(new ApplyPowerAction(mo, mo, new SwellingPower(mo, this.magicNumber), this.magicNumber));
                }
                this.addToBot(new HealAction(mo, p, 4, 0.2F));
            }
        }
    }

    @Override
    public void enchant() {
        
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(0);
            this.upgradeMagicNumber(2);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new TheUndigested();
    }
}
