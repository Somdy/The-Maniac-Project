package TheManiac.actions.PossessedMonsterAction;

import TheManiac.monsters.possessed_enemies.AbstractPossessedMonster;
import TheManiac.vfx.PossessedVfx.VanishEffect;
import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;

public class VanishAction extends AbstractGameAction {
    private AbstractPossessedMonster target;
    private boolean goDeath;
    private Color color;
    
    public VanishAction(AbstractPossessedMonster target, boolean goDeath, Color color) {
        this.target = target;
        this.goDeath = goDeath;
        this.color = color;
        this.duration = 0.5F;
        this.actionType = ActionType.TEXT;
    }
    
    @Override
    public void update() {
        if (this.duration == 0.5F) {
            this.addToBot(new VFXAction(new VanishEffect(target, color)));
            target.vanish(goDeath);
            BaseMod.logger.info(target.name + " 正在消失...");
        }
        
        this.tickDuration();
    }
}