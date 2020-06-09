package TheManiac.cards.maniac_blue.skill;

import TheManiac.actions.UndyingDesireAction;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;

import java.util.ArrayList;
import java.util.List;

public class UndyingDesire extends AbstractManiacCard {
    public static final String ID = "maniac:UndyingDesire";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/undying_desire.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 3;
    private static final int UPGRADED_COST = 2;
    private static final int DRAW = 2;
    private static final int LIFE_COST = 3;
    private List<TooltipInfo> tips;

    public UndyingDesire() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = LIFE_COST;
        this.maniacExtraMagicNumber = this.maniacBaseExtraMagicNumber = DRAW;
        this.enchantNumber = this.baseEnchantNumber = 0;
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[2], EXTENDED_DESCRIPTION[3]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new OfferingEffect(), 0.05f));
        AbstractDungeon.actionManager.addToBottom(new LoseHPAction(p, p, this.magicNumber));
        for (int i = 0; i < this.maniacExtraMagicNumber; i++) {
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(1, new UndyingDesireAction(1, 2)));
        }
        
        if (enchanted) {
            if (this.enchantment == 1) {
                this.addToBot(new VFXAction(new OfferingEffect(), 0.05f));
                this.addToBot(new LoseHPAction(p, p, this.magicNumber));
                for (int i = 0; i < this.maniacExtraMagicNumber; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DrawCardAction(1, new UndyingDesireAction(1, 2)));
                }
            } else {
                this.addToBot(new GainBlockAction(p, p, this.enchantNumber));
            }
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
        switch (this.enchantment) {
            case 1:
                break;
            default:
                this.modifyEnchants(2);
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
            this.upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new UndyingDesire();
    }
}
