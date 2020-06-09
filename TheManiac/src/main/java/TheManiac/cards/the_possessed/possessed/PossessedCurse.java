package TheManiac.cards.the_possessed.possessed;

import TheManiac.TheManiac;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class PossessedCurse extends AbstractPossessedCard {
    public static final String ID = TheManiac.makeID("PossessedCurse");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/possessed/possessed_curse.png";
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final TooltipInfo INFO = new TooltipInfo(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[1]);
    
    public PossessedCurse() {
        super(ID, IMG_PATH, -2, INFO);
        this.target = TARGET;
        this.selfRetain = false;
        this.misc = 0;
        this.magicNumber = this.baseMagicNumber = 0;
        this.damage = this.baseDamage = 0;
        initDesc();
    }

    public PossessedCurse(int opt, int baseMagics, int baseDmg) {
        super(ID, IMG_PATH, -2, INFO);
        this.target = TARGET;
        this.selfRetain = false;
        this.misc = opt;
        this.magicNumber = this.baseMagicNumber = baseMagics;
        this.damage = this.baseDamage = baseDmg;
        initDesc();
    }
    
    public void initDesc() {
        switch (this.misc) {
            case 1:
                this.rawDescription = EXTENDED_DESCRIPTION[2];
                break;
            case 2:
                this.rawDescription = EXTENDED_DESCRIPTION[3];
                break;
            case 3:
                this.rawDescription = EXTENDED_DESCRIPTION[4];
                break;    
            case 4:
                this.rawDescription = EXTENDED_DESCRIPTION[5];
                break;
            case 5:
                this.rawDescription = EXTENDED_DESCRIPTION[6];
                break;
            case 6:
                this.rawDescription = EXTENDED_DESCRIPTION[7];
                break;
            case 7:
                this.rawDescription = EXTENDED_DESCRIPTION[8];
                break;   
            default:
                this.rawDescription = DESCRIPTION;
                break;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);
    }

    @Override
    public void onRemoveFromMasterDeck() {
        if (this.misc == 1) {
            AbstractDungeon.player.decreaseMaxHealth(this.magicNumber);
            CardCrawlGame.sound.play("BLOOD_SWISH");
        }
    }

    @Override
    public void atEndOfTurn(boolean inHand, boolean inDrawPile) {
        if (inHand) {
            if (this.misc == 2) {
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, 
                        new DexterityPower(AbstractDungeon.player, -this.magicNumber), -this.magicNumber));
            }
            if (this.misc == 3) {
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                        new StrengthPower(AbstractDungeon.player, -this.magicNumber), -this.magicNumber));
            }
        }
        
        if (this.misc == 4) {
            if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() < this.magicNumber) {
                this.addToBot(new DamageAction(AbstractDungeon.player, 
                        new DamageInfo(AbstractDungeon.player, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                
            }
        }

        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
    }

    @Override
    public void onMonsterDeathInDeck(AbstractMonster m) {
        if (this.misc == 5) {
            int goldsToLose = this.magicNumber;
            int comp = 0;
            
            if (AbstractDungeon.player.gold > 0) {
                comp = Math.abs(goldsToLose - AbstractDungeon.player.gold);
            }
            if (goldsToLose > AbstractDungeon.player.gold) {
                goldsToLose = AbstractDungeon.player.gold;
            }
            
            if (goldsToLose > 0) {
                AbstractDungeon.player.loseGold(goldsToLose);
            }
            if (comp > 0) {
                this.addToBot(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, comp));
            }
        }
    }

    @Override
    public void triggerOnCardPlayed(AbstractCard card, AbstractCreature target, boolean inHand, boolean inDrawPile) {
        if (inHand) {
            if (this.misc == 6) {
                for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                    if (!monster.isDeadOrEscaped()) {
                        this.addToBot(new ApplyPowerAction(monster, monster, new PlatedArmorPower(monster, this.magicNumber), this.magicNumber));
                    }
                }
            }
        }
    }

    @Override
    public float onAttackToModifyDamage(float damageAmount, DamageInfo.DamageType type, boolean inHand) {
        if (this.misc == 7) {
            return damageAmount + this.magicNumber;
        }
        
        return damageAmount;
    }

    @Override
    protected void getNextMove(int chance) {
        
    }

    @Override
    public void usePreBattle() {
        
    }

    @Override
    public void atTurnStart() {
        
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

    @Override
    public AbstractCard makeCopy() {
        return new PossessedCurse(this.misc, this.baseMagicNumber, this.baseDamage);
    }
}
