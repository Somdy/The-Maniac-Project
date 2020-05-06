package TheManiac.cards.maniac_blue.attack;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.DoomPower;
import TheManiac.powers.FrightenedPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class DoomScytheAttack extends AbstractManiacCard {
    public static final String ID = "maniac:DoomScytheAttack";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/attack/doom_scythe.png";
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final int COST = 1;
    private static final int baseDmg = 2;
    private static final int powers = 0;
    
    public DoomScytheAttack() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = baseDmg;
        this.magicNumber = this.baseMagicNumber = powers;
    }
    
    public void setScytheAttackValues(int amount, int powers, boolean applyDoom) {
        this.damage = this.baseDamage += amount;
        this.magicNumber = this.baseMagicNumber += powers;
        this.applyAdditionalPowers = applyDoom;
        
        this.rawDescription = applyAdditionalPowers ? EXTENDED_DESCRIPTION[1] : EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new FrightenedPower(m, this.magicNumber), this.magicNumber));
        if (applyAdditionalPowers) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new DoomPower(m, this.magicNumber), this.magicNumber));
        }
    }

    @Override
    public void enchant() {
        
    }

    @Override
    public void upgrade() {
    }

    @Override
    public AbstractCard makeCopy() {
        return new DoomScytheAttack();
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
