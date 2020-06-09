package TheManiac.cards.maniac_blue.attack;

import TheManiac.actions.SpectralFlashAction;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.FrightenedPower;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;

import java.util.ArrayList;
import java.util.List;

public class SpectralFlash extends AbstractManiacCard {
    public static final String ID = "maniac:SpectralFlash";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/attack/spectral_flash.png";
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final int COST = 1;
    private static final int Dmg = 3;
    private static final int Dmg_Increased = 1;
    private List<TooltipInfo> tips;
    
    public SpectralFlash() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = Dmg;
        this.magicNumber = this.baseMagicNumber = Dmg_Increased;
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[3], EXTENDED_DESCRIPTION[4]));
    }
    
    public void setAdditionalDmg(int amount) {
        this.damage = this.baseDamage = amount;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new SpectralFlashAction(this.damage, this.baseDamage, this.magicNumber, p, m));
        if (enchanted) {
            if (this.enchantment == 1) {
                this.addToBot(new ApplyPowerAction(m, p, new FrightenedPower(m, this.enchantNumber), this.enchantNumber));
            }
            else if (this.enchantment == 2) {
                this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, this.enchantNumber), this.enchantNumber));
                this.addToBot(new ApplyPowerAction(p, p, new LoseDexterityPower(p, this.enchantNumber), this.enchantNumber));
            }
        }
    }

    @Override
    public void WhenDetected() {
        if (enchanted && this.enchantment == 3) {
            this.addToBot(new DamageRandomEnemyAction(new DamageInfo(AbstractDungeon.player, this.enchantNumber), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
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
        if (!enchanted) {
            switch (this.enchantOpts(1, 3)) {
                case 1:
                    this.rawDescription += EXTENDED_DESCRIPTION[0];
                    break;
                case 2:
                    this.rawDescription += EXTENDED_DESCRIPTION[1];
                    break;
                default:
                    this.rawDescription += EXTENDED_DESCRIPTION[2];
            }
            System.out.println(this.name + "gets enchantment opt: " + this.enchantment);
        }
        this.enchantName();
        if (this.enchantment == 1) {
            this.modifyEnchants(3);
        }
        else if (this.enchantment == 2) {
            this.modifyEnchants(1);
        } else {
            this.modifyEnchants(2);
        }
        initializeDescription();
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        return this.tips;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(0);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SpectralFlash();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();
        card.baseMagicNumber = this.baseMagicNumber;
        card.magicNumber = this.magicNumber;
        card.baseDamage = this.baseDamage;
        card.damage = this.damage;
        if (card instanceof AbstractManiacCard) {
            ((AbstractManiacCard) card).maniacBaseExtraMagicNumber = this.maniacBaseExtraMagicNumber;
            ((AbstractManiacCard) card).maniacExtraMagicNumber = this.maniacExtraMagicNumber;
        }
        card.description = (ArrayList)this.description.clone();
        return card;
    }
}
