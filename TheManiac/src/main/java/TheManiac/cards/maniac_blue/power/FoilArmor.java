package TheManiac.cards.maniac_blue.power;

import TheManiac.TheManiac;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.FoilsPower;
import TheManiac.stances.LimboStance;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

import java.util.ArrayList;
import java.util.List;

public class FoilArmor extends AbstractManiacCard {
    public static final String ID = "maniac:FoilArmor";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/power/foil_armor.png";
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 2;
    private static final int FOILS_ARMOR = 17;
    private static final int METAL = 3;
    private static final int UPGRADE_AMT = 3;
    private List<TooltipInfo> tips;

    public FoilArmor() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = FOILS_ARMOR;
        this.maniacExtraMagicNumber = this.maniacBaseExtraMagicNumber = METAL;
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[4], EXTENDED_DESCRIPTION[5]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new FoilsPower(p, this.magicNumber), this.magicNumber));
        if (isInLimbo()) {
            this.addToBot(new ApplyPowerAction(p, p, new MetallicizePower(p, this.maniacExtraMagicNumber), this.maniacExtraMagicNumber));
        }
        
        if (enchanted) {
            if (this.enchantment == 1) {
                if (p.currentHealth < p.maxHealth / 2) {
                    this.addToBot(new GainBlockAction(p, p, this.enchantNumber));
                }
            }
            else if (this.enchantment == 2) {
                this.addToBot(new ApplyPowerAction(p, p, new FoilsPower(p, this.enchantNumber), this.enchantNumber));
            }
            else if (this.enchantment == 3) {
                this.addToBot(new ApplyPowerAction(p, p, new MetallicizePower(p, this.enchantNumber), this.enchantNumber));
            } else {
                if (isInLimbo()) {
                    this.addToBot(new ChangeStanceAction("Neutral"));
                    this.addToBot(new GainBlockAction(p, p, this.enchantNumber));
                }
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
            switch (this.enchantOpts(1, 4)) {
                case 1:
                    this.rawDescription += EXTENDED_DESCRIPTION[0];
                    break;
                case 2:
                    this.rawDescription += EXTENDED_DESCRIPTION[1];
                    break;
                case 3:
                    this.rawDescription += EXTENDED_DESCRIPTION[2];
                    break;
                default:
                    this.rawDescription += EXTENDED_DESCRIPTION[3];
            }
            System.out.println(this.name + "gets enchantment opt: " + this.enchantment);
        }
        this.enchantName();
        if (this.enchantment == 1) {
            this.modifyEnchants(5);
        }
        else if (this.enchantment == 2) {
            this.modifyEnchants(2);
        }
        else if (this.enchantment == 3) {
            this.modifyEnchants(1);
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
            if (TheManiac.leisureMode) {
                this.upgradeMagicNumber(UPGRADE_AMT + 3);
                this.upgradeManiacExtraMagicNumber(UPGRADE_AMT + 2);
            }
            else if (TheManiac.challengerMode) {
                this.upgradeMagicNumber(UPGRADE_AMT);
                this.upgradeManiacExtraMagicNumber(UPGRADE_AMT);
            }
            else {
                this.upgradeMagicNumber(UPGRADE_AMT + 2);
                this.upgradeManiacExtraMagicNumber(UPGRADE_AMT + 1);
            }
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FoilArmor();
    }
}
