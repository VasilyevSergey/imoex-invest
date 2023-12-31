package org.indexinvesting.index;

import java.util.List;

public interface ImoexCompositionGetter {
    /**
     * Получение текущего состава индекса МосБиржи. Актуальный состав индекса МосБиржи для формирования файла можно
     * узнать здесь https://www.moex.com/ru/index/IMOEX/constituents
     *
     * @return список ценных бумаг и их весов в индексе МосБиржи
     */
    List<Issuer> get();

    /**
     * Получение состава индекса МосБиржи, скорректированного с учётом поправочных коэффициентов, установленных
     * пользователем. Коэффициенты изменяют относительный вес ценной бумаги в индексе МосБиржи.
     *
     * @return скорректированный список ценных бумаг и их весов в индексе МосБиржи
     */
    List<Issuer> getWithCorrectedWeights();
}
