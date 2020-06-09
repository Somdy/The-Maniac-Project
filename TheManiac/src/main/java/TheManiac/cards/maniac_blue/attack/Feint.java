package TheManiac.cards.maniac_blue.attack;

import TheManiac.actions.FeintAction;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.WeaknessPower;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.ArrayList;
import java.util.List;

public class Feint extends AbstractManiacCard {
    public static final String ID = "maniac:Feint";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/attack/feint.png";
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final int COST = 1;
    private static final int DAMAGE = 4;
    private static final int powersToGain = 2;
    private List<TooltipInfo> tips;

    public Feint() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = DAMAGE;
        this.magicNumber = this.baseMagicNumber = powersToGain;
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[3], EXTENDED_DESCRIPTION[4]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new TalkAction(true, TheManiacCharacter.charStrings.TEXT[5], 1.0f, 1.5f));
        this.addToBot(new FeintAction(p, this.damage, 1, m));
        
        if (enchanted) {
            if (this.enchantment == 1) {
                if (m.intent == AbstractMonster.Intent.ATTACK) {
                    this.addToBot(new ApplyPowerAction(m, p, new WeaknessPower(m, this.enchantNumber), this.enchantNumber));
                }
            }
            else if (this.enchantment == 2) {
                if (m.intent == AbstractMonster.Intent.DEBUFF || m.intent == AbstractMonster.Intent.STRONG_DEBUFF) {
                    this.addToBot(new ApplyPowerAction(p, p, new ArtifactPower(p, 1), 1));
                }
            } else {
                this.addToBot(new DamageAction(m, new DamageInfo(p, this.enchantNumber, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            }
        }
    }

    @Override
    public void WhenDetected() {
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, this.magicNumber), this.magicNumber));
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
        return new Feint();
    }
}
