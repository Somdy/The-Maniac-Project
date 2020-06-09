package TheManiac.cards.the_possessed.possessed;

import TheManiac.TheManiac;
import TheManiac.cards.the_possessed.ManiacRisksCard;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPossessedCard extends ManiacRisksCard {
    private static final Logger logger = LogManager.getLogger(AbstractPossessedCard.class.getName());
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("maniac:PossessedCard");
    public List<TooltipInfo> tips = new ArrayList<>();
    public int nextMove;
    public ArrayList<Integer> moveHistory = new ArrayList<>();
    public ArrayList<DamageInfo> DmgInfo = new ArrayList<>();
    
    public AbstractPossessedCard(String id, String img, int cost, TooltipInfo info) {
        super(id, img, cost, CardType.SKILL, Enum.THE_POSSESSED, null);
        this.selfRetain = true;
        this.tips.add(new TooltipInfo(cardStrings.NAME, cardStrings.DESCRIPTION));
        this.tips.add(info);
        this.nextMove = -1;
    }

    public AbstractPossessedCard(String id, String img, int cost) {
        super(id, img, cost, CardType.SKILL, Enum.THE_POSSESSED, null);
        this.selfRetain = true;
        this.tips.add(new TooltipInfo(cardStrings.NAME, cardStrings.DESCRIPTION));
        this.nextMove = -1;
    }

    protected abstract void getNextMove(int chance);
    
    public void rollNextMove() {
        getNextMove(AbstractDungeon.cardRandomRng.random(99));
    }
    
    public void setNextMove(int nextMove, String Intent, CardTarget nextTarget, int Cost) {
        this.nextMove = nextMove;
        this.rawDescription = Intent;
        this.target = nextTarget;
        this.costForTurn = this.cost = Cost;
        this.isCostModified = true;
        this.isCostModifiedForTurn = true;
        this.initializeDescription();
        
        if (this.nextMove != -1) {
            this.moveHistory.add(nextMove);
            logger.info("已记录 " + this.name + " 的下一步行动代号：" + this.nextMove);
        }
        
        logger.info(this.name + " 取得下一步行动代号：" + this.nextMove + "，其具体行动应为：" + Intent);
    }
    
    protected boolean lastCardMove(int move) {
        if (this.moveHistory.isEmpty()) {
            return false;
        }

        return this.moveHistory.get(this.moveHistory.size() - 1) == move;
    }
    
    protected boolean lastTwoMovesContain(int move) {
        if (this.moveHistory.size() < 2) {
            return false;
        }
        
        return this.moveHistory.get(this.moveHistory.size() - 1) == move || this.moveHistory.get(this.moveHistory.size() - 2) == move;
    }
    
    protected boolean lastTwoCardMoves(int move) {
        if (this.moveHistory.size() < 2) {
            return false;
        }
        
        return this.moveHistory.get(this.moveHistory.size() - 1) == move && this.moveHistory.get(this.moveHistory.size() - 2) == move;
    }
    
    protected boolean lastCardPlayedThisCombat(CardType type) {
        if (AbstractDungeon.actionManager.cardsPlayedThisCombat.size() < 2) {
            return false;
        }
        
        return AbstractDungeon.actionManager.cardsPlayedThisCombat.get(AbstractDungeon.actionManager.cardsPlayedThisCombat.size() - 2).type == type;
    }
    
    protected boolean lastCardPlayedThisTurn(CardType type) {
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() < 2) {
            return false;
        }
        
        return AbstractDungeon.actionManager.cardsPlayedThisTurn.get(AbstractDungeon.actionManager.cardsPlayedThisTurn.size() - 2).type == type;
    }
    
    public CardType mostTypesInPile(CardGroup group) {
        int attacks = 0;
        int skills = 0;
        int powers = 0;
        
        if (!group.isEmpty()) {
            for (AbstractCard card : group.group) {
                if (card.type == CardType.ATTACK) {
                    attacks++;
                }
                else if (card.type == CardType.SKILL) {
                    skills++;
                }
                else if (card.type == CardType.POWER) {
                    powers++;
                }
            }
        }
        
        int mosts = Math.max(Math.max(attacks, skills), powers);
        
        if (mosts == attacks && mosts != skills && mosts != powers && mosts != 0) {
            return CardType.ATTACK;
        }
        else if (mosts == skills && mosts != powers && mosts != 0) {
            return CardType.SKILL;
        }
        else if (mosts == powers && mosts != 0) {
            return CardType.POWER;
        }
        
        return CardType.CURSE;
    }

    @Override
    public void usePreBattle() {
        super.usePreBattle();
        rollNextMove();
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        rollNextMove();
    }

    @Override
    public void setOutlook() {
        this.setOrbTexture("maniacMod/images/512defaults/cardui/card_possessed_orb.png",
                "maniacMod/images/1024portraits/cardui/possessed_orb.png");
        this.setBannerTexture("maniacMod/images/512defaults/cardui/banner_possessed.png",
                "maniacMod/images/1024portraits/cardui/banner_possessed.png");
        this.setBackgroundTexture("maniacMod/images/512defaults/cardui/bg_possessed.png",
                "maniacMod/images/1024portraits/cardui/bg_possessed.png");
        this.setPortraitTextures("maniacMod/images/512defaults/cardui/frame_possessed.png",
                "maniacMod/images/1024portraits/cardui/frame_possessed.png");
    }

    @Override
    public void onObtain() {
        super.onObtain();
        CardCrawlGame.sound.play(TheManiac.makeID("ObtainPossessed"));
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        return this.tips;
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();
        
        if (card instanceof AbstractPossessedCard) {
            ((AbstractPossessedCard) card).nextMove = this.nextMove;
        }
        
        return card;
    }
}
