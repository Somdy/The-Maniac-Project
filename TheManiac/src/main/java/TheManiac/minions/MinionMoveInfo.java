package TheManiac.minions;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MinionMoveInfo {
    public byte nextMove;
    public AbstractMonster.Intent intent;
    public AbstractCreature target;
    public DamageInfo info;
    public int multiplier;
    public boolean isMultiDamage;

    public MinionMoveInfo(byte nextMove, AbstractMonster.Intent intent, AbstractCreature target, DamageInfo info, int multiplier, boolean isMultiDamage) {
        this.nextMove = nextMove;
        this.intent = intent;
        this.target = target;
        this.info = info;
        this.multiplier = multiplier;
        this.isMultiDamage = isMultiDamage;
    }
}
