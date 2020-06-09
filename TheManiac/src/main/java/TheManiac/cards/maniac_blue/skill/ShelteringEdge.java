package TheManiac.cards.maniac_blue.skill;

import TheManiac.TheManiac;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.ExitManiacPower;
import TheManiac.powers.FoilsPower;
import TheManiac.stances.LimboStance;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

public class ShelteringEdge extends AbstractManiacCard {
    public static final String ID = "maniac:ShelteringEdge";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/sheltering_edge.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 1;
    private static final int base_block = 6;
    private static final int upgrade_block = 2;
    private List<TooltipInfo> tips;
    
    public ShelteringEdge() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.block = this.baseBlock = base_block;
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[2], EXTENDED_DESCRIPTION[3]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, this.block));
        this.addToBot(new ChangeStanceAction(new LimboStance()));
        this.addToBot(new ApplyPowerAction(p, p, new ExitManiacPower()));
        
        if (enchanted) {
            if (this.enchantment == 1) {
                this.addToBot(new ApplyPowerAction(p, p, new FoilsPower(p, this.enchantNumber), this.enchantNumber));
            } else {
                if (p.currentHealth < p.maxHealth / 2) {
                    this.addToBot(new HealAction(p, p, this.enchantNumber, 0.2F));
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
                this.upgradeBlock(upgrade_block + 2);
            } else {
                this.upgradeBlock(upgrade_block);
            }
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShelteringEdge();
    }
}
