package TheManiac.cards.maniac_blue.skill;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.helper.MinionHelper;
import TheManiac.minions.AbstractManiacMinion;
import TheManiac.monsters.minions.StabbingBook;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

public class Deal extends AbstractManiacCard {
    public static final String ID = "maniac:Deal";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/deal.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 2;
    private List<TooltipInfo> tips;
    
    public Deal() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 3;
        this.maniacExtraMagicNumber = this.maniacBaseExtraMagicNumber = 2;
        this.purgeOnUse = true;
        this.isEnchanter = true;
        
        this.tips = new ArrayList<>();
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[1], EXTENDED_DESCRIPTION[2]));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        float x = p.animX - 960F * Settings.scale;
        float y = p.animY + 100F * Settings.scale;
        StabbingBook stabbingBook = new StabbingBook(24 + this.maniacExtraMagicNumber, 20 + this.maniacExtraMagicNumber, this.magicNumber, x, y);
        if (!MinionHelper.summonMinion(stabbingBook, p)) {
            this.rawDescription = EXTENDED_DESCRIPTION[0];
            initializeDescription();
            this.addToBot(new DrawCardAction(p, 1, false));
        } else {
            p.loseGold(5 + this.timesUpgraded);
        }
    }

    @Override
    public void enchant() {
        
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        return this.tips;
    }

    @Override
    public boolean canUpgrade() {
        return true;
    }

    @Override
    public void upgrade() {
        this.upgradeMagicNumber(1);
        this.upgradeManiacExtraMagicNumber(6);
        this.timesUpgraded++;
        this.upgraded = true;
        this.name = NAME + "+" + this.timesUpgraded;
        initializeTitle();
    }

    @Override
    public AbstractCard makeCopy() {
        return new Deal();
    }
}
