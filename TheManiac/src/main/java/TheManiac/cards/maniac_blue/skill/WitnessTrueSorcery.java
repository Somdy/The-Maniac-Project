package TheManiac.cards.maniac_blue.skill;

import TheManiac.actions.WitnessTrueSorceryAction;
import TheManiac.actions.WitnessTrueSorceryReduceAction;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.stances.LimboStance;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

public class WitnessTrueSorcery extends AbstractManiacCard {
    public static final String ID = "maniac:WitnessTrueSorcery";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/witness_true_sorcery.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 1;
    private static final int cardChoice = 3;
    private static final int additionalChoice = 2;
    private static final int obtain = 1;
    private List<TooltipInfo> tips;

    public WitnessTrueSorcery() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = obtain;
        this.maniacExtraMagicNumber = this.maniacBaseExtraMagicNumber = cardChoice;
        this.maniacOtherMagicNumber = this.maniacBaseOtherMagicNumber = additionalChoice;
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[2], EXTENDED_DESCRIPTION[3]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.stance.ID.equals(LimboStance.STANCE_ID)) {
            this.maniacExtraMagicNumber += this.maniacOtherMagicNumber;
        }
        AbstractDungeon.actionManager.addToBottom(new WitnessTrueSorceryAction(this.magicNumber, this.maniacExtraMagicNumber, this.upgraded));
        
        if (enchanted) {
            if (this.enchantment == 1) {
                this.addToBot(new WitnessTrueSorceryAction(this.magicNumber, this.maniacExtraMagicNumber, this.upgraded));
            } else {
                this.addToBot(new ExhaustAction(1, false, false, false));
                this.addToBot(new DrawCardAction(p, 1, false));
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
            switch (this.enchantOpts(1, 2)) {
                case 1:
                    this.rawDescription += EXTENDED_DESCRIPTION[0];
                    break;
                default:
                    this.rawDescription += EXTENDED_DESCRIPTION[1];
            }
            System.out.println(this.name + "gets enchantment opt: " + this.enchantment);
        }
        this.enchantName();
        this.modifyEnchants(0);
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
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WitnessTrueSorcery();
    }
}
