package TheManiac.cards.the_possessed.risks;

import TheManiac.TheManiac;
import TheManiac.actions.ThePossessedAction.FuryOfAirAction;
import TheManiac.actions.ThePossessedAction.RiskUniversalThrillActions;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.GrandFinalEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningOrbPassiveEffect;

public class FuryOfAir extends AbstractRisksCard {
    public static final String ID = TheManiac.makeID("FuryOfAir");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/risks/attack/fury_of_air.png";
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final int COST = 1;
    private static final TooltipInfo INFO = new TooltipInfo(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[1]);
    
    public FuryOfAir() {
        super(ID, IMG_PATH, COST, TYPE, TARGET, INFO);
        this.damage = this.baseDamage = 10;
        this.magicNumber = this.baseMagicNumber = 6;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);
        this.addToBot(new FuryOfAirAction(m, p, this.magicNumber, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
    }

    @Override
    public void thrill(AbstractPlayer p, AbstractMonster m, boolean onUse) {
        if (onUse) {
            this.addToBot(new VFXAction(new LightningEffect(p.hb.cX, p.hb.cY)));
            this.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));
        }
        else
            this.addToBot(new RiskUniversalThrillActions(p, m, AbstractDungeon.cardRandomRng.random(99), this.magicNumber, this.baseBlock, this.target));
    }

    @Override
    public void triggerOnCardPlayed(AbstractCard card, AbstractCreature target, boolean inHand, boolean inDrawPile) {
        if (target instanceof AbstractMonster && inDrawPile && isThrilled)
            thrill(AbstractDungeon.player, (AbstractMonster) target, false);
    }

    @Override
    public AbstractCard makeCopy() {
        return new FuryOfAir();
    }
}
