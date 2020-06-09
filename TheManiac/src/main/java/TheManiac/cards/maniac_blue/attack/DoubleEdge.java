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
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.ArrayList;
import java.util.List;

public class DoubleEdge extends AbstractManiacCard {
    public static final String ID = "maniac:DoubleEdge";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/attack/double_edge.png";
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final int COST = 1;
    private static final int Dmg_Deal = 14;
    private static final int Dmg_Feedback = 14;
    private static final int Upgrade_Dmg = 4;
    private List<TooltipInfo> tips;
    
    public DoubleEdge() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = Dmg_Deal;
        this.magicNumber = this.baseMagicNumber = Dmg_Feedback;
        this.maniacExtraMagicNumber = this.maniacBaseExtraMagicNumber = 4;
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[3], EXTENDED_DESCRIPTION[4]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.stance.ID.equals(LimboStance.STANCE_ID)) {
            this.magicNumber -= this.maniacExtraMagicNumber;
        }
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        this.addToBot(new DamageAction(p, new DamageInfo(p, this.magicNumber, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        
        if (enchanted) {
            if (this.enchantment == 1) {
                this.addToBot(new ChangeStanceAction(new LimboStance()));
                this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.enchantNumber), this.enchantNumber));
                this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, -this.enchantNumber), -this.enchantNumber));
            }
            else if (this.enchantment == 2) {
                this.addToBot(new ApplyPowerAction(m, p, new WeaknessPower(m, this.enchantNumber), this.enchantNumber));
                this.addToBot(new ApplyPowerAction(p, p, new BleedingPower(p, this.enchantNumber), this.enchantNumber));
            } else {
                this.addToBot(new ChangeStanceAction("Neutral"));
                this.addToBot(new ApplyPowerAction(p, p, new PlatedArmorPower(p, this.enchantNumber), this.enchantNumber));
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
            this.modifyEnchants(1);
        }
        else if (this.enchantment == 2) {
            this.modifyEnchants(3);
        } else {
            this.modifyEnchants(4);
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
            this.upgradeDamage(Upgrade_Dmg);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DoubleEdge();
    }
}
