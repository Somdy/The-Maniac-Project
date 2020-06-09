package TheManiac.cards.the_possessed.risks;

import TheManiac.TheManiac;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PsychicNightmare extends AbstractRisksCard {
    public static final String ID = TheManiac.makeID("PsychicNightmare");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/risks/attack/psychic_nightmare.png";
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final int COST = 0;
    private static final TooltipInfo INFO = new TooltipInfo(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[1]);
    
    public PsychicNightmare() {
        super(ID, IMG_PATH, COST, TYPE, TARGET, INFO);
        
        this.damage = this.baseDamage = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);
        this.baseDamage = p.drawPile.group.size();
        this.calculateCardDamage(m);
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        this.addToBot(new GainEnergyAction(Math.round(p.drawPile.group.size() / 4F)));
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void thrill(AbstractPlayer p, AbstractMonster m, boolean onUse) {
        if (!onUse)
            this.addToBot(new DamageAction(p, new DamageInfo(p, 4, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SMASH));
    }

    @Override
    public void atEndOfTurn(boolean inHand, boolean inDrawPile) {
        if (inDrawPile) {
            int amount = Math.round(AbstractDungeon.player.drawPile.group.size() / 8F);
            if (amount <= 0) {
                amount = 1;
            }
            System.out.println(this.name + " updates Voids to shuffle: " + amount);
            this.addToBot(new MakeTempCardInDrawPileAction(new VoidCard(), amount, true, true));
        }
    }

    @Override
    public void triggerOnCardPlayed(AbstractCard card, AbstractCreature target, boolean inHand, boolean inDrawPile) {
        if (inDrawPile && target instanceof AbstractMonster && isThrilled)
            thrill(AbstractDungeon.player, (AbstractMonster) target, false);
    }

    @Override
    public void applyPowers() {
        this.baseDamage = AbstractDungeon.player.drawPile.size();
        super.applyPowers();
        this.rawDescription = cardStrings.DESCRIPTION;
        this.rawDescription = this.rawDescription + EXTENDED_DESCRIPTION[2];
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        this.rawDescription = cardStrings.DESCRIPTION;
        this.rawDescription = this.rawDescription + EXTENDED_DESCRIPTION[2];
        initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        super.onMoveToDiscard();
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new PsychicNightmare();
    }
}
