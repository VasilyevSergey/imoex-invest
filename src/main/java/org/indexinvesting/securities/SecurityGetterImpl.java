package org.indexinvesting.securities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;

public class SecurityGetterImpl implements SecurityGetter {

    private static final String SECURITIES_PAGE_URL = "https://iss.moex.com/iss/engines/stock/markets/shares/boards/TQBR/securities.html";

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final List<Security> securities;

    public SecurityGetterImpl() {
        this.securities = getSecuritiesFromMoex();
    }

    @Override
    public List<Security> getSecurities() {
        return securities;
    }

    @Override
    public Security getSecurityById(String secId) {
        return securities.stream()
                .filter(Objects::nonNull)
                .filter(security -> Objects.equals(security.getSecId(), secId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Security getSecurityByShortName(String shortName) {
        return securities.stream()
                .filter(Objects::nonNull)
                .filter(security -> Objects.equals(security.getShortName(), shortName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Получение данных о ценных бумагах с сайта МосБиржи
     *
     * @return данные о ценных бумагах
     */
    private List<Security> getSecuritiesFromMoex() {
        Document securitiesPage;
        try {
            securitiesPage = getSecuritiesPage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Element table = securitiesPage.select("table").first();
        if (table == null) {
            throw new RuntimeException("Не удалось загрузить таблицу с ценными бумагами с %s" + SECURITIES_PAGE_URL);
        }

        Elements rows = table.select("tr"); // разбиваем таблицу на строки по тегу
        List<Security> securities = new ArrayList<>();

        for (int i = 1; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements columns = row.select("td");

            String secId = columns.get(0).text();
            String shortName = columns.get(2).text();
            double prevPrice = Double.parseDouble(columns.get(3).text());
            int lotSize = Integer.parseInt(columns.get(4).text());
            String secName = columns.get(9).text();

            Security security = new Security(secId, shortName, prevPrice, lotSize, secName);
            securities.add(security);
        }

        return securities;
    }

    /**
     * Получение страницы с данными о ценных бумагах
     */
    private Document getSecuritiesPage() throws IOException {
        try {
            return Jsoup.connect(SECURITIES_PAGE_URL).get();
        } catch (IOException exception) {
            logger.log(SEVERE, "Не удалось загрузить информацию о ценных бумагах. {}", exception.getMessage());
            throw exception;
        }
    }
}
