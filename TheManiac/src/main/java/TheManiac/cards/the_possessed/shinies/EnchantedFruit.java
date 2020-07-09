package TheManiac.cards.the_possessed.shinies;

import TheManiac.TheManiac;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class EnchantedFruit extends AbstractShiniesCard {
    public static final String ID = TheManiac.makeID("EnchantedFruit");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/shinies/skill/enchanted_fruit.png";
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 2;
    
    public EnchantedFruit() {
        super(ID, IMG_PATH, COST, TYPE, TARGET);
        this.block = this.baseBlock = 12;
        this.magicNumber = this.baseMagicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, this.block));
        this.addToBot(new DrawCardAction(p, this.magicNumber, false));
    }

    @Override
    public void smith(int level) {
        super.smith(level);
        upgradeBlock(MathUtils.floor(level * 1.25F));
        upgradeMagicNumber(MathUtils.floor(level * 0.5F));
    }

    @Override
    public AbstractCard makeCopy() {
        return new EnchantedFruit();
    }
}
