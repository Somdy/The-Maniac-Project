package TheManiac.cards.maniac_blue.skill;

import TheManiac.TheManiac;
import TheManiac.actions.TrackAction;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.stances.LimboStance;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

public class ManipulatedFaith extends AbstractManiacCard {
    public static final String ID = "maniac:ManipulatedFaith";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/manipulated_faith.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 2;
    private static final int DRAW = 2;
    private List<TooltipInfo> tips;

    public ManipulatedFaith() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = DRAW;
        this.isUnreal = true;
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[4], EXTENDED_DESCRIPTION[5]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.magicNumber, false));
        AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction(new LimboStance()));
        
        if (enchanted) {
            if (this.enchantment == 1) {
                this.addToBot(new DrawCardAction(p, 1, false));
            }
            else if (this.enchantment == 2) {
                this.addToBot(new ExhaustAction(this.enchantNumber, false, false));
                this.addToBot(new DrawCardAction(p, this.enchantNumber, false));
            }
            else if (this.enchantment == 3) {
                this.addToBot(new TrackAction(this.enchantNumber, AbstractDungeon.player.exhaustPile));
            } else {
                this.addToBot(new GainEnergyAction(1));
            }
        }
    }

    @Override
    public void enchant() {
        if (!enchanted) {
            switch (this.enchantOpts(1, 4)) {
                case 1:
                    this.rawDescription += EXTENDED_DESCRIPTION[0];
                    break;
                case 2:
                    this.rawDescription += EXTENDED_DESCRIPTION[1];
                    break;
                case 3:
                    this.rawDescription += EXTENDED_DESCRIPTION[2];
                    break;
                default:
                    this.rawDescription += EXTENDED_DESCRIPTION[3];
            }
            System.out.println(this.name + "gets enchantment opt: " + this.enchantment);
        }
        this.enchantName();
        if (this.enchantment == 1) {
            this.modifyEnchants(0);
        }
        else if (this.enchantment == 2) {
            this.modifyEnchants(1);
        }
        else if (this.enchantment == 3) {
            this.modifyEnchants(3);
        } else {
            this.modifyEnchants(0);
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
            this.upgradeMagicNumber(1);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ManipulatedFaith();
    }
}
