package TheManiac.cards.maniac_blue.attack;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.BleedingPower;
import TheManiac.powers.WeaknessPower;
import TheManiac.stances.LimboStance;
import basemod.helpers.TooltipInfo;
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
import javafx.scene.control.Tooltip;

import java.util.ArrayList;
import java.util.List;

public class GlassScratch extends AbstractManiacCard {
    public static final String ID = "maniac:GlassScratch";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/attack/glass_scratch.png";
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final int COST = 1;
    private static final int DAMAGE = 3;
    private static final int POWERSTOAPPLY = 3;
    private static final int ADDITIONALPOWERS = 2;
    private List<TooltipInfo> tips;

    public GlassScratch() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = DAMAGE;
        this.magicNumber = this.baseMagicNumber = POWERSTOAPPLY ;
        this.maniacBaseExtraMagicNumber = this.maniacExtraMagicNumber = ADDITIONALPOWERS;
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[3], EXTENDED_DESCRIPTION[4]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.stance.ID.equals(LimboStance.STANCE_ID)) {
            this.magicNumber += this.maniacExtraMagicNumber;
        }
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        this.addToBot(new ApplyPowerAction(m, p, new BleedingPower(m, this.magicNumber), this.magicNumber));
        this.addToBot(new ApplyPowerAction(m, p, new BleedingPower(m, this.magicNumber), this.magicNumber));
        
        if (enchanted) {
            if (this.enchantment == 1) {
                this.addToBot(new ApplyPowerAction(m, p, new BleedingPower(m, this.enchantNumber), this.enchantNumber));
            }
            else if (this.enchantment == 2) {
                if (m.currentHealth < m.maxHealth / 2) {
                    this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                }
            } else {
                this.addToBot(new ApplyPowerAction(m, p, new WeaknessPower(m, this.enchantNumber), this.enchantNumber));
            }
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
            this.modifyEnchants(2);
        } 
        else if (this.enchantment == 2) {
            this.modifyEnchants(0);
        } else {
            this.modifyEnchants(3);
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
        return new GlassScratch();
    }
}
