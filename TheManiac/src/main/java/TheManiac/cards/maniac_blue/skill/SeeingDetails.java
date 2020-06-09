package TheManiac.cards.maniac_blue.skill;

import TheManiac.TheManiac;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.BleedingPower;
import TheManiac.powers.WeaknessPower;
import TheManiac.stances.LimboStance;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

public class SeeingDetails extends AbstractManiacCard {
    public static final String ID = "maniac:SeeingDetails";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/seeing_details.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final int COST = 1;
    private static final int powersToApply = 5;
    private static final int upgrade_powers = 1;
    private List<TooltipInfo> tips;
    
    public SeeingDetails() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = powersToApply;
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[3], EXTENDED_DESCRIPTION[4]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new WeaknessPower(m, this.magicNumber), this.magicNumber));
        if (isInLimbo()) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new WeaknessPower(m, this.magicNumber), this.magicNumber));
        }
        
        if (enchanted) {
            if (this.enchantment == 1 && noAttacksCheck()) {
                this.addToBot(new ApplyPowerAction(m, p, new WeaknessPower(m, this.enchantNumber), this.enchantNumber));
            }
            else if (this.enchantment == 3) {
                if (m.intent == AbstractMonster.Intent.ATTACK) {
                    this.addToBot(new ApplyPowerAction(m, p, new BleedingPower(m, this.enchantNumber), this.enchantNumber));
                }
            }
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
    public void triggerOnTrack() {
        if (enchanted && this.enchantment == 2) {
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                this.addToBot(new ApplyPowerAction(monster, monster, new WeaknessPower(monster, this.enchantNumber), this.enchantNumber));
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
            if (TheManiac.leisureMode) {
                this.upgradeMagicNumber(upgrade_powers + 4);
            }
            else if (TheManiac.challengerMode) {
                this.upgradeMagicNumber(upgrade_powers);
            }
            else {
                this.upgradeMagicNumber(upgrade_powers + 2);
            }
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SeeingDetails();
    }
}
