package TheManiac.helper;

import basemod.interfaces.ISubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;

public class TargetIndicator {
    private static final Logger logger = LogManager.getLogger(TargetIndicator.class.getName());
    private static boolean isHidden = true;
    private static boolean hasLimits = false;
    private static AbstractCreature hoveredCreature;
    private static AbstractPlayer source;
    private static AbstractCreature target;
    private static Vector2 controlPoint;
    private static Vector2 start;
    private static Vector2[] points;
    private static float arrowScale;
    private static float arrowScaleTimer;
    private static float startX;
    private static float startY;
    private static ArrayList<TargetIndicatorSubscriber> indicators = new ArrayList<>();
    private static ArrayList<AbstractCreature> allowedCreatures = new ArrayList<>();
    private static TargetIndicatorSubscriber actionTrigger;
    
    public static void register(ISubscriber sub) {
        boolean dupe = false;
        
        for (TargetIndicatorSubscriber subscriber : indicators) {
            if (subscriber.getClass() == sub.getClass()) {
                dupe = true;
                break;
            }
        }
        
        if (!dupe) indicators.add((TargetIndicatorSubscriber) sub);
    }
    
    public static void init() {
        points = new Vector2[20];
        isHidden = false;
        GameCursor.hidden = true;
        for (int i = 0; i < points.length; i++) points[i] = new Vector2();
    }
    
    private static void close() {
        isHidden = true;
    }

    public static void active(TargetIndicatorSubscriber actor, Vector2 whereStart) {
        logger.info("===指示器被激活===");
        actionTrigger = actor;
        start = whereStart;
        startX = start.x;
        startY = start.y;
        init();
    }

    public static void active(TargetIndicatorSubscriber actor, Vector2 whereStart, ArrayList<AbstractCreature> allows) {
        logger.info("===指示器被激活===");
        actionTrigger = actor;
        start = whereStart;
        startX = start.x;
        startY = start.y;
        allowedCreatures.addAll(allows);
        hasLimits = true;
        logger.info("需要检查目标是否属于：" + allowedCreatures);
        init();
    }

    public static void active(TargetIndicatorSubscriber actor, float x, float y) {
        logger.info("===指示器被激活===");
        actionTrigger = actor;
        startX = x;
        startY = y;
        start = new Vector2(startX, startY);
        init();
    }
    
    public static void setSource(AbstractPlayer sourcePlayer) {
        source = sourcePlayer;
    }
    
    public static void setTarget(AbstractCreature targetCreature) {
        target = targetCreature;
    }
    
    public static AbstractPlayer getSource() {
        if (source == null) throw new NullPointerException();
        return source;
    }
    
    public static AbstractCreature getTarget() {
        if (target == null) throw new NullPointerException();
        return target;
    }
    
    private static boolean isAllowed(AbstractCreature target) {
        if (!hasLimits) return true;
        
        return allowedCreatures.contains(target);
    }
    
    private static void runAction() {
        logger.info("===开始传递参数===");
        for (TargetIndicatorSubscriber indicator : indicators) {
            boolean allowed = indicator.getClass() == actionTrigger.getClass();
            logger.info("判断 " + indicator.getClass() + " 是否为 " + actionTrigger.getClass() + "：" + allowed);
            if (allowed) {
                logger.info("正在传递给 " + indicator.getClass() + " 相应的参数");
                indicator.receivePostTargeted(getSource(), getTarget());
            }
        }
    }
    
