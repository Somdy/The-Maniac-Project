package TheManiac.cards.maniac_blue.skill;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.ArrayList;
import java.util.List;

public class WardAndDefend extends AbstractManiacCard {
    public static final String ID = "maniac:WardAndDefend";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/ward_and_defend.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 1;
    private static final int block_amt = 5;
    private static final int foils_armor = 2;
    private static final int upgrade_amt = 1;
    private List<TooltipInfo> tips;
    
    public WardAndDefend() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.block = this.baseBlock = block_amt;
        this.magicNumber = this.baseMagicNumber = foils_armor;
        this.cardsToPreview = new Defend_Maniac();
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[2], EXTENDED_DESCRIPTION[3]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, this.block));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                Defend_Maniac defend = new Defend_Maniac();
                defend.setPlatedArmor(WardAndDefend.this.magicNumber);
                defend.exhaust = true;
                defend.isEthereal = true;
                defend.purgeOnUse = true;
                this.addToBot(new MakeTempCardInHandAction(defend.makeStatEquivalentCopy(), 1));
                this.isDone = true;
            }
        });
        
        if (enchanted) {
            if (this.enchantment == 1) {
                this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, this.enchantNumber), this.enchantNumber));
                this.addToBot(new ApplyPowerAction(p, p, new LoseDexterityPower(p, this.enchantNumber), this.enchantNumber));
            } else {
                if (p.getPower(WeakPower.POWER_ID) != null) {
                    this.addToBot(new ApplyPowerAction(p, p, new PlatedArmorPower(p, this.enchantNumber), this.enchantNumber));
                }
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
        this.enchantName();
        if (this.enchantment == 1) {
            this.modifyEnchants(1);
        } else {
            this.modifyEnchants(5);
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
            this.upgradeBlock(upgrade_amt);
            this.upgradeMagicNumber(upgrade_amt);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WardAndDefend();
    }
}
