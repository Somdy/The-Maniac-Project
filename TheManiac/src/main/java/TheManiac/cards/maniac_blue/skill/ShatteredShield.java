package TheManiac.cards.maniac_blue.skill;

import TheManiac.actions.ModifyCostForCombatAction;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.FoilsPower;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ModifyBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.ThornsPower;

import java.util.ArrayList;
import java.util.List;

public class ShatteredShield extends AbstractManiacCard {
    public static final String ID = "maniac:ShatteredShield";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/shattered_shield.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 2;
    private List<TooltipInfo> tips;

    public ShatteredShield() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[3], EXTENDED_DESCRIPTION[4]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int lostHp = p.maxHealth - p.currentHealth;
        this.addToBot(new GainBlockAction(p, lostHp));
        this.addToBot(new ModifyCostForCombatAction(this.uuid, 1));
        
        if (enchanted) {
            if (this.enchantment == 1) {
                if (p.currentHealth < p.maxHealth / 2) {
                    this.addToBot(new ApplyPowerAction(p, p, new FoilsPower(p, this.enchantNumber), this.enchantNumber));
                }
            }
            else if (this.enchantment == 2) {
                if (p.currentHealth > p.maxHealth / 2) {
                    this.addToBot(new ApplyPowerAction(p, p, new MetallicizePower(p, this.enchantNumber), this.enchantNumber));
                }
            } else {
                this.addToBot(new ApplyPowerAction(p, p, new ThornsPower(p, this.enchantNumber), this.enchantNumber));
            }
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
            this.modifyEnchants(4);
        } 
        else if (this.enchantment == 2) {
            this.modifyEnchants(2);
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
            this.upgradeBaseCost(1);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShatteredShield();
    }
}
