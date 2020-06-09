package TheManiac.cards.the_possessed.possessed;


import TheManiac.TheManiac;
import TheManiac.actions.ThePossessedAction.Uniques.GiftsDrawCardAction;
import TheManiac.actions.ThePossessedAction.Uniques.GiftsExhaustHandAction;
import TheManiac.actions.ThePossessedAction.Uniques.RollNextCardMoveAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.powers.DexterityPower;

public class Gifts extends AbstractPossessedCard {
    public static final String ID = TheManiac.makeID("Gifts");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/possessed/epilogue.png";
    private static final int COST = -2;
    private int atkDmg;

    public Gifts() {
        super(ID, IMG_PATH, COST);
        this.combatCounter = 0;
        this.damage = this.baseDamage = 5;
        this.atkDmg = 10;
        this.magicNumber = this.baseMagicNumber = 5;
        
        this.DmgInfo.add(new DamageInfo(AbstractDungeon.player, this.atkDmg, DamageInfo.DamageType.THORNS));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        switch (this.nextMove) {
            case 1:
                this.addToBot(new GiftsExhaustHandAction());
                break;
            case 2:
                this.addToBot(new GiftsDrawCardAction(new DamageInfo(p, this.damage, this.damageTypeForTurn)));
                break;
            case 3:
                this.addToBot(new ApplyPowerAction(p, p, new BufferPower(p, this.magicNumber), this.magicNumber));
                this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, -3), -3));
                break;
        }
        this.addToBot(new RollNextCardMoveAction(this));
    }

    @Override
    protected void getNextMove(int chance) {
        if (chance < 37) {
            if (!this.lastTwoMovesContain(1)) {
                this.setNextMove(1, EXTENDED_DESCRIPTION[0], CardTarget.NONE, 1);
            } else {
                if (!this.lastTwoMovesContain(2)) {
                    this.setNextMove(2, EXTENDED_DESCRIPTION[1], CardTarget.SELF, 1);
                }
                else if (AbstractDungeon.cardRandomRng.randomBoolean()) {
                    this.setNextMove(3, EXTENDED_DESCRIPTION[2], CardTarget.SELF, 2);
                } else {
                    this.setNextMove(5, EXTENDED_DESCRIPTION[4], CardTarget.NONE, -2);
                }
            }
        }
        else if (chance < 48) {
            if (!this.lastTwoMovesContain(2)) {
                this.setNextMove(2, EXTENDED_DESCRIPTION[1], CardTarget.SELF, 1);
            } else {
                if (!this.lastTwoMovesContain(1)) {
                    this.setNextMove(1, EXTENDED_DESCRIPTION[0], CardTarget.NONE, 1);
                }
                else if (AbstractDungeon.cardRandomRng.randomBoolean()) {
                    this.setNextMove(5, EXTENDED_DESCRIPTION[4], CardTarget.NONE, -2);
                } else {
                    this.setNextMove(3, EXTENDED_DESCRIPTION[2], CardTarget.SELF, 2);
                }
            }
        }
        else if (chance < 59) {
            if (!this.lastCardMove(3)) {
                this.setNextMove(3, EXTENDED_DESCRIPTION[2], CardTarget.SELF, 2);
            } else {
                if (!this.lastTwoCardMoves(5)) {
                    this.setNextMove(5, EXTENDED_DESCRIPTION[4], CardTarget.NONE, -2);
                }
                else if (this.mostTypesInPile(AbstractDungeon.player.drawPile) == CardType.ATTACK) {
                    this.setNextMove(4, EXTENDED_DESCRIPTION[3], CardTarget.NONE, -2);
                } else {
                    this.setNextMove(1, EXTENDED_DESCRIPTION[0], CardTarget.NONE, 1);
                }
            }
        } else {
            if (!this.lastTwoMovesContain(4)) {
                this.setNextMove(4, EXTENDED_DESCRIPTION[3], CardTarget.NONE, -2);
            } else {
                if (AbstractDungeon.cardRandomRng.randomBoolean()) {
                    this.setNextMove(2, EXTENDED_DESCRIPTION[1], CardTarget.SELF, 1);
                } else {
                    this.setNextMove(5, EXTENDED_DESCRIPTION[4], CardTarget.NONE, -2);
                }
            }
        }
    }

    @Override
    public void onExhaustOtherCard(AbstractCard card, boolean inHand) {
        this.addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, 5));
        this.combatCounter++;
    }

    @Override
    public void atEndOfTurn(boolean inHand, boolean inDrawPile) {
        if (this.nextMove == 5 && this.combatCounter < 2) {
            this.addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, 20, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE, true));
        }
        this.combatCounter = 0;
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target, boolean inHand) {
        if (info.type == DamageInfo.DamageType.NORMAL && damageAmount > 0 && info.owner == AbstractDungeon.player && this.nextMove == 4 
                && target != null && target != AbstractDungeon.player && !target.isDeadOrEscaped()) {
            this.addToBot(new DamageAction(target, this.DmgInfo.get(0), AbstractGameAction.AttackEffect.FIRE, true));
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return this.nextMove != 4 && this.nextMove != 5;
    }

    @Override
    public AbstractCard makeCopy() {
        return new Gifts();
    }
}