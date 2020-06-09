package TheManiac.cards.the_possessed.risks;

import TheManiac.TheManiac;
import TheManiac.actions.ThePossessedAction.RiskUniversalThrillActions;
import TheManiac.powers.DrargorPower;
import TheManiac.vfx.EtherDrakeFireEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class EtherDrake extends AbstractRisksCard {
    public static final String ID = TheManiac.makeID("EtherDragon");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/risks/attack/ether_dragon.png";
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardTarget TARGET = CardTarget.ALL;
    private static final int COST = 1;
    
    public EtherDrake() {
        super(ID, IMG_PATH, COST, TYPE, TARGET);
        this.magicNumber = this.baseMagicNumber = 4;
        this.damage = this.baseDamage = 20;
        this.isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);
        this.addToBot(new VFXAction(new EtherDrakeFireEffect(), 0.25F));
        this.addToBot(new DamageAction(p, new DamageInfo(p,this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.FIRE, true));
        this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.FIRE));
        this.addToBot(new ApplyPowerAction(p, p, new DrargorPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void thrill(AbstractPlayer p, AbstractMonster m, boolean onUse) {
        if (AbstractDungeon.cardRandomRng.randomBoolean() && onUse)
            this.addToBot(new LoseHPAction(p, p, this.magicNumber));
        else if (!onUse)
            this.addToBot(new RiskUniversalThrillActions(p, m, AbstractDungeon.cardRandomRng.random(99), this.magicNumber, CardTarget.ENEMY));
    }

    @Override
    public void onMonsterDeath(AbstractMonster m, boolean inHand, boolean inDrawPile) {
        if ((inHand || inDrawPile) && isThrilled)
            thrill(AbstractDungeon.player, AbstractDungeon.getMonsters().getRandomMonster(true), false);
    }

    @Override
    public AbstractCard makeCopy() {
        return new EtherDrake();
    }
}