    private static void targetModeUpdate() {
        if (InputHelper.justClickedRight || AbstractDungeon.isScreenUp
                || InputHelper.mY > Settings.HEIGHT - 80.0F * Settings.scale || AbstractDungeon.player.hoveredCard != null || InputHelper.mY < 140.0F * Settings.scale) {
            GameCursor.hidden = false;
            close();
        }

        hoveredCreature = null;

        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m.hb.hovered && !m.isDying && isAllowed(m)) {
                hoveredCreature = m;
                break;
            }
        }

        AbstractPlayer p = AbstractDungeon.player;
        if (p.hb.hovered && !p.isDying && isAllowed(p)) hoveredCreature = p;

        if (InputHelper.justClickedLeft) {
            InputHelper.justClickedLeft = false;
            if (hoveredCreature != null) {
                setSource(p);
                setTarget(hoveredCreature);
                runAction();
            }
            GameCursor.hidden = false;
            close();
        }
    }

    public static void renderTargetPointer(SpriteBatch sb) {
        float x = InputHelper.mX;
        float y = InputHelper.mY;
        controlPoint = new Vector2(startX - (x - startX) / 4.0F, startY + (y - startY - 40.0F * Settings.scale) / 2.0F);
        if (hoveredCreature == null) {
            arrowScale = Settings.scale;
            arrowScaleTimer = 0.0F;
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
        } else {
            arrowScaleTimer += Gdx.graphics.getDeltaTime();
            if (arrowScaleTimer > 1.0F) {
                arrowScaleTimer = 1.0F;
            }

            arrowScale = Interpolation.elasticOut.apply(Settings.scale, Settings.scale * 1.2F, arrowScaleTimer);
            sb.setColor(new Color(1.0F, 0.2F, 0.3F, 1.0F));
        }

        Vector2 tmp = new Vector2(controlPoint.x - x, controlPoint.y - y);
        tmp.nor();

        drawCurvedLine(sb, start, new Vector2(x, y), controlPoint);
        sb.draw(ImageMaster.TARGET_UI_ARROW, x - 128F, y - 128F, 128.0F, 128.0F, 256.0F, 256.0F,
                arrowScale, arrowScale, tmp.angle() + 90.0F, 0, 0, 256, 256, false, false);
    }

    private static void drawCurvedLine(SpriteBatch sb, Vector2 start, Vector2 end, Vector2 control) {
        float radius = 7.0F * Settings.scale;

        for (int i = 0; i < points.length - 1; i++) {
            float angle; 
            points[i] = Bezier.quadratic(points[i], i / 20.0F, start, control, end, new Vector2());
            radius += 0.4F * Settings.scale;

            if (i != 0) {
                Vector2 tmp = new Vector2((points[i - 1]).x - (points[i]).x, (points[i - 1]).y - (points[i]).y);
                angle = tmp.nor().angle() + 90.0F;
            } else {
                Vector2 tmp = new Vector2(controlPoint.x - (points[i]).x, controlPoint.y - (points[i]).y);
                angle = tmp.nor().angle() + 270.0F;
            }

            sb.draw(ImageMaster.TARGET_UI_CIRCLE, (points[i]).x - 64.0F, (points[i]).y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, 
                    radius / 18.0F, radius / 18.0F, angle, 0, 0, 128, 128, false, false);
        }
    }
    
    public static void render(SpriteBatch sb) {
        if (!isHidden) {
            renderTargetPointer(sb);
            if (hoveredCreature != null) {
                hoveredCreature.renderReticle(sb);
            }
        }
    }
    
    public static void update() {
        if (!isHidden) targetModeUpdate();
    }
    
    public interface TargetIndicatorSubscriber extends ISubscriber {
        void receivePostTargeted(AbstractCreature source, AbstractCreature target);
    }
}

class TargetIndicatorPatch {

    @SpirePatch( clz = AbstractDungeon.class, method = "render")
    public static class InsertRender {

        @SpireInsertPatch(locator = RenderLocator.class)
        public static void InsertR(Object _obj, SpriteBatch sb) {
            TargetIndicator.render(sb);
        }

        private static class RenderLocator extends SpireInsertLocator {

            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(SpriteBatch.class, "setColor");

                return LineFinder.findInOrder(ctMethodToPatch, methodCallMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "update")
    public static class InsertPostUpdate {
        @SpireInsertPatch(locator = UpdateLocator.class)
        public static void Insert(Object __obj) {
            TargetIndicator.update();
        }

        private static class UpdateLocator extends SpireInsertLocator {

            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(OverlayMenu.class, "update");

                return LineFinder.findInOrder(ctMethodToPatch, methodCallMatcher);
            }
        }
    }
}
