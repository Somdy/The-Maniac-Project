package TheManiac.cards.the_possessed.uncertainties;

import TheManiac.TheManiac;
import TheManiac.cards.the_possessed.ManiacRisksCard;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Stellarite extends AbstractUncertaintiesCard {
    public static final String ID = TheManiac.makeID("Stellarite");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/uncertainties/skill/stellarite.png";
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final int COST = -2;
    private static final TooltipInfo INFO = new TooltipInfo(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[1]);
    
    public Stellarite() {
        super(ID, IMG_PATH, COST, TYPE, TARGET, INFO);
        this.magicNumber = this.baseMagicNumber = 2;
    }

    @Override
    public void usePreBattle() {
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, this.magicNumber), this.magicNumber));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, this.magicNumber), this.magicNumber));
    }

    @Override
    public void triggerOnCardPlayed(AbstractCard card, AbstractCreature target, boolean inHand, boolean inDrawPile) {
        if (inHand && card.type == CardType.ATTACK && !(card instanceof ManiacRisksCard)) {
            this.addToBot(new ExhaustSpecificCardAction(card, AbstractDungeon.player.hand));
            this.addToBot(new ExhaustSpecificCardAction(card, AbstractDungeon.player.discardPile));
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

    @Override
    public AbstractCard makeCopy() {
        return new Stellarite();
    }
}
