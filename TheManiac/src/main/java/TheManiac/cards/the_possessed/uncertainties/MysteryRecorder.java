package TheManiac.cards.the_possessed.uncertainties;

import TheManiac.TheManiac;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MysteryRecorder extends AbstractUncertaintiesCard {
    public static final String ID = TheManiac.makeID("MysteryRecorder");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/uncertainties/skill/mystery_recorder.png";
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final int COST = -2;
    
    public MysteryRecorder() {
        super(ID, IMG_PATH, COST, TYPE, TARGET);
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if (c.type == CardType.SKILL) {
            if (!AbstractDungeon.player.powers.isEmpty()) {
                for (AbstractPower power : AbstractDungeon.player.powers) {
                    if (power.type == AbstractPower.PowerType.DEBUFF) {
                        this.addToBot(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, power, 1));
                    }
                }
            }
        }
        else if (c.type == CardType.ATTACK && noDebuffs()) {
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (!monster.powers.isEmpty()) {
                    for (AbstractPower power : monster.powers) {
                        if (power.type == AbstractPower.PowerType.BUFF) {
                            power.flash();
                            power.amount++;
                        }
                    }
                }
            }
        }
    }
    
    private boolean noDebuffs() {
        if (!AbstractDungeon.player.powers.isEmpty()) {
            for (AbstractPower power : AbstractDungeon.player.powers) {
                if (power.type == AbstractPower.PowerType.DEBUFF) {
                    return false;
                }
            }
        }
        
        return true;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

    @Override
    public AbstractCard makeCopy() {
        return new MysteryRecorder();
    }
}
