package TheManiac.cards.maniac_blue.skill;

import TheManiac.actions.DetectAction;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.stances.LimboStance;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;

import java.util.ArrayList;
import java.util.List;

public class ColdHeart extends AbstractManiacCard {
    public static final String ID = "maniac:ColdHeart";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/coldheart.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 1;
    private static final int BLOCK = 6;
    private static final int DRAW = 1;
    private static final int ADDITION_DRAW = 1;
    private static final int UPGRADE_BLOCK = 2;
    private static final int UPGRADE_ADDITION = 1;
    private List<TooltipInfo> tips;

    public ColdHeart() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.block = this.baseBlock = BLOCK;
        this.magicNumber = this.baseMagicNumber = DRAW;
        this.maniacExtraMagicNumber = this.maniacBaseExtraMagicNumber = ADDITION_DRAW;
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[3], EXTENDED_DESCRIPTION[4]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.magicNumber, false));
        if (isInLimbo()) {
            AbstractDungeon.actionManager.addToBottom(new DetectAction(this.maniacExtraMagicNumber, p.drawPile));
            AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction("Neutral"));
        }
        
        if (enchanted) {
            if (this.enchantment == 1) {
                this.addToBot(new LoseBlockAction(p, p, this.enchantNumber));
                this.addToBot(new HealAction(p, p, this.enchantNumber));
            }
            else if (this.enchantment == 3) {
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
    public void WhenDetected() {
        if (enchanted) {
            if (this.enchantment == 2) {
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, this.enchantNumber), this.enchantNumber));
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
            this.modifyEnchants(2);
        } else {
            this.modifyCostForCombat(0);
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
            this.upgradeBlock(UPGRADE_BLOCK);
            this.upgradeManiacExtraMagicNumber(UPGRADE_ADDITION);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ColdHeart();
    }
}
