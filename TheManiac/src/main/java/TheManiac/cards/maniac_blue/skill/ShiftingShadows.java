package TheManiac.cards.maniac_blue.skill;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.FoilsPower;
import TheManiac.powers.ShiftingShadowsPower;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

import java.util.ArrayList;
import java.util.List;

public class ShiftingShadows extends AbstractManiacCard {
    public static final String ID = "maniac:ShiftingShadows";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/shifting_shadows.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 2;
    private static final int UPGRADED_COST = 1;
    private List<TooltipInfo> tips;

    public ShiftingShadows() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 6;
        this.isUnreal = true;
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[3], EXTENDED_DESCRIPTION[4]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ShiftingShadowsPower(p, 1)));
        
        if (enchanted) {
            if (this.enchantment == 1) {
                for (AbstractMonster monster : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                    if (!monster.isDeadOrEscaped()) {
                        this.addToBot(new ApplyPowerAction(p, p, new FoilsPower(p, this.enchantNumber), this.enchantNumber));
                    }
                }
            }
            else if (this.enchantment == 2) {
                if (p.hand.isEmpty()) {
                    this.addToBot(new GainBlockAction(p, p, this.enchantNumber));
                }
            } else {
                for (int i = 0; i < this.enchantNumber; i++) {
                    AbstractCard card = this.makeCopy();
                    this.addToBot(new MakeTempCardInHandAction(card, true, true));
                    for (AbstractCard c : p.hand.group) {
                        if (c.uuid == card.uuid) {
                            c.retain = true;
                            c.exhaust = true;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void WhenDetected() {
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FoilsPower(AbstractDungeon.player, this.magicNumber), this.magicNumber));
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
            this.upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShiftingShadows();
    }
}
