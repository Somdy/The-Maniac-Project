package TheManiac.cards.the_possessed.shinies;

import TheManiac.TheManiac;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class RocketshipOfLearning extends AbstractShiniesCard {
    public static final String ID = TheManiac.makeID("RocketshipOfLearning");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/shinies/attack/rocketship_of_learning.png";
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final int COST = 1;
    
    public RocketshipOfLearning() {
        super(ID, IMG_PATH, COST, TYPE, TARGET);
        
        this.damage = this.baseDamage = 8;
        this.magicNumber = this.baseMagicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (busInHand()) {
            int dmg = this.damage * 2;
            this.addToBot(new DamageAllEnemiesAction(p, DamageInfo.createDamageMatrix(dmg, true, false), this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY));
        } else {
            this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, this.magicNumber, false), this.magicNumber));
        }
    }

    private boolean busInHand() {
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card.cardID.equals(CelebrationBus.ID)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void smith(int level) {
        super.smith(level);
        upgradeDamage(level);
    }

    @Override
    public void triggerOnGlowCheck() {
        if (busInHand()) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new RocketshipOfLearning();
    }
}
