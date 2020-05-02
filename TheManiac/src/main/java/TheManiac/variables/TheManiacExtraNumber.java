package TheManiac.variables;

import TheManiac.cards.maniac_blue.AbstractManiacCard;
import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class TheManiacExtraNumber extends DynamicVariable {

    @Override
    public String key() {
        return ("ExtraNumber");
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((AbstractManiacCard) card).isManiacExtraMagicNumberModified;
    }

    @Override
    public int value(AbstractCard card) {
        return ((AbstractManiacCard) card).maniacExtraMagicNumber;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ((AbstractManiacCard) card).maniacBaseExtraMagicNumber;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((AbstractManiacCard) card).upgradedManiacExtraMagicNumber;
    }
}
