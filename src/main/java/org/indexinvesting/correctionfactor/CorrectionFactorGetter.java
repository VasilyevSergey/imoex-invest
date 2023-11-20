package org.indexinvesting.correctionfactor;

import java.util.List;

public interface CorrectionFactorGetter {
    /**
     * Получение поправочных коэффициентов из файла
     *
     * @return список поправочных коэффициентов
     */
    List<CorrectionFactor> get();
}
