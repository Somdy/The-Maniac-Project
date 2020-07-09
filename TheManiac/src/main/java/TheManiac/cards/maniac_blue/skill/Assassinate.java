package TheManiac.cards.maniac_blue.skill;

import TheManiac.TheManiac;
import TheManiac.actions.AssassinateAction;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.UUID;

public class Assassinate extends AbstractManiacCard {
    public static final String ID = TheManiac.makeID("Assassinate");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/assassinate.png";
    private static final String SHROUD_IMG = "maniacMod/images/1024portraits/maniac_blue/skill/conceal.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final int COST = 3;
    
    public Assassinate() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 10;
        this.isShroud = true;
        this.isEthereal = true;
        
        this.cardsToPreview = new Assassinate(null, this.upgraded);
    }

    public Assassinate(boolean upgraded) {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 10;
        this.isShroud = true;
        this.isEthereal = true;

        if (upgraded) {
            this.upgrade();
        }
    }

    public Assassinate(UUID corUUID, boolean upgraded) {
        super(ID, EXTENDED_DESCRIPTION[1], SHROUD_IMG, -2, EXTENDED_DESCRIPTION[0], TYPE, COLOR, RARITY, CardTarget.NONE);
        this.magicNumber = this.baseMagicNumber = 4;
        this.isShroud = true;
        this.shrouded = true;
        this.storedUUID = corUUID;
        
        if (corUUID != null) {
            this.cardsToPreview = new Assassinate(upgraded);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (shrouded) return;
        
        this.addToBot(new AssassinateAction(this.magicNumber));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return !shrouded;
    }

    @Override
    public void triggerWhenDrawn() {
        if (shrouded) {
            AbstractMonster m = AbstractDungeon.getMonsters().getRandomMonster(true);
            if (m != null) {
                this.addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new StrengthPower(m, this.magicNumber), this.magicNumber));
            }
        }
    }

    @Override
    public int onAttackedToModifyDamage(DamageInfo info, int damageAmount, boolean inHand) {
        if (shrouded && info.type == DamageInfo.DamageType.NORMAL && info.owner instanceof AbstractMonster)
            return (int) (damageAmount * 0.35);
        
        return damageAmount;
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
            if (!shrouded) {
                this.upgradeBaseCost(2);
                this.cardsToPreview = new Assassinate(null, true);
            }
            else this.upgradeMagicNumber(-1);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        if (shrouded) return new Assassinate(null, false);
        return new Assassinate();
    }

    @Override
    public AbstractCard makeShroudCopy() {
        AbstractCard card = new Assassinate(this.uuid, this.upgraded);
        
        if (upgraded) card.upgrade();
        
        return card;
    }
}
