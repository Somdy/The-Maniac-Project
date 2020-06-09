package TheManiac.cards.the_possessed.risks;

import TheManiac.TheManiac;
import TheManiac.actions.ThePossessedAction.BookOfDarknessAction;
import TheManiac.actions.ThePossessedAction.RiskUniversalThrillActions;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.ReaperEffect;

public class BookOfDarkness extends AbstractRisksCard {
    public static final String ID = TheManiac.makeID("BookOfDarkness");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/risks/attack/book_of_darkness.png";
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final int COST = 1;
    private static final TooltipInfo INFO = new TooltipInfo(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[1]);
    
    public BookOfDarkness() {
        super(ID, IMG_PATH, COST, TYPE, TARGET, INFO);
        this.magicNumber = this.baseMagicNumber = 5;
        this.damage = this.baseDamage = 15;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);
        int leastDmg = this.damage;
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                if (monster.currentBlock > 0) {
                    leastDmg += Math.abs(monster.currentBlock - this.baseDamage);
                } else {
                    leastDmg += Math.min(monster.currentHealth / this.baseDamage, this.baseDamage);
                }
            }
        }
        System.out.println(this.name + " updates least unblocked damage required: " + leastDmg);
        this.addToBot(new VFXAction(new ReaperEffect()));
        this.addToBot(new BookOfDarknessAction(p, this.damage, leastDmg));
    }

    @Override
    public void thrill(AbstractPlayer p, AbstractMonster m, boolean onUse) {
        if (!onUse)
            this.addToBot(new RiskUniversalThrillActions(p, m, AbstractDungeon.cardRandomRng.random(99), this.magicNumber, this.baseDamage, this.target));
        else {
            int amounts = AbstractDungeon.cardRandomRng.randomBoolean() ? 1 : -2;
            this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, amounts), amounts));
        }
    }

    @Override
    public void atEndOfTurn(boolean inHand, boolean inDrawPile) {
        if (inHand && isThrilled)
            thrill(AbstractDungeon.player, AbstractDungeon.getMonsters().getRandomMonster(true), false);
    }

    @Override
    public AbstractCard makeCopy() {
        return new BookOfDarkness();
    }
}
