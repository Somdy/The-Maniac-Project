package TheManiac.cards.maniac_blue.attack;

import TheManiac.TheManiac;
import TheManiac.actions.TrackAction;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.WeaknessPower;
import TheManiac.stances.LimboStance;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.ArrayList;
import java.util.List;

public class ObscuredAttack extends AbstractManiacCard {
    public static final String ID = "maniac:ObscuredAttack";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/attack/obscured_attack.png";
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final int COST = 2;
    private static final int DAMAGE = 9;
    private static final int TRACK = 2;
    private static final int UPGRADE_AMT = 1;
    private List<TooltipInfo> tips;

    public ObscuredAttack() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = TRACK;
        this.damage = this.baseDamage = DAMAGE;
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[3], EXTENDED_DESCRIPTION[4]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ChangeStanceAction(new LimboStance()));
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        this.addToBot(new TrackAction(this.magicNumber, p.drawPile));
        
        if (enchanted) {
            if (this.enchantment == 2) {
                if (m.hasPower(WeaknessPower.POWER_ID)) {
                    this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, this.enchantNumber, false), this.enchantNumber));
                }
            }
            else if (this.enchantment == 3) {
                for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                    if (!monster.isDeadOrEscaped() && monster.hasPower(WeakPower.POWER_ID)) {
                        this.addToBot(new DrawCardAction(p, this.enchantNumber, false));
                    }
                }
            }
        }
    }

    @Override
    public void WhenDetected() {
        if (enchanted) {
            if (this.enchantment == 1) {
                for (int i = 0; i < 2; i++) {
                    this.addToBot(new DamageRandomEnemyAction(new DamageInfo(AbstractDungeon.player, this.enchantNumber, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SMASH));
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
            this.modifyEnchants(3);
        }
        else if (this.enchantment == 2) {
            this.modifyEnchants(2);
        } else {
            this.modifyEnchants(1);
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
            this.upgradeDamage(UPGRADE_AMT);
            this.upgradeMagicNumber(UPGRADE_AMT);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ObscuredAttack();
    }
}
