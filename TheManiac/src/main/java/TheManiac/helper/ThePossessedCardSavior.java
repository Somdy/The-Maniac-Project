package TheManiac.helper;

import TheManiac.cards.the_possessed.ManiacRisksCard;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ThePossessedCardSavior implements CustomSavable<HashMap<String, Integer>> {
    private static final Logger logger = LogManager.getLogger(ThePossessedCardSavior.class.getName());
    public static HashMap<String, Integer> SchindlerList = new HashMap<>();
    
    public ThePossessedCardSavior() {}

    public static void loadSavior(ManiacRisksCard card, int level) {
        AbstractPlayer player = AbstractDungeon.player;
        if (player != null && !player.masterDeck.isEmpty()) {
            AbstractCard target = player.masterDeck.getSpecificCard(card);
            if (target instanceof ManiacRisksCard) {
                logger.info("已记录 " + ((ManiacRisksCard) target).riskID + " 的强化数据：" + level);
                if (!SchindlerList.containsKey(((ManiacRisksCard) target).riskID)) 
                    SchindlerList.put(((ManiacRisksCard) target).riskID, level);
                else if (SchindlerList.containsKey(((ManiacRisksCard) target).riskID)) 
                    SchindlerList.put(((ManiacRisksCard) target).riskID, SchindlerList.get(((ManiacRisksCard) target).riskID) + level);
            }
        }
    }
    
    @Override
    public HashMap<String, Integer> onSave() {
        logger.info("正在上传群魔的强化数据记录...");
        return SchindlerList;
    }

    @Override
    public void onLoad(HashMap<String, Integer> list) {
        logger.info("===开始加载强化数据===");
        try {
            if (list == null) {
                return;
            }
            
            for (Map.Entry<String, Integer> Entry : list.entrySet()) {
                String target = Entry.getKey();
                if (target != null) {
                    logger.info("正在加载群魔的强化记录");
                    SchindlerList.put(target, list.get(target));
                }
            }
            
            logger.info("记录加载加成===开始进行修改");
            int num = 0;
            AbstractPlayer player = AbstractDungeon.player;
            if (player != null && !player.masterDeck.isEmpty()) {
                for (Map.Entry<String, Integer> entry : SchindlerList.entrySet()) {
                    String target = entry.getKey();
                    ManiacRisksCard card = findCardByRiskID(target, player.masterDeck);
                    if (card != null) {
                        logger.info("正在对" + card.name + "进行强化，对应的数据为：" + SchindlerList.get(target));
                        card.smith(SchindlerList.get(target));
                        num++;
                    }
                }
            }
            
            if (num == 0) logger.info("===Unknown Error===");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private ManiacRisksCard findCardByRiskID(String riskID, CardGroup group) {
        if (group.isEmpty()) return null;
        
        for (AbstractCard card : group.group) {
            if (card instanceof ManiacRisksCard && ((ManiacRisksCard) card).riskID.equals(riskID)) {
                return (ManiacRisksCard) card;
            }
        }
        
        return null;
    }
}
