package TheManiac.cards.maniac_blue.weapon;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.ScorchingSpearPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class ScorchingSpear extends AbstractManiacCard {
    public static final String ID = "maniac:ScorchingSpear";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/weapon/scorching_spear.png";
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 0;
    private static final int baseDmg = 1;
    private static final int powers = 0;
    
    public ScorchingSpear() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = baseDmg;
        this.maniacExtraMagicNumber = this.maniacBaseExtraMagicNumber = powers;
    }
    
    public void setSpearValues(int amount, int powers, boolean applyDebuff) {
        this.magicNumber = this.baseMagicNumber += amount;
        this.maniacExtraMagicNumber = this.maniacBaseExtraMagicNumber += powers;
        this.applyAdditionalPowers = applyDebuff;
        
        this.rawDescription = applyDebuff ? EXTENDED_DESCRIPTION[1] : EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(ScorchingSpearPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, ScorchingSpearPower.POWER_ID));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ScorchingSpearPower(this.magicNumber, this.maniacExtraMagicNumber, this.applyAdditionalPowers)));
    }

    @Override
    public void enchant() {
        
    }

    @Override
    public void upgrade() {
    }

    @Override
    public AbstractCard makeCopy() {
        return new ScorchingSpear();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();
        card.baseMagicNumber = this.baseMagicNumber;
        card.magicNumber = this.magicNumber;
        if (card instanceof AbstractManiacCard) {
            ((AbstractManiacCard) card).maniacBaseExtraMagicNumber = this.maniacBaseExtraMagicNumber;
            ((AbstractManiacCard) card).maniacExtraMagicNumber = this.maniacExtraMagicNumber;
            ((AbstractManiacCard) card).applyAdditionalPowers = this.applyAdditionalPowers;
        }
        card.description = (ArrayList)this.description.clone();
        return card;
    }
}
