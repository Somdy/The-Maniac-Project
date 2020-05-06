package TheManiac.cards.maniac_blue.skill;

import TheManiac.TheManiac;
import TheManiac.cards.maniac_blue.AbstractManiacCard;
import TheManiac.character.TheManiacCharacter;
import TheManiac.stances.LimboStance;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ManipulatedFaith extends AbstractManiacCard {
    public static final String ID = "maniac:ManipulatedFaith";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/maniac_blue/skill/manipulated_faith.png";
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheManiacCharacter.Enums.MANIAC_BLUE;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 2;
    private static final int DISCARD = 1;
    private static final int DRAW = 2;

    public ManipulatedFaith() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = DISCARD;
        this.maniacExtraMagicNumber = this.maniacBaseExtraMagicNumber = DRAW;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DiscardAction(p, p, this.magicNumber, false,false));
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.maniacExtraMagicNumber, false));
        AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction(new LimboStance()));
    }

    @Override
    public void enchant() {
        
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeName();
            if (TheManiac.leisureMode) {
                this.upgradeBaseCost(1);
                this.upgradeManiacExtraMagicNumber(1);
            }
            else if (TheManiac.challengerMode) {
                this.upgradeManiacExtraMagicNumber(2);
            }
            else {
                this.upgradeManiacExtraMagicNumber(1);
            }
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ManipulatedFaith();
    }
}
