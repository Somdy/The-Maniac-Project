package TheManiac.cards.maniac_blue.skill;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.FoilsPower;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

import java.util.ArrayList;
import java.util.List;

public class Defend_Maniac extends AbstractManiacCard {
    public static final String ID = "maniac:Defend";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/defend_maniac.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 1;
    private static final int BLOCK = 5;
    private static final int UPGRADE_BLOCK = 3;
    private List<TooltipInfo> tips;

    public Defend_Maniac() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.block = this.baseBlock = BLOCK;
        this.magicNumber = this.baseMagicNumber = 0;
        this.tags.add(CardTags.STARTER_DEFEND);
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[3], EXTENDED_DESCRIPTION[4]));
    }
    
    public void setPlatedArmor(int amount) {
        this.magicNumber = this.baseMagicNumber = amount;
        if (this.baseMagicNumber > 0) {
            this.rawDescription += EXTENDED_DESCRIPTION[0];
        }
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
        if (this.baseMagicNumber > 0) {
            this.addToBot(new ApplyPowerAction(p, p, new FoilsPower(p, this.magicNumber)));
        }
        
        if (enchanted) {
            if (this.enchantment == 1) {
                this.addToBot(new GainBlockAction(p, p, this.enchantNumber));
            } else {
                this.addToBot(new ApplyPowerAction(p, p, new FoilsPower(p, this.enchantNumber), this.enchantNumber));
            }
        }
    }

    @Override
    public void enchant() {
        if (!enchanted) {
            switch (this.enchantOpts(1, 2)) {
                case 1:
                    this.rawDescription += EXTENDED_DESCRIPTION[1];
                    break;
                default:
                    this.rawDescription += EXTENDED_DESCRIPTION[2];
            }
            System.out.println(this.name + "gets enchantment opt: " + this.enchantment);
        }
        this.enchantName();
        this.modifyEnchants(2);
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
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Defend_Maniac();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();
        card.baseMagicNumber = this.baseMagicNumber;
        card.magicNumber = this.magicNumber;
        card.baseDamage = this.baseDamage;
        card.damage = this.damage;
        card.isEthereal = this.isEthereal;
        card.exhaust = this.exhaust;
        card.purgeOnUse = this.purgeOnUse;
        if (card instanceof AbstractManiacCard) {
            ((AbstractManiacCard) card).maniacBaseExtraMagicNumber = this.maniacBaseExtraMagicNumber;
            ((AbstractManiacCard) card).maniacExtraMagicNumber = this.maniacExtraMagicNumber;
        }
        card.description = (ArrayList)this.description.clone();
        return card;
    }
}
