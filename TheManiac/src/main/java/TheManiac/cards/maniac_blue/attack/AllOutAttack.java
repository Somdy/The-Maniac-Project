package TheManiac.cards.maniac_blue.attack;

import TheManiac.actions.AlloutAttackAction;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.BleedingPower;
import TheManiac.powers.WeaknessPower;
import TheManiac.stances.LimboStance;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import javafx.scene.control.Tooltip;

import java.util.ArrayList;
import java.util.List;

public class AllOutAttack extends AbstractManiacCard {
    public static final String ID = "maniac:AllOutAttack";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/attack/all_out_attack.png";
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final int COST = 2;
    private static final int DAMAGE = 3;
    private List<TooltipInfo> tips;

    public AllOutAttack() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = DAMAGE;
        this.magicNumber = this.baseMagicNumber = 1;
        this.isMultiDamage = true;
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[3], EXTENDED_DESCRIPTION[4]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.stance.ID.equals(LimboStance.STANCE_ID)) {
            for (int i = 0; i < this.multiDamage.length; i++)
            this.multiDamage[i] += this.magicNumber;
        }
        this.addToBot(new AlloutAttackAction(this.multiDamage));
        
        if (enchanted) {
            if (this.enchantment == 1) {
                for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                    if (!monster.isDeadOrEscaped()) {
                        this.addToBot(new ApplyPowerAction(monster, p, new WeaknessPower(monster, this.enchantNumber), this.enchantNumber));
                    }
                }
            }
            else if (this.enchantment == 2) {
                for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                    if (monster.hasPower(VulnerablePower.POWER_ID)) {
                        this.addToBot(new ApplyPowerAction(monster, p, new BleedingPower(monster, this.enchantNumber), this.enchantNumber));
                    }
                }
            } else {
                if (AbstractDungeon.getMonsters().monsters.size() >= 2) {
                    this.addToBot(new DrawCardAction(p, this.enchantNumber, false));
                }
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
            this.modifyEnchants(3);
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
            this.upgradeDamage(1);
            this.upgradeMagicNumber(1);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new AllOutAttack();
    }
}
