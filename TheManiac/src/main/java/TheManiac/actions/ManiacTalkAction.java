package TheManiac.actions;

import TheManiac.TheManiac;
import TheManiac.character.TheManiacCharacter;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.SpeechBubble;

public class ManiacTalkAction extends AbstractGameAction {
    private String msg;
    private boolean talked;
    private float bubbleDuration;
    
    public ManiacTalkAction(String text, float duration, float bubbleDuration) {
        this.talked = false;
        this.msg = text;
        this.actionType = ActionType.TEXT;
        this.bubbleDuration = bubbleDuration;
        if (Settings.FAST_MODE) {
            this.duration = Settings.ACTION_DUR_MED;
        } else {
            this.duration = duration;
        }
    }
    
    @Override
    public void update() {
        if (!TheManiac.talkiveMode) {
            this.isDone = true;
            return;
        }
        if (!(AbstractDungeon.player instanceof TheManiacCharacter)) {
            this.isDone = true;
            return;
        }
        
        if (!talked) {
            AbstractDungeon.effectList.add(new SpeechBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, bubbleDuration, msg, true));
            talked = true;
        }
        
        this.tickDuration();
    }
}