package TheManiac.cards.the_possessed.shinies;

import TheManiac.TheManiac;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CelebrationBus extends AbstractShiniesCard {
    public static final String ID = TheManiac.makeID("CelebrationBus");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/shinies/skill/celebration_bus.png";
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 1;
    
    public CelebrationBus() {
        super(ID, IMG_PATH, COST, TYPE, TARGET);
        
        this.magicNumber = this.baseMagicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DrawCardAction(p, this.magicNumber, false));
        if (rocketInHand()) {
            this.addToBot(new GainEnergyAction(1));
        }
    }
    
    private boolean rocketInHand() {
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card.cardID.equals(RocketshipOfLearning.ID)) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public void triggerOnGlowCheck() {
        if (rocketInHand()) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public void smith(int level) {
        super.smith(level);
        upgradeMagicNumber(1);
    }

    @Override
    public AbstractCard makeCopy() {
        return new CelebrationBus();
    }
}
