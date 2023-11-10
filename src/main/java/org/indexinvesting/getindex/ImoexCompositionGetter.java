package org.indexinvesting.getindex;

import java.util.List;

public interface ImoexCompositionGetter {
    /**
     * Получение текущего состава индекса МосБиржи. Актуальный состав индекса МосБиржи для формирования файла можно
     * узнать здесь https://www.moex.com/ru/index/IMOEX/constituents
     *
     * @return список тикеров эмитентов и их веса в индексе МосБиржи
     */
    List<Issuer> get();
}
