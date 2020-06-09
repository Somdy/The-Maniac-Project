package TheManiac.cards.maniac_blue.skill;

import TheManiac.actions.TrackAction;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.FoilsPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ArmToTheTeeth extends AbstractManiacCard {
    public static final String ID = "maniac:ArmToTheTeeth";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/arm_to_the_teeth.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 1;
    private static final int UPGRADE_AMOUNT = 2;

    public ArmToTheTeeth() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 6;
        this.maniacExtraMagicNumber = this.maniacBaseExtraMagicNumber = 2;
        this.isEnchanter = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new FoilsPower(p, this.magicNumber), this.magicNumber));
        if (isInLimbo() && p.currentBlock > 0) {
            this.addToBot(new TrackAction(this.maniacExtraMagicNumber, p.discardPile));
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isInLimbo()) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
        else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public void enchant() {
        
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_AMOUNT);
            this.upgradeManiacExtraMagicNumber(UPGRADE_AMOUNT);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ArmToTheTeeth();
    }
}
