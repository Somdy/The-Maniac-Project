package TheManiac.rewards;

import TheManiac.cards.the_possessed.shinies.JarOfFireflies;
import TheManiac.helper.ManiacImageMaster;
import TheManiac.helper.ThePossessedPool;
import TheManiac.patches.NewRewardIType;
import TheManiac.relics.PossessedManuscripts;
import basemod.abstracts.CustomReward;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class MixesReward extends CustomReward {
    private static final Logger logger = LogManager.getLogger(MixesReward.class.getName());
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("maniac:PossessedRewards");
    private static final String[] TEXT = uiStrings.TEXT;
    private int rewardNum;
    
    public MixesReward() {
        super(ManiacImageMaster.MIXES_REWARD, TEXT[0], NewRewardIType.Possessed);
        this.rewardNum = 3;
    }
    
    public MixesReward(int rewardNum) {
        super(ManiacImageMaster.MIXES_REWARD, TEXT[0], NewRewardIType.Possessed);
        this.rewardNum = rewardNum;
    }

    @Override
    public boolean claimReward() {
        getDiverseRisks();
        return true;
    }
    
    public void getDiverseRisks() {
        ThePossessedPool.reloadCardPool();

        ArrayList<AbstractCard> ReCard = new ArrayList<>();
        
        for (int i = 0; i < this.rewardNum; i++) {
            AbstractCard.CardRarity nextRisks = rollNextRisks();
            AbstractCard card = null;
            boolean dupe = true;
            
            while (dupe) {
                dupe = false;
                
                switch (nextRisks) {
                    case COMMON:
                        card = ThePossessedPool.ShiniesPool.getRandomCard(true);
                        break;
                    case UNCOMMON:
                        card = ThePossessedPool.UncertaintiesPool.getRandomCard(true);
                        break;
                    case RARE:
                        card = ThePossessedPool.RisksPool.getRandomCard(true);
                        break;
                }
                for (AbstractCard c : ReCard) {
                    if (c.cardID.equals(card != null ? card.cardID : null)) {
                        dupe = true;
                        logger.info("Get next risks: " + nextRisks.toString());
                        break;
                    }
                }
                if (card != null && AbstractDungeon.player.masterDeck.findCardById(card.cardID) != null) {
                    dupe = true;
                    logger.info("Get duplicates. Replacing " + card.cardID);
                }
                if (card != null && card.cardID.equals(JarOfFireflies.ID) && AbstractDungeon.player.getRelic(PossessedManuscripts.ID) != null) {
                    for (AbstractRelic relic : AbstractDungeon.player.relics) {
                        if (relic instanceof PossessedManuscripts && ((PossessedManuscripts) relic).activeEffects.get(0)) {
                            float chance = ((PossessedManuscripts) relic).activeAmounts.get(0).floatValue() * 0.1F;
                            if (MathUtils.randomBoolean(0.25F + chance)) {
                                dupe = true;
                                logger.info("Failed to get another Jar of Fireflies.");
                                break;
                            }
                        }
                    }
                }
            }
            
            if (card != null) {
                logger.info("===获取特殊奖励卡：" + card.name);
                ReCard.add(card);
            }
        }
        
        ArrayList<AbstractCard> ReCardItem = new ArrayList<>();
        for (AbstractCard card : ReCard) {
            ReCardItem.add(card.makeCopy());
        }
        
        AbstractDungeon.cardRewardScreen.open(ReCardItem, this, TEXT[1]);
        AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
    }
    
    private AbstractCard.CardRarity rollNextRisks() {
        
        int roll = AbstractDungeon.cardRng.random(98);
        
        if (roll < 34) {
            return AbstractCard.CardRarity.COMMON;
        }
        else if (roll < 67) {
            return AbstractCard.CardRarity.UNCOMMON;
        } else {
            return AbstractCard.CardRarity.RARE;
        }
    }
}
