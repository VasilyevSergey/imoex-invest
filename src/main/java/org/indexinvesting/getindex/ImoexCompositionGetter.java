package org.indexinvesting.getindex;

import java.util.List;

public interface ImoexCompositionGetter {
    /**
     * Получение текущего состава индекса МосБиржи
     *
     * @return список тикеров эмитентов и их веса в индексе МосБиржи
     */
    List<Issuer> getImoexComposition();
}
