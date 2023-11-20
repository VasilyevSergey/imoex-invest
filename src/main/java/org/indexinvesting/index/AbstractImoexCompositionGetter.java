package org.indexinvesting.index;

import org.indexinvesting.correctionfactor.CorrectionFactor;
import org.indexinvesting.correctionfactor.CorrectionFactorGetter;
import org.indexinvesting.securities.SecurityGetter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractImoexCompositionGetter implements ImoexCompositionGetter {
    private final SecurityGetter securityGetter;
    private final CorrectionFactorGetter correctionFactorGetter;

    public AbstractImoexCompositionGetter(SecurityGetter securityGetter, CorrectionFactorGetter correctionFactorGetter) {
        this.securityGetter = securityGetter;
        this.correctionFactorGetter = correctionFactorGetter;
    }

    public SecurityGetter getSecurityGetter() {
        return securityGetter;
    }

    public CorrectionFactorGetter getCorrectionFactorGetter() {
        return correctionFactorGetter;
    }

    @Override
    public List<Issuer> getWithCorrectedWeights() {
        List<CorrectionFactor> correctionFactors = correctionFactorGetter.get();
        List<Issuer> nativeImoexComposition = this.get();

        List<Issuer> correctedImoexComposition = new ArrayList<>();
        for (Issuer issuer : nativeImoexComposition) {
            String secId = issuer.getSecurity().getSecId();
            CorrectionFactor correctionFactor = getCorrectionFactorBySecId(correctionFactors, secId);
            if (correctionFactor != null) {
                Double correctedWeight = issuer.getWeight() * correctionFactor.getCorrectionFactor();
                issuer.setWeight(correctedWeight);
            }
            correctedImoexComposition.add(issuer);
        }

        return correctedImoexComposition;
    }
    /**
     * Поиск поправочного коэффициента по тикеру
     *
     * @param correctionFactors список поправочных коэффициентов
     * @param secId             тикер
     * @return поправочный коэффициент
     */
    private CorrectionFactor getCorrectionFactorBySecId(List<CorrectionFactor> correctionFactors, String secId) {
        if (secId == null || correctionFactors == null || correctionFactors.isEmpty()) {
            return null;
        }

        return correctionFactors.stream()
                .filter(correctionFactor -> Objects.equals(correctionFactor.getSecId(), secId))
                .findFirst()
                .orElse(null);
    }
}
