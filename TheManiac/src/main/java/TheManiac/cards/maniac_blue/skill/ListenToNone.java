package TheManiac.cards.maniac_blue.skill;

import TheManiac.actions.ListenToNoneAction;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.ArrayList;
import java.util.List;

public class ListenToNone extends AbstractManiacCard {
    public static final String ID = "maniac:ListenToNone";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/listen_to_none.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 2;
    private static final int BLOCK = 10;
    private static final int UPGRADE_BLOCK = 4;
    private static final int EXTRA_BLOCK = 5;
    private List<TooltipInfo> tips;

    public ListenToNone() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.block = this.baseBlock = BLOCK;
        this.maniacExtraMagicNumber = this.maniacBaseExtraMagicNumber = EXTRA_BLOCK;
        this.enchantNumber = this.baseEnchantNumber = 0;
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[3], EXTENDED_DESCRIPTION[4]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ListenToNoneAction(this.block, this.maniacExtraMagicNumber, CardType.ATTACK));
        
        if (enchanted) {
            if (this.enchantment == 1) {
                this.addToBot(new GainBlockAction(p, p, this.enchantNumber));
            }
            else if (this.enchantment == 2) {
                if (p.currentHealth > p.maxHealth / 2) {
                    this.addToBot(new ApplyPowerAction(p, p, new MetallicizePower(p, this.enchantNumber), this.enchantNumber));
                }
            } else {
                if (p.getPower(WeakPower.POWER_ID) != null) {
                    this.addToBot(new RemoveSpecificPowerAction(p, p, WeakPower.POWER_ID));
                }
            }
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isInLimbo() && noAttacksCheck()) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
        else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    private boolean noAttacksCheck() {
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card.type != CardType.ATTACK) {
                return true;
            }
        }
        return false;
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
        switch (this.enchantment) {
            case 1:
                this.modifyEnchants(5);
                break;
            case 2:
                this.modifyEnchants(3);
                break;
            default:
                this.modifyEnchants(0);
        }
        this.enchantName();
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
            this.upgradeBlock(UPGRADE_BLOCK);
            this.upgradeManiacExtraMagicNumber(UPGRADE_BLOCK);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ListenToNone();
    }
}
