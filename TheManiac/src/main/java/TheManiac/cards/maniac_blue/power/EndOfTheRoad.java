package TheManiac.cards.maniac_blue.power;

import TheManiac.cards.colorless.attack.DeusExMe;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.BleedingPower;
import TheManiac.powers.DeadEndPower;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

import java.util.ArrayList;
import java.util.List;

public class EndOfTheRoad extends AbstractManiacCard {
    public static final String ID = "maniac:EndOfTheRoad";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/power/end_of_the_road.png";
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 2;
    private static final int powersToApply = 8;
    private List<TooltipInfo> tips;

    public EndOfTheRoad() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = powersToApply;
        this.cardsToPreview = new DeusExMe();
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[3], EXTENDED_DESCRIPTION[4]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DeadEndPower(1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new BleedingPower(p, this.magicNumber), this.magicNumber));
        
        if (enchanted) {
            if (this.enchantment == 1) {
                for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                    if (monster.getPower(BleedingPower.POWER_ID) != null) {
                        this.addToBot(new HealAction(p, p, this.enchantNumber, 0.05F));
                    }
                }
            }
            else if (this.enchantment == 2) {
                if (AbstractDungeon.getMonsters().monsters.size() >= 3) {
                    this.addToBot(new ApplyPowerAction(p, p, new PlatedArmorPower(p, this.enchantNumber), this.enchantNumber));
                }
            }
        }
    }

    @Override
    public void triggerOnTrack() {
        if (enchanted) {
            if (this.enchantment == 3) {
                if (AbstractDungeon.player.powers != null) {
                    for (AbstractPower power : AbstractDungeon.player.powers) {
                        if (power.type == AbstractPower.PowerType.DEBUFF) {
                            this.addToBot(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, power, this.enchantNumber));
                            break;
                        }
                    }
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
            this.modifyEnchants(2);
        }
        else if (this.enchantment == 2) {
            this.modifyEnchants(6);
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
            this.upgradeBaseCost(1);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new EndOfTheRoad();
    }
}
