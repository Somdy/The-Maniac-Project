package TheManiac.cards.maniac_blue.skill;

import TheManiac.actions.DetectAction;
import TheManiac.actions.FlashbackAction;
import TheManiac.actions.TrackAction;
import TheManiac.cards.colorless.skill.Perception;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.stances.LimboStance;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;

import java.util.ArrayList;
import java.util.List;

public class PerceiveDanger extends AbstractManiacCard {
    public static final String ID = "maniac:PerceiveDanger";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/perceive_danger.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 2;
    private static final int TRACK = 4;
    private static final int UPGRADE_TRACK = 2;
    private List<TooltipInfo> tips;

    public PerceiveDanger() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = TRACK;
        this.cardsToPreview = new Perception();
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[3], EXTENDED_DESCRIPTION[4]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DetectAction(this.magicNumber, p.discardPile));
        if (p.stance.ID.equals(LimboStance.STANCE_ID)) {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(this.cardsToPreview.makeStatEquivalentCopy(), 1, true, true));
        }
        
        if (enchanted) {
            if (this.enchantment == 2) {
                this.addToBot(new FlashbackAction(this, this.enchantNumber, false));
            }
            else if (this.enchantment == 3) {
                this.addToBot(new DamageAllEnemiesAction(p, DamageInfo.createDamageMatrix(this.enchantNumber, true, false), DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            }
        }
    }

    @Override
    public void WhenDetected() {
        if (enchanted) {
            if (this.enchantment == 1) {
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, this.enchantNumber), this.enchantNumber));
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
            this.modifyEnchants(1);
        }
        else if (this.enchantment == 2) {
            this.modifyEnchants(2);
        } else {
            this.modifyEnchants(8);
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
            this.upgradeMagicNumber(UPGRADE_TRACK);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new PerceiveDanger();
    }
}
