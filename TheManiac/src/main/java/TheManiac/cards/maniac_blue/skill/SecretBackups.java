package TheManiac.cards.maniac_blue.skill;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.actions.utility.DrawPileToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.ArrayList;
import java.util.List;

public class SecretBackups extends AbstractManiacCard {
    public static final String ID = "maniac:SecretBackups";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/secret_backups.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final int drawAmt = 2;
    private static final int UPGRADE_drawAmt = 1;
    private List<TooltipInfo> tips;

    public SecretBackups() {
        super(ID, NAME, IMG_PATH, -2, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = drawAmt;
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[3], EXTENDED_DESCRIPTION[4]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DrawCardAction(p, this.magicNumber, false));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public void triggerOnExhaust() {
        

        if (enchanted) {
            if (this.enchantment == 2) {
                AbstractMonster monster = AbstractDungeon.getMonsters().getRandomMonster(true);
                if (monster != null) {
                    this.addToBot(new ApplyPowerAction(monster, AbstractDungeon.player, new WeakPower(monster, this.enchantNumber, false), this.enchantNumber));
                }
            }
        }
    }

    @Override
    public void WhenDetected() {
        this.addToBot(new DrawCardAction(AbstractDungeon.player, this.magicNumber, false));
        this.addToBot(new GainEnergyAction(1));

        if (enchanted) {
            if (this.enchantment == 1) {
                this.addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, this.enchantNumber, 0.05F));
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
        if (this.enchantment == 1) {
            this.modifyEnchants(4);
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
            this.upgradeMagicNumber(UPGRADE_drawAmt);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SecretBackups();
    }
}
