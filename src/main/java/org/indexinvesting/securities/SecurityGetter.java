package org.indexinvesting.securities;

import java.util.List;

public interface SecurityGetter {

    /**
     * Получение данных о ценных бумагах
     *
     * @return данные о ценных бумагах
     */
    List<Security> getSecurities();

    /**
     * Получение данных о ценной бумаге по её идентификатору
     *
     * @return данные о ценной бумаге
     */
    Security getSecurityById(String secId);

    /**
     * Получение данных о ценной бумаге по её короткому имени
     *
     * @return данные о ценной бумаге
     */
    Security getSecurityByShortName(String shortName);

}
