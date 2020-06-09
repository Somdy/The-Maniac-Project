package TheManiac.cards.the_possessed.risks;

import TheManiac.TheManiac;
import TheManiac.actions.ThePossessedAction.RiskUniversalThrillActions;
import TheManiac.powers.GainDexterityPower;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;

public class DustAndAshes extends AbstractRisksCard {
    public static final String ID = TheManiac.makeID("DustAndAshes");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/risks/skill/dust_and_ashes.png";
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final int COST = 0;
    private static final TooltipInfo INFO = new TooltipInfo(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[1]);
    private int leastCards;
    
    public DustAndAshes() {
        super(ID, IMG_PATH, COST, TYPE, TARGET, INFO);
        this.combatCounter = 0;
        this.counter = 0;
        leastCards = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);
        leastCards += AbstractDungeon.player.energy.energyMaster + this.counter;
        System.out.println(this.name + " requires at least " + leastCards + " cards!");
        if (this.combatCounter > 0) {
            this.addToBot(new LoseHPAction(m, p, this.combatCounter * 2, AbstractGameAction.AttackEffect.SMASH));
            if (this.combatCounter < leastCards) {
                this.addToBot(new LoseHPAction(p, p, this.combatCounter, AbstractGameAction.AttackEffect.SMASH));
            }
        }
        this.counter++;
        this.combatCounter = 0;
    }

    @Override
    public void thrill(AbstractPlayer p, AbstractMonster m, boolean onUse) {
        if (onUse)
            this.addToBot(new RiskUniversalThrillActions(p, m, AbstractDungeon.cardRandomRng.random(99), 4, 5, this.target));
        else {
            this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, -2), -2));
            this.addToBot(new ApplyPowerAction(p, p, new GainDexterityPower(p, 2), 2));
        }
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target, boolean inHand) {
        if (target instanceof AbstractMonster && inHand)
            thrill(AbstractDungeon.player, (AbstractMonster) target, false);
    }

    @Override
    public void triggerOnCardPlayed(AbstractCard card, AbstractCreature target, boolean inHand, boolean inDrawPile) {
        if (card != this && inHand && isThrilled) {
            this.combatCounter++;
        }
    }

    @Override
    public void atEndOfTurn(boolean inHand, boolean inDrawPile) {
        leastCards = 0;
        this.combatCounter = 0;
    }

    @Override
    public void triggerOnGainEnergy(int e, boolean dueToCard) {
        if (dueToCard) {
            leastCards += e;
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DustAndAshes();
    }
}
