package TheManiac.rewards;

import TheManiac.cards.the_possessed.possessed.BookClub;
import TheManiac.cards.the_possessed.possessed.Gifts;
import TheManiac.cards.the_possessed.possessed.Leviathans;
import TheManiac.helper.ManiacImageMaster;
import TheManiac.helper.ThePossessedPool;
import TheManiac.patches.NewRewardIType;
import basemod.abstracts.CustomReward;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;

public class PossessionsReward extends CustomReward {
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("maniac:PossessedRewards");
    private static final String[] TEXT = uiStrings.TEXT;
    private AbstractPlayer player = AbstractDungeon.player;
    private boolean bookclub;
    private boolean epilogue;
    private boolean leviathan;
    
    public PossessionsReward(boolean bookclub, boolean epilogue, boolean leviathan) {
        super(ManiacImageMaster.POSSES_REWARD, TEXT[8], NewRewardIType.Possessed);
        this.bookclub = bookclub;
        this.epilogue = epilogue;
        this.leviathan = leviathan;
    }

    @Override
    public boolean claimReward() {
        getPossessions();
        return true;
    }
    
    public void getPossessions() {
        ThePossessedPool.reloadCardPool();
        
        int num = 0;

        ArrayList<AbstractCard> ReCard = new ArrayList<>();
        
        if (bookclub) {
            num++;
            ReCard.add(new BookClub());
        }
        if (epilogue) {
            num++;
            ReCard.add(new Gifts());
        }
        if (leviathan) {
            num++;
            ReCard.add(new Leviathans());
        }
        
        if (num > 2) {
            num = 2;
        }
        
        ArrayList<AbstractCard> ReCardItem = new ArrayList<>();
        for (AbstractCard c : ReCard) {
            while (ReCardItem.size() < num || ReCardItem.size() < ReCard.size()) {
                ReCardItem.add(c.makeCopy());
            }
        }

        AbstractDungeon.cardRewardScreen.open(ReCardItem, this, TEXT[9]);
        AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
    }
}
