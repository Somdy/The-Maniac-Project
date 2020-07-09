package TheManiac.cards.the_possessed.uncertainties;

import TheManiac.TheManiac;
import TheManiac.actions.TrackAction;
import TheManiac.patches.CardMarkFieldPatch;
import basemod.abstracts.CustomSavable;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class ShadowVisions extends AbstractUncertaintiesCard implements CustomSavable<ArrayList<Integer>> {
    private static final Logger logger = LogManager.getLogger(ShadowVisions.class.getName());
    public static final String ID = TheManiac.makeID("ShadowVisions");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final String IMG_PATH = "maniacMod/images/1024portraits/uncertainties/skill/shadow_visions.png";
    private static final CardType TYPE = CardType.SKILL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int COST = 1;
    private static final TooltipInfo INFO = new TooltipInfo(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[1]);
    public ArrayList<Integer> shadowsIndex = new ArrayList<>();
    
    public ShadowVisions() {
        super(ID, IMG_PATH, COST, TYPE, TARGET, INFO);
        this.magicNumber = this.baseMagicNumber = 4;
        this.combatCounter = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.combatCounter++;
        this.addToBot(new TrackAction(this.magicNumber, p.drawPile));
        if (this.combatCounter >= 2 && hasAvailableCard()) {
            flipCard();
        }
    }
    
    private void flipCard() {
        if (AbstractDungeon.player.masterDeck.isEmpty()) {
            return;
        }
        
        boolean getFlipper = false;
        
        while (!getFlipper) {
            AbstractCard card = AbstractDungeon.player.masterDeck.getRandomCard(true);
            if (CardMarkFieldPatch.ShadowVisionHideField.isShadowHidden.get(card) || card.uuid.equals(this.uuid)) {
                continue;
            }
            CardMarkFieldPatch.ShadowVisionHideField.isShadowHidden.set(card, true);
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c instanceof ShadowVisions) {
                    ((ShadowVisions) c).shadowsIndex.add(AbstractDungeon.player.masterDeck.group.indexOf(card));
                    logger.info("==已记录的暗影视界成员：" + AbstractDungeon.player.masterDeck.group.get(((ShadowVisions) c).shadowsIndex.get(((ShadowVisions) c).shadowsIndex.size() - 1)).name);
                }
            }
            getFlipper = true;
        }
    }
    
    private boolean hasAvailableCard() {
        if (AbstractDungeon.player.masterDeck.isEmpty()) {
            return false;
        }
        
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (!CardMarkFieldPatch.ShadowVisionHideField.isShadowHidden.get(card) && !card.uuid.equals(this.uuid)) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public void smith(int level) {
        super.smith(level);
        upgradeMagicNumber(level);
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShadowVisions();
    }

    @Override
    public ArrayList<Integer> onSave() {
        ShadowVisions card = (ShadowVisions) AbstractDungeon.player.masterDeck.findCardById(ShadowVisions.ID);
        if (card.shadowsIndex.isEmpty()) {
            logger.info("==暗影视界成员保存出现问题，请联系作者==");
        }
        
        logger.info("==正在保存暗影视界黑掉的牌==");
        for (int i = 0; i < card.shadowsIndex.size(); i++) {
            logger.info("==正在保存第" + i + "位成员，其对应为：" + AbstractDungeon.player.masterDeck.group.get(card.shadowsIndex.get(i)).name
            + " 对应的uuid：" + AbstractDungeon.player.masterDeck.group.get(card.shadowsIndex.get(i)).uuid);
        }
        
        return card.shadowsIndex;
    }

    @Override
    public void onLoad(ArrayList<Integer> records) {
        try {
            if (records == null) {
                logger.info("==暗影视界成员失踪，请联系作者==");
                return;
            }
            
            ShadowVisions sv = (ShadowVisions) AbstractDungeon.player.masterDeck.findCardById(ShadowVisions.ID);
            
            logger.info("==正在读取暗影视界黑掉的牌==");
            for (int i = 0; i < records.size(); i++) {
                sv.shadowsIndex.add(records.get(i));
                /*logger.info("==正在读取第" + i + "位成员，应该为：" + AbstractDungeon.player.masterDeck.group.get(records.get(i)).name 
                        + " 对应的uuid：" + AbstractDungeon.player.masterDeck.group.get(records.get(i)).uuid);*/
                CardMarkFieldPatch.ShadowVisionHideField.isShadowHidden.set(AbstractDungeon.player.masterDeck.group.get(records.get(i)), true);
            }
            
            logger.info("==正在检查暗影视界黑掉的牌==");
            for (int i = 0; i < sv.shadowsIndex.size(); i++) {
                /*logger.info("==正在检查第" + i + "位成员，最终为：" + AbstractDungeon.player.masterDeck.group.get(sv.shadowsIndex.get(i)).name 
                        + " 对应的uuid：" + AbstractDungeon.player.masterDeck.group.get(sv.shadowsIndex.get(i)).uuid);*/
                if (AbstractDungeon.player.masterDeck.group.get(sv.shadowsIndex.get(i)).uuid != AbstractDungeon.player.masterDeck.group.get(records.get(i)).uuid) {
                    logger.info("==检查到第" + i + "位成员出错！请联系作者！！！==");
                }
            }
        } catch (Exception e) {
            logger.info("==暗影视界成员读取出错，请联系作者==");
            e.printStackTrace();
        }
    }
}
