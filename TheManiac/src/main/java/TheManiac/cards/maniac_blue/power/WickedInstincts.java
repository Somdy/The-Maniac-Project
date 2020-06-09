package TheManiac.cards.maniac_blue.power;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.powers.WickedInstinctsPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class WickedInstincts extends AbstractManiacCard {
    public static final String ID = "maniac:WickedInstincts";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADED_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/power/wicked_instincts.png";
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 3;

    public WickedInstincts() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.isEthereal = true;
        this.magicNumber = this.baseMagicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!p.hasPower(WickedInstinctsPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new WickedInstinctsPower(this.magicNumber), this.magicNumber));
        }
    }

    @Override
    public void enchant() {
        
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeName();
            this.isEthereal = false;
            this.rawDescription = UPGRADED_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WickedInstincts();
    }
}
