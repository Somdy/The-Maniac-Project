package TheManiac.cards.maniac_blue.skill;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.GremlinLeaderUpgradePower;
import TheManiac.powers.SorceryHourPower;
import TheManiac.stances.LimboStance;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.ArrayList;
import java.util.List;

public class SorceryHour extends AbstractManiacCard {
    public static final String ID = "maniac:SorceryHour";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/sorcery_hour.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 3;
    private static final int UPGRADED_COST = 2;
    private List<TooltipInfo> tips;

    public SorceryHour() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
        this.isUnreal = true;
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[3], EXTENDED_DESCRIPTION[4]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ChangeStanceAction(new LimboStance()));
        this.addToBot(new ApplyPowerAction(p, p, new SorceryHourPower(p, 1), 1));
        
        if (enchanted) {
            if (this.enchantment == 1) {
                if (p.drawPile.isEmpty()) {
                    AbstractCard card = this.makeCopy();
                    this.addToBot(new MakeTempCardInDrawPileAction(card, this.enchantNumber, true, true));
                }
            }
            else if (this.enchantment == 2) {
                this.addToBot(new ApplyPowerAction(p, p, new MetallicizePower(p, this.enchantNumber), this.enchantNumber));
            } else {
                boolean modifiedDebuff = false;
                for (AbstractPower power : p.powers) {
                    if (power.type == AbstractPower.PowerType.DEBUFF) {
                        this.addToBot(new ReducePowerAction(p, p, power, this.enchantNumber));
                        modifiedDebuff = true;
                    }
                }
                
                if (modifiedDebuff) {
                    this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, -this.enchantNumber), -this.enchantNumber));
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
            this.modifyEnchants(1);
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
            this.upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SorceryHour();
    }
}
