package TheManiac.cards.the_possessed.uncertainties;

import TheManiac.TheManiac;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RailroadXing extends AbstractUncertaintiesCard {
    public static final String ID = TheManiac.makeID("RailroadXing");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/uncertainties/attack/railroad_xing.png";
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final int COST = 2;
    
    public RailroadXing() {
        super(ID, IMG_PATH, COST, TYPE, TARGET);
        this.damage = this.baseDamage = 10;
        this.maniacExtraMagicNumber = this.maniacBaseExtraMagicNumber = 0;
        this.isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!p.hand.isEmpty()) {
            int count = p.hand.size();
            for (int i = 0; i < this.multiDamage.length; i++) {
                this.multiDamage[i] += count;
            }
        }
        this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.SLASH_HEAVY, true));
    }

    @Override
    public void atEndOfTurn(boolean inHand, boolean inDrawPile) {
        if ((inHand || inDrawPile) && AbstractDungeon.actionManager.cardsPlayedThisTurn.size() > 0) {
            int DMG = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();
            if (this.maniacExtraMagicNumber > 1) DMG += Math.ceil(this.maniacExtraMagicNumber * 0.5);
            this.addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, DamageInfo.createDamageMatrix(DMG, false, false), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HEAVY, true));
        }
    }

    @Override
    public void smith(int level) {
        super.smith(level);
        upgradeManiacExtraMagicNumber(level);
    }

    @Override
    public AbstractCard makeCopy() {
        return new RailroadXing();
    }
}
