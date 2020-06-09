package TheManiac.cards.the_possessed.possessed;

import TheManiac.TheManiac;
import TheManiac.actions.ThePossessedAction.Uniques.RollNextCardMoveAction;
import TheManiac.actions.TrackAction;
import TheManiac.cards.the_possessed.risks.AbstractRisksCard;
import TheManiac.cards.the_possessed.shinies.AbstractShiniesCard;
import TheManiac.cards.the_possessed.uncertainties.AbstractUncertaintiesCard;
import TheManiac.powers.WeaknessPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;

public class Leviathans extends AbstractPossessedCard {
    public static final String ID = TheManiac.makeID("Leviathans");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/possessed/leviathans.png";
    private static final int COST = -2;
    
    public Leviathans() {
        super(ID, IMG_PATH, COST);
        this.damage = this.baseDamage = 2;
        this.magicNumber = this.baseMagicNumber = 5;
        this.selfRetain = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        switch (this.nextMove) {
            case 1:
                this.addToBot(new TrackAction(-1, p.masterDeck));
                break;
            case 3:
                if (!m.powers.isEmpty()) {
                    for (AbstractPower power : m.powers) {
                        if (power.type == AbstractPower.PowerType.BUFF) {
                            this.addToBot(new RemoveSpecificPowerAction(m, p, power));
                        }
                    }
                }
                this.addToBot(new DamageAction(m, new DamageInfo(p, 20, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
                break;
            case 5:
                int num = 0;
                for (AbstractCard card : p.masterDeck.group) {
                    if (card instanceof AbstractShiniesCard || card instanceof AbstractUncertaintiesCard || card instanceof AbstractRisksCard) {
                        num++;
                    }
                }
                if (num < 0) {
                    num = 1;
                }
                for (int i = 0; i < num; i++) {
                    if (!m.isDeadOrEscaped()) {
                        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH, true));
                    }
                }
                break;
        }
        this.addToBot(new RollNextCardMoveAction(this));
    }

    @Override
    protected void getNextMove(int chance) {
        if (chance < 21) {
            if (!this.lastTwoMovesContain(4)) {
                this.setNextMove(4, EXTENDED_DESCRIPTION[3], CardTarget.NONE, -2);
            } else {
                if (chance < 11 && !this.lastTwoCardMoves(1)) {
                    this.setNextMove(1, EXTENDED_DESCRIPTION[0], CardTarget.SELF, 1);
                }
                else if (chance < 16 && !this.lastTwoMovesContain(5)) {
                    this.setNextMove(5, EXTENDED_DESCRIPTION[4], CardTarget.ENEMY, 2);
                } else {
                    if (AbstractDungeon.cardRandomRng.randomBoolean()) {
                        this.setNextMove(2, EXTENDED_DESCRIPTION[1], CardTarget.NONE, -2);
                    } else {
                        this.setNextMove(3, EXTENDED_DESCRIPTION[2], CardTarget.ENEMY, 1);
                    }
                }
            }
        }
        else if (chance < 32) {
            if (!this.lastTwoCardMoves(2)) {
                this.setNextMove(2, EXTENDED_DESCRIPTION[1], CardTarget.NONE, -2);
            } else {
                if (chance < 24 && !this.lastTwoMovesContain(4)) {
                    this.setNextMove(4, EXTENDED_DESCRIPTION[3], CardTarget.NONE, -2);
                }
                else if (!this.lastCardMove(1)) {
                    this.setNextMove(1, EXTENDED_DESCRIPTION[0], CardTarget.SELF, 1);
                }
                else {
                    if (AbstractDungeon.cardRandomRng.randomBoolean()) {
                        this.setNextMove(3, EXTENDED_DESCRIPTION[2], CardTarget.ENEMY, 1);
                    } else {
                        this.setNextMove(5, EXTENDED_DESCRIPTION[4], CardTarget.ENEMY, 2);
                    }
                }
            }
        }
        else if (chance < 49) {
            if (!this.lastTwoMovesContain(3)) {
                this.setNextMove(3, EXTENDED_DESCRIPTION[2], CardTarget.ENEMY, 1);
            } else {
                if (!this.lastTwoMovesContain(5)) {
                    this.setNextMove(5, EXTENDED_DESCRIPTION[4], CardTarget.ENEMY, 2);
                }
                else if (AbstractDungeon.cardRandomRng.randomBoolean()) {
                    this.setNextMove(2, EXTENDED_DESCRIPTION[1], CardTarget.NONE, -2);
                } else {
                    this.setNextMove(1, EXTENDED_DESCRIPTION[0], CardTarget.SELF, 1);
                }
            }
        } else {
            if (!this.lastTwoMovesContain(1)) {
                this.setNextMove(1, EXTENDED_DESCRIPTION[0], CardTarget.SELF, 1);
            } else {
                if (chance < 61 && !this.lastTwoCardMoves(5)) {
                    this.setNextMove(5, EXTENDED_DESCRIPTION[4], CardTarget.ENEMY, 2);
                }
                else if (AbstractDungeon.cardRandomRng.randomBoolean()) {
                    this.setNextMove(2, EXTENDED_DESCRIPTION[1], CardTarget.NONE, -2);
                } else {
                    this.setNextMove(3, EXTENDED_DESCRIPTION[2], CardTarget.ENEMY, 1);
                }
            }
        }
    }

    @Override
    public float onAttackToModifyDamage(float damageAmount, DamageInfo.DamageType type, boolean inHand) {
        if (damageAmount > 0 && this.nextMove == 2) {
            return damageAmount * 2;
        }
        
        return damageAmount;
    }

    @Override
    public void triggerOnCardPlayed(AbstractCard card, AbstractCreature target, boolean inHand, boolean inDrawPile) {
        if (card.type == CardType.SKILL && (card.costForTurn <= 1 || card.energyOnUse <= 1) && this.nextMove == 4) {
            this.addToBot(new VFXAction(AbstractDungeon.player, new BiteEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, Color.RED), 0.05F));
            this.addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, this.magicNumber, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE, true));
        }
    }

    @Override
    public void atEndOfTurn(boolean inHand, boolean inDrawPile) {
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                this.addToBot(new ApplyPowerAction(monster, AbstractDungeon.player, new WeaknessPower(monster, 6), 6));
            }
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return this.nextMove != 4 && this.nextMove != 2;
    }

    @Override
    public AbstractCard makeCopy() {
        return new Leviathans();
    }
}
