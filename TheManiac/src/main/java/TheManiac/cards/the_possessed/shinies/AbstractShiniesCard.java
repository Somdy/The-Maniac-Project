package TheManiac.cards.the_possessed.shinies;

import TheManiac.TheManiac;
import TheManiac.cards.the_possessed.ManiacRisksCard;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractShiniesCard extends ManiacRisksCard {
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("maniac:GloriesCard");
    public List<TooltipInfo> tips = new ArrayList<>();
    
    public AbstractShiniesCard(String id, String img, int cost, CardType type, CardTarget target, TooltipInfo info) {
        super(id, img, cost, type, Enum.THE_SHINIES, target);
        this.purgeOnUse = true;

        this.tips.add(new TooltipInfo(cardStrings.NAME, cardStrings.DESCRIPTION));
        this.tips.add(info);
    }

    public AbstractShiniesCard(String id, String img, int cost, CardType type, CardTarget target) {
        super(id, img, cost, type, Enum.THE_SHINIES, target);
        this.purgeOnUse = true;

        this.tips.add(new TooltipInfo(cardStrings.NAME, cardStrings.DESCRIPTION));
    }

    @Override
    public void setOutlook() {
        this.setOrbTexture("maniacMod/images/512defaults/cardui/card_shinies_orb.png",
                "maniacMod/images/1024portraits/cardui/shinies_orb.png");
        this.setBannerTexture("maniacMod/images/512defaults/cardui/banner_shinies.png",
                "maniacMod/images/1024portraits/cardui/banner_shinies.png");
        switch (this.type) {
            case ATTACK:
                this.setBackgroundTexture("maniacMod/images/512defaults/cardui/bg_attack_shinies.png",
                        "maniacMod/images/1024portraits/cardui/bg_attack_shinies.png");
                this.setPortraitTextures("maniacMod/images/512defaults/cardui/frame_attack_shinies.png",
                        "maniacMod/images/1024portraits/cardui/frame_attack_shinies.png");
                break;
            case SKILL:
                this.setBackgroundTexture("maniacMod/images/512defaults/cardui/bg_skill_shinies.png",
                        "maniacMod/images/1024portraits/cardui/bg_skill_shinies.png");
                this.setPortraitTextures("maniacMod/images/512defaults/cardui/frame_skill_shinies.png",
                        "maniacMod/images/1024portraits/cardui/frame_skill_shinies.png");
                break;
            default:
                this.setBackgroundTexture("maniacMod/images/512defaults/cardui/bg_power.png",
                        "maniacMod/images/1024portraits/cardui/bg_power.png");

        }
    }

    @Override
    public void onObtain() {
        super.onObtain();
        CardCrawlGame.sound.play(TheManiac.makeID("ObtainShinies"));
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        return this.tips;
    }
}
