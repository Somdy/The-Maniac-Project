package TheManiac.cards.maniac_blue.skill;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class WardAndDefend extends AbstractManiacCard {
    public static final String ID = "maniac:WardAndDefend";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/ward_and_defend.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 1;
    private static final int block_amt = 5;
    private static final int plated_armor = 2;
    private static final int upgrade_amt = 1;
    
    public WardAndDefend() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.block = this.baseBlock = block_amt;
        this.magicNumber = this.baseMagicNumber = plated_armor;
        this.cardsToPreview = new Defend_Maniac();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, this.block));
        Defend_Maniac defend = new Defend_Maniac();
        defend.setPlatedArmor(this.magicNumber);
        defend.exhaust = true;
        defend.isEthereal = true;
        defend.purgeOnUse = true;
        this.addToBot(new MakeTempCardInHandAction(defend.makeStatEquivalentCopy(), 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeName();
            this.upgradeBlock(upgrade_amt);
            this.upgradeMagicNumber(upgrade_amt);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WardAndDefend();
    }
}
