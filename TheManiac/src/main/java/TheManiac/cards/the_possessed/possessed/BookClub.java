package TheManiac.cards.the_possessed.possessed;

import TheManiac.TheManiac;
import TheManiac.actions.ThePossessedAction.Uniques.BookClubDrawCardAction;
import TheManiac.actions.ThePossessedAction.Uniques.BookClubPlagueDamageAction;
import TheManiac.actions.ThePossessedAction.Uniques.RollNextCardMoveAction;
import TheManiac.powers.PlaguePower;
import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class BookClub extends AbstractPossessedCard {
    public static final String ID = TheManiac.makeID("BookClub");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/possessed/book_club.png";
    private static final int COST = -2;
    public ArrayList<AbstractCard> plagues = new ArrayList<>();
    private int timesPlayed;
    
    public BookClub() {
        super(ID, IMG_PATH, COST);
        this.timesPlayed = 0;
        this.maniacExtraMagicNumber = this.maniacBaseExtraMagicNumber = 5;
        this.magicNumber = this.baseMagicNumber = 1;
        this.damage = this.baseDamage = 50;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        BaseMod.logger.info(this.name + "即将进行的行动为：" + this.nextMove);
        this.timesPlayed++;
        BaseMod.logger.info(this.name + "已被打出：" + this.timesPlayed + "次");
        switch (this.nextMove) {
            case 1:
                this.addToBot(new DrawCardAction(1, new BookClubDrawCardAction(new DamageInfo(p, this.damage, this.damageTypeForTurn))));
                this.addToBot(new GainEnergyAction(1));
                break;
            case 2:
                if (!p.hand.group.isEmpty()) {
                    int count = p.hand.group.size();
                    for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                        if (!monster.isDeadOrEscaped()) {
                            for (int i = 0; i < count; i++) {
                                this.addToBot(new ApplyPowerAction(monster, p, new PlaguePower(monster, p, this.magicNumber), this.magicNumber));
                            }
                        }
                    }
                }
                break;
            case 3:
                for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                    if (!monster.isDeadOrEscaped() && monster.hasPower(PlaguePower.POWER_ID)) {
                        monster.getPower(PlaguePower.POWER_ID).flash();
                        monster.getPower(PlaguePower.POWER_ID).amount *= 2;
                    }
                }
                break;
            case 5:
                this.addToBot(new BookClubPlagueDamageAction(p, m));
                break;
        }
        this.addToBot(new RollNextCardMoveAction(this));
    }

    @Override
    protected void getNextMove(int chance) {
        if (chance < 23) {
            if (!this.lastTwoMovesContain(4)) {
                this.setNextMove(4, EXTENDED_DESCRIPTION[3], CardTarget.NONE, -2);
            } else {
                if (!this.lastCardMove(2)) {
                    this.setNextMove(2, EXTENDED_DESCRIPTION[1], CardTarget.SELF, 1);
                } else {
                    this.setNextMove(1, EXTENDED_DESCRIPTION[0], CardTarget.SELF, 0);
                }
            }
        }
        else if (chance < 34) {
            if (!this.lastTwoMovesContain(3)) {
                this.setNextMove(3, EXTENDED_DESCRIPTION[2], CardTarget.ALL_ENEMY, 2);
            } else {
                if (!this.lastCardMove(5)) {
                    this.setNextMove(5, EXTENDED_DESCRIPTION[4], CardTarget.ENEMY, 2);
                } else {
                    this.setNextMove(4, EXTENDED_DESCRIPTION[3], CardTarget.NONE, -2);
                }
            }
        }
        else if (chance < 46) {
            if (!this.lastCardMove(5)) {
                this.setNextMove(5, EXTENDED_DESCRIPTION[4], CardTarget.ENEMY, 2);
            } else {
                if (AbstractDungeon.cardRandomRng.randomBoolean()) {
                    this.setNextMove(2, EXTENDED_DESCRIPTION[1], CardTarget.SELF, 1);
                } else {
                    this.setNextMove(3, EXTENDED_DESCRIPTION[2], CardTarget.ALL_ENEMY, 1);
                }
            }
        }
        else if (chance < 56) {
            if (!this.lastCardMove(2)) {
                this.setNextMove(2, EXTENDED_DESCRIPTION[1], CardTarget.SELF, 1);
            } else {
                if (AbstractDungeon.cardRandomRng.randomBoolean()) {
                    this.setNextMove(1, EXTENDED_DESCRIPTION[0], CardTarget.SELF, 0);
                } else {
                    this.setNextMove(3, EXTENDED_DESCRIPTION[2], CardTarget.ALL_ENEMY, 2);
                }
            }
        } else {
            if (!this.lastTwoMovesContain(1)) {
                this.setNextMove(1, EXTENDED_DESCRIPTION[0], CardTarget.SELF, 0);
            } else {
                if (this.lastCardPlayedThisCombat(CardType.ATTACK)) {
                    this.setNextMove(2, EXTENDED_DESCRIPTION[1], CardTarget.SELF, 1);
                } else {
                    if (AbstractDungeon.cardRandomRng.randomBoolean()) {
                        this.setNextMove(5, EXTENDED_DESCRIPTION[4], CardTarget.ENEMY, 2);
                    } else {
                        this.setNextMove(4, EXTENDED_DESCRIPTION[3], CardTarget.NONE, -2);
                    }
                }
            }
        }
    }
    
    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return this.nextMove != 4;
    }

    @Override
    public void atEndOfTurn(boolean inHand, boolean inDrawPile) {
        if (this.nextMove == 4) {
            AbstractCard card;
            if (AbstractDungeon.player.hand.group.contains(this) && AbstractDungeon.player.hand.size() > 1) {
                card = AbstractDungeon.player.hand.getRandomCard(true);
                while (card == this) {
                    card = AbstractDungeon.player.hand.getRandomCard(true);
                }
                if (card == null) {
                    card = AbstractDungeon.player.drawPile.getRandomCard(true);
                    if (card == null) {
                        card = AbstractDungeon.player.discardPile.getRandomCard(true);
                    }
                }
                if (card != null && !this.plagues.contains(card)) {
                    this.plagues.add(card);
                    
                }
            }

            if (AbstractDungeon.player.drawPile.group.contains(this) && AbstractDungeon.player.drawPile.size() > 1) {
                card = AbstractDungeon.player.drawPile.getRandomCard(true);
                while (card == this) {
                    card = AbstractDungeon.player.drawPile.getRandomCard(true);
                }
                if (card == null) {
                    card = AbstractDungeon.player.discardPile.getRandomCard(true);
                    if (card == null) {
                        card = AbstractDungeon.player.hand.getRandomCard(true);
                    }
                }
                if (card != null && !this.plagues.contains(card)) {
                    this.plagues.add(card);
                }
            }

            if (AbstractDungeon.player.discardPile.group.contains(this) && AbstractDungeon.player.discardPile.size() > 1) {
                card = AbstractDungeon.player.discardPile.getRandomCard(true);
                while (card == this) {
                    card = AbstractDungeon.player.discardPile.getRandomCard(true);
                }
                if (card == null) {
                    card = AbstractDungeon.player.drawPile.getRandomCard(true);
                    if (card == null) {
                        card = AbstractDungeon.player.hand.getRandomCard(true);
                    }
                }
                if (card != null && !this.plagues.contains(card)) {
                    this.plagues.add(card);
                }
            }
        }
    }

    @Override
    public void triggerOnCardPlayed(AbstractCard card, AbstractCreature target, boolean inHand, boolean inDrawPile) {
        if (this.plagues.contains(card)) {
            //this.plagues.remove(card);
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PlaguePower(AbstractDungeon.player, AbstractDungeon.player, this.maniacExtraMagicNumber), this.maniacExtraMagicNumber));
        }
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target, boolean inHand) {
        if (info.type == DamageInfo.DamageType.NORMAL && target != null) {
            this.addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new PlaguePower(target, AbstractDungeon.player, 2 + this.timesPlayed), 2 + this.timesPlayed));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BookClub();
    }
}
