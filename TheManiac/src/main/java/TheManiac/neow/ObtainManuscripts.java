package TheManiac.neow;

import TheManiac.TheManiac;
import TheManiac.relics.PossessedManuscripts;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.neow.NeowReward;

public class ObtainManuscripts extends NeowReward {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(TheManiac.makeID("NeowReward_Manuscripts"));
    public static final String[] TEXT = uiStrings.TEXT;
    private static final String DESCRIPTION = TEXT[0];
    
    public ObtainManuscripts() {
        super(0);
        
        this.optionLabel = DESCRIPTION;
    }

    @Override
    public void activate() {
        if (!AbstractDungeon.player.hasRelic(PossessedManuscripts.ID)) {
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, new PossessedManuscripts());
        }
    }
}