package TheManiac.cards.maniac_blue.attack;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.stances.LimboStance;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class FlyTheKnives extends AbstractManiacCard {
    public static final String ID = "maniac:FlyTheKnives";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/attack/fly_the_knives.png";
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final int COST = 2;
    private static final int baseDmg = 10;
    private static final int increase_Dmg = 2;
    private static final int bonus_Dmg = 2;
    
    public FlyTheKnives() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = baseDmg;
        this.magicNumber = this.baseMagicNumber = increase_Dmg;
        this.maniacExtraMagicNumber = this.maniacBaseExtraMagicNumber = bonus_Dmg;
    }
    
    private static int countCards() {
        int count = 0;
        
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card.type == CardType.ATTACK && card.cost == 0) {
                count ++;
            }
        }
        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
            if (card.type == CardType.ATTACK && card.cost == 0) {
                count ++;
            }
        }
        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if (card.type == CardType.ATTACK && card.cost == 0) {
                count ++;
            }
        }
        for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
            if (card.type == CardType.ATTACK && card.cost == 0) {
                count ++;
            }
        }
        
        return count;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
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
    public void calculateCardDamage(AbstractMonster mo) {
        if (AbstractDungeon.player.stance.ID.equals(LimboStance.STANCE_ID)) {
            this.baseMagicNumber += this.maniacExtraMagicNumber;
        }
        int Dmg = this.baseDamage;
        this.baseDamage += this.baseMagicNumber * countCards();
        super.calculateCardDamage(mo);
        this.baseDamage = Dmg;
        this.isDamageModified = (this.damage != this.baseDamage);
    }

    @Override
    public void applyPowers() {
        if (AbstractDungeon.player.stance.ID.equals(LimboStance.STANCE_ID)) {
            this.baseMagicNumber += this.maniacExtraMagicNumber;
        }
        int Dmg = this.baseDamage;
        this.baseDamage += this.baseMagicNumber * countCards();
        super.applyPowers();
        this.baseDamage = Dmg;
        this.isDamageModified = (this.damage != this.baseDamage);
    }

    @Override
    public void enchant() {
        
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(1);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FlyTheKnives();
    }
}
