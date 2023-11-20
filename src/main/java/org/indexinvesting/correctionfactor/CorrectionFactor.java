package org.indexinvesting.correctionfactor;

import java.time.LocalDate;

public class CorrectionFactor {
    // тикер
    private String secId;
    // поправочный коэффициент
    private Double correctionFactor;
    // дата, в которую стоит пересмотреть актуальность поправочного коэффициента
    private LocalDate date;
    // комментарий, причина добавления поправочного коэффициента
    private String comment;

    public CorrectionFactor() {
    }

    public void setSecId(String secId) {
        this.secId = secId;
    }

    public void setCorrectionFactor(Double correctionFactor) {
        this.correctionFactor = correctionFactor;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Поправочный коэффициент{" +
                "тикер='" + secId + '\'' +
                ", коэффициент=" + correctionFactor +
                ", дата=" + date +
                ", комментарий='" + comment + '\'' +
                '}';
    }

    public String getSecId() {
        return secId;
    }

    public Double getCorrectionFactor() {
        return correctionFactor;
    }
}
