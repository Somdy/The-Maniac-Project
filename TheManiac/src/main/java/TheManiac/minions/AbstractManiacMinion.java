package TheManiac.minions;

import TheManiac.actions.MinionsUnique.RollNextMinionMove;
import TheManiac.character.TheManiacCharacter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class AbstractManiacMinion extends AbstractMonster {
    public static final Logger logger = LogManager.getLogger(AbstractManiacMinion.class.getName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("maniac:AbstractMinion");
    public static final String[] TEXT = uiStrings.TEXT;
    private static final String AGGRESSIVE = TEXT[1];
    private static final String STRATEGIC = TEXT[8];
    private static final String DEFENSIVE = TEXT[11];
    private static final String MAGICAL = TEXT[12];
    private static final String SUPPORTIVE = TEXT[13];
    private static final String UNKNOWN = TEXT[19];
    public AbstractCreature target;
    protected MinionMoveInfo move;
    
    public AbstractManiacMinion(String name, String id, int maxHealth, int minHp, int baseDmg, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
        this.setHp(minHp, maxHealth);
        initializeDmg(baseDmg);
    }
    
    public static class Enums {
        @SpireEnum
        public static AbstractMonster.Intent M_BUFF_PLAYER;
        @SpireEnum
        public static AbstractMonster.Intent M_DEFEND_PLAYER;
        @SpireEnum
        public static EnemyType MINION_AGGRESSIVE;
        @SpireEnum 
        public static EnemyType MINION_DEFENSIVE;
        @SpireEnum
        public static EnemyType MINION_SUPPORTIVE;
    }

    @Override
    public void createIntent() {
        try {
            logger.info(this.name + "正在尝试使用特定的createIntent.");
            createMinionIntent();
        } catch (Exception e) {
            logger.info( "未知原因导致" + this.name + "无法使用特定的createIntent. " + e);
            super.createIntent();
        }
    }
    
    public void createMinionIntent() throws Exception {
        Field intentParticleTimer = AbstractMonster.class.getDeclaredField("intentParticleTimer");
        intentParticleTimer.setAccessible(true);
        Field intentBaseDmg = AbstractMonster.class.getDeclaredField("intentBaseDmg");
        intentBaseDmg.setAccessible(true);
        Field intentMultiAmt = AbstractMonster.class.getDeclaredField("intentMultiAmt");
        intentMultiAmt.setAccessible(true);
        Field isMultiDmg = AbstractMonster.class.getDeclaredField("isMultiDmg");
        isMultiDmg.setAccessible(true);
        
        this.intent = this.move.intent;
        intentParticleTimer.set(this, 0.5F);
        this.nextMove = this.move.nextMove;
        intentBaseDmg.set(this, this.move.info.base);
        if (this.move.info.base > -1) {
            this.calculateDamage(intentBaseDmg.getInt(this));
            if (this.move.isMultiDamage) {
                intentMultiAmt.set(this, this.move.multiplier);
                isMultiDmg.set(this, true);
            } else {
                intentMultiAmt.set(this, -1);
                isMultiDmg.set(this, false);
            }
        }
        
        Field intentImg = AbstractMonster.class.getDeclaredField("intentImg");
        intentImg.setAccessible(true);
        Field intentBg = AbstractMonster.class.getDeclaredField("intentBg");
        intentBg.setAccessible(true);
        Method getIntentImg = AbstractMonster.class.getDeclaredMethod("getIntentImg");
        getIntentImg.setAccessible(true);
        Method getIntentBg = AbstractMonster.class.getDeclaredMethod("getIntentBg");
        getIntentBg.setAccessible(true);

        intentImg.set(this, getIntentImg.invoke(this));
        intentBg.set(this, getIntentBg.invoke(this));
        this.tipIntent = this.intent;
        this.intentAlpha = 0.0F;
        this.intentAlphaTarget = 1.0F;
        this.updateIntentTip();
    }

    protected void setMinionMove(String moveName, byte nextMove, Intent intent, AbstractCreature target, DamageInfo info, int multiplier, boolean isMultiDamage) {
        if (this.nextMove < 0) {
            this.addToBot(new RollNextMinionMove(this, true));
        }
        if (target != null) {
            if ((intent == Intent.ATTACK || intent == Intent.ATTACK_BUFF || intent == Intent.ATTACK_DEBUFF || intent == Intent.ATTACK_DEFEND) && target instanceof AbstractManiacMinion) {
                logger.info("不允许将自己作为攻击的目标！");
                this.addToBot(new RollNextMinionMove(this, false));
            } else {
                this.target = target;
            }
        } else {
            logger.info("没有下一步行动目标！");
            this.addToBot(new RollNextMinionMove(this, false));
        }
        this.moveName = moveName;
        if (nextMove != -1) {
            this.moveHistory.add(nextMove);
        }
        this.move = new MinionMoveInfo(nextMove, intent, target, info, multiplier, isMultiDamage);
        this.createIntent();
        logger.info(this.name + "获取的下一步为：" + nextMove + "，实际的下一步为：" + this.nextMove + "，对应的目标为：" + this.target.name);
    }

    protected void setMinionMove(byte nextMove, Intent intent, AbstractCreature target, DamageInfo info, int multiplier, boolean isMultiDamage) {
        setMinionMove(null, nextMove, intent, target, info, multiplier, isMultiDamage);
    }

    protected void setMinionMove(String moveName, byte nextMove, Intent intent, AbstractCreature target, DamageInfo info) {
        setMinionMove(moveName, nextMove, intent, target, info, 0, false);
    }

    protected void setMinionMove(byte nextMove, Intent intent, AbstractCreature target, DamageInfo info) {
        setMinionMove(null, nextMove, intent, target, info, 0, false);
    }

    protected void setMinionMove(String moveName, byte nextMove, Intent intent, AbstractCreature target) {
        setMinionMove(moveName, nextMove, intent, target, new DamageInfo(this, -99, DamageInfo.DamageType.THORNS), 0, false);
    }
    
    protected void setMinionMove(byte nextMove, Intent intent, AbstractCreature target) {
        setMinionMove(null, nextMove, intent, target, new DamageInfo(this, -99, DamageInfo.DamageType.THORNS), 0, false);
    }
    
    public abstract void takeMinionTurn();
    
    public abstract void forceNextMove(boolean noValidTarget);
    
    public AbstractCreature getValidTarget(boolean monster, boolean friendly) {
        if ((monster && friendly) || (!monster && !friendly)) {
            logger.info("ERROR：不允许同时获取友方目标和敌方目标。");
            return null;
        }
        if (monster) {
            int num = 0;
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDeadOrEscaped() && !(m instanceof AbstractManiacMinion) && m.currentHealth > 0 && !m.id.equals(this.id)) {
                    num++;
                }
            }
            if (num > 0) {
                AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(true);
                
                while (target instanceof AbstractManiacMinion || target.id.equals(this.id) || target.isDeadOrEscaped()) {
                    logger.info(this.name + "选取到错误目标：" + target.name + "，重选。");
                    target = AbstractDungeon.getMonsters().getRandomMonster(true);
                }
                
                return target;
            } else {
                forceNextMove(true);
                return null;
            }
        } else {
            return AbstractDungeon.player;
        }
    }

    @Override
    public void takeTurn() {
        takeMinionTurn();
    }

    public void rollNextMinionMove() {
        this.getMove(AbstractDungeon.aiRng.random(99));
    }
    
    public void initializeDmg(int baseDmg) {
    }
    
    public void updateOnMonsterDeath(AbstractMonster m) {
        if (m == this.move.target)
            this.addToBot(new RollNextMinionMove(this));
    }

    @Override
    public void init() {
        this.healthBarUpdatedEvent();
        this.rollNextMinionMove();
    }

    @SpireOverride
    protected void updateIntentTip() throws Exception {
        Field isMultiDmg = AbstractMonster.class.getDeclaredField("isMultiDmg");
        isMultiDmg.setAccessible(true);
        
        Field intentMultiAmt = AbstractMonster.class.getDeclaredField("intentMultiAmt");
        intentMultiAmt.setAccessible(true);

        Method getAttackIntentTip = AbstractMonster.class.getDeclaredMethod("getAttackIntentTip");
        getAttackIntentTip.setAccessible(true);
        
        Field r_intentTip = AbstractMonster.class.getDeclaredField("intentTip");
        r_intentTip.setAccessible(true);
        
        PowerTip intentTip = (PowerTip) r_intentTip.get(this);
        intentTip.body = TEXT[0] + this.name;
        
        
        if (this.intent == Intent.ATTACK) {
            intentTip.header = AGGRESSIVE;
            if (isMultiDmg.getBoolean(this)) {
                intentTip.body += TEXT[2] + this.target.name + TEXT[3] + this.getIntentDmg() + TEXT[4] + intentMultiAmt.getInt(this) + TEXT[6];
            } else {
                intentTip.body += TEXT[2] + this.target.name + TEXT[3] + this.getIntentDmg() + TEXT[5];
            }
            intentTip.img = (Texture) getAttackIntentTip.invoke(this);
            return;
        }
        else if (this.intent == Intent.ATTACK_BUFF) {
            intentTip.header = AGGRESSIVE;
            if (isMultiDmg.getBoolean(this)) {
                intentTip.body += TEXT[7] + this.target.name + TEXT[3] + this.getIntentDmg() + TEXT[4] + intentMultiAmt.getInt(this) + TEXT[6];
            } else {
                intentTip.body += TEXT[7] + this.target.name + TEXT[3] + this.getIntentDmg() + TEXT[5];
            }
            intentTip.img = ImageMaster.INTENT_ATTACK_BUFF;
            return;
        }
        else if (this.intent == Intent.ATTACK_DEBUFF) {
            intentTip.header = STRATEGIC;
            if (isMultiDmg.getBoolean(this)) {
                intentTip.body += TEXT[9] + this.target.name + TEXT[3] + this.getIntentDmg() + TEXT[4] + intentMultiAmt.getInt(this) + TEXT[6];
            } else {
                intentTip.body += TEXT[9] + this.target.name + TEXT[3] + this.getIntentDmg() + TEXT[5];
            }
            intentTip.img = ImageMaster.INTENT_ATTACK_DEBUFF;
            return;
        }
        else if (this.intent == Intent.ATTACK_DEFEND) {
            intentTip.header = STRATEGIC;
            if (isMultiDmg.getBoolean(this)) {
                intentTip.body += TEXT[10] + this.target.name + TEXT[3] + this.getIntentDmg() + TEXT[4] + intentMultiAmt.getInt(this) + TEXT[6];
            } else {
                intentTip.body += TEXT[10] + this.target.name + TEXT[3] + this.getIntentDmg() + TEXT[5];
            }
            intentTip.img = ImageMaster.INTENT_ATTACK_DEFEND;
            return;
        }
        else if (this.intent == Intent.BUFF) {
            intentTip.header = STRATEGIC;
            intentTip.body += TEXT[14];
            intentTip.img = ImageMaster.INTENT_BUFF;
            return;
        }
        else if (this.intent == Intent.STRONG_DEBUFF) {
            intentTip.header = STRATEGIC;
            intentTip.body = TEXT[15];
            intentTip.img = ImageMaster.INTENT_DEBUFF;
            return;
        }
        else if (this.intent == Enums.M_BUFF_PLAYER) {
            intentTip.header = SUPPORTIVE;
            intentTip.body = TEXT[16];
            intentTip.img = ImageMaster.INTENT_BUFF;
            return;
        }
        else if (this.intent == Enums.M_DEFEND_PLAYER) {
            intentTip.header = SUPPORTIVE;
            intentTip.body = TEXT[17];
            intentTip.img = ImageMaster.INTENT_DEFEND;
            return;
        }
        else if (this.intent == Intent.MAGIC) {
            intentTip.header = MAGICAL;
            intentTip.body = TEXT[18];
            intentTip.img = ImageMaster.INTENT_MAGIC;
            return;
        }
        
        intentTip.header = UNKNOWN;
        intentTip.body = TEXT[20];
        intentTip.img = ImageMaster.INTENT_UNKNOWN;
        
        r_intentTip.set(this, intentTip);
    }

    /*
    @Override
    public void renderTip(SpriteBatch sb) {
        this.tips.clear();
        
        this.tips.add(this.intentTip);
        
        for (AbstractPower p : this.powers) {
            if (p.region48 != null) {
                this.tips.add(new PowerTip(p.name, p.description, p.region48)); continue;
            }
            this.tips.add(new PowerTip(p.name, p.description, p.img));
        }


        if (!this.tips.isEmpty())
        {

            if (this.hb.cX + this.hb.width / 2.0F < TIP_X_THRESHOLD) {
                TipHelper.queuePowerTips(this.hb.cX + this.hb.width / 2.0F + TIP_OFFSET_R_X, this.hb.cY +

                        TipHelper.calculateAdditionalOffset(this.tips, this.hb.cY), this.tips);
            }
            else {

                TipHelper.queuePowerTips(this.hb.cX - this.hb.width / 2.0F + TIP_OFFSET_L_X, this.hb.cY +

                        TipHelper.calculateAdditionalOffset(this.tips, this.hb.cY), this.tips);
            }
        }
    }
    
     */

    @Override
    public void applyEndOfTurnTriggers() {
        super.applyEndOfTurnTriggers();
    }

    @Override
    public void applyPowers() {
        if (this.move.target == null || this.move.target instanceof AbstractManiacMinion) {
            forceNextMove(true);
            return;
        }
        try {
            logger.info(this.name + "正尝试使用特定的applyPowers.");
            applyMinionPowers();
        } catch (Exception e) {
            logger.info("未知原因导致" + this.name + "无法使用特定的applyPowers. " + e);
            super.applyPowers();
        }
    }

    public void applyMinionPowers() throws Exception {
        for (DamageInfo dmg : this.damage) {
            dmg.applyPowers(this, this.move.target);
        }
        
        if (this.move.info.base > -1) {
            calculateDamage(this.move.info.base);
        }

        Field intentImg = AbstractMonster.class.getDeclaredField("intentImg");
        intentImg.setAccessible(true);
        Method getIntentImg = AbstractMonster.class.getDeclaredMethod("getIntentImg");
        getIntentImg.setAccessible(true);
        
        intentImg.set(this, getIntentImg.invoke(this));
        updateIntentTip();
    }

    @SpireOverride
    protected void calculateDamage(int dmg) throws Exception {
        AbstractCreature target = this.target;
        float tmp = dmg;

        for (AbstractPower p : this.powers) {
            tmp = p.atDamageGive(tmp, this.move.info.type);
        }

        for (AbstractPower p : target.powers) {
            tmp = p.atDamageReceive(tmp, this.move.info.type);
        }

        for (AbstractPower p : this.powers) {
            tmp = p.atDamageFinalGive(tmp, this.move.info.type);
        }

        for (AbstractPower p : target.powers) {
            tmp = p.atDamageFinalReceive(tmp, this.move.info.type);
        }

        dmg = MathUtils.floor(tmp);
        if (dmg < 0) {
            dmg = 0;
        }
        
        Field intentDmg = AbstractMonster.class.getDeclaredField("intentDmg");
        intentDmg.setAccessible(true);
        intentDmg.set(this, dmg);
    }

    /*
    @Override
    public void damage(DamageInfo info) {
        if (!(info.owner instanceof AbstractMonster) || info.owner instanceof AbstractManiacMinion) {
            info.output = 0;
            return;
        }
        super.damage(info);
    }
    
     */

    @Override
    public void die() {
        if (AbstractDungeon.player instanceof TheManiacCharacter) {
            ((TheManiacCharacter) AbstractDungeon.player).removeMinion(this);
        } else {
            logger.info("ERROR：一名不自量力的角色召唤了" + this.name + "，但却不能很好地控制它...");
        }
        this.isDead = true;
        die(false);
    }
}
