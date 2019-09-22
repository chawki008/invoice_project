package com.cheikh.invoice.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.cheikh.invoice.domain.Correction} entity. This class is used
 * in {@link com.cheikh.invoice.web.rest.CorrectionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /corrections?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CorrectionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter champ;

    private StringFilter oldValue;

    private StringFilter newValue;

    private StringFilter etat;

    private LongFilter sasisseurId;

    private LongFilter verificateurId;

    private LongFilter factureId;

    public CorrectionCriteria(){
    }

    public CorrectionCriteria(CorrectionCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.champ = other.champ == null ? null : other.champ.copy();
        this.oldValue = other.oldValue == null ? null : other.oldValue.copy();
        this.newValue = other.newValue == null ? null : other.newValue.copy();
        this.etat = other.etat == null ? null : other.etat.copy();
        this.sasisseurId = other.sasisseurId == null ? null : other.sasisseurId.copy();
        this.verificateurId = other.verificateurId == null ? null : other.verificateurId.copy();
        this.factureId = other.factureId == null ? null : other.factureId.copy();
    }

    @Override
    public CorrectionCriteria copy() {
        return new CorrectionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getChamp() {
        return champ;
    }

    public void setChamp(StringFilter champ) {
        this.champ = champ;
    }

    public StringFilter getOldValue() {
        return oldValue;
    }

    public void setOldValue(StringFilter oldValue) {
        this.oldValue = oldValue;
    }

    public StringFilter getNewValue() {
        return newValue;
    }

    public void setNewValue(StringFilter newValue) {
        this.newValue = newValue;
    }

    public StringFilter getEtat() {
        return etat;
    }

    public void setEtat(StringFilter etat) {
        this.etat = etat;
    }

    public LongFilter getSasisseurId() {
        return sasisseurId;
    }

    public void setSasisseurId(LongFilter sasisseurId) {
        this.sasisseurId = sasisseurId;
    }

    public LongFilter getVerificateurId() {
        return verificateurId;
    }

    public void setVerificateurId(LongFilter verificateurId) {
        this.verificateurId = verificateurId;
    }

    public LongFilter getFactureId() {
        return factureId;
    }

    public void setFactureId(LongFilter factureId) {
        this.factureId = factureId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CorrectionCriteria that = (CorrectionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(champ, that.champ) &&
            Objects.equals(oldValue, that.oldValue) &&
            Objects.equals(newValue, that.newValue) &&
            Objects.equals(etat, that.etat) &&
            Objects.equals(sasisseurId, that.sasisseurId) &&
            Objects.equals(verificateurId, that.verificateurId) &&
            Objects.equals(factureId, that.factureId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        champ,
        oldValue,
        newValue,
        etat,
        sasisseurId,
        verificateurId,
        factureId
        );
    }

    @Override
    public String toString() {
        return "CorrectionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (champ != null ? "champ=" + champ + ", " : "") +
                (oldValue != null ? "oldValue=" + oldValue + ", " : "") +
                (newValue != null ? "newValue=" + newValue + ", " : "") +
                (etat != null ? "etat=" + etat + ", " : "") +
                (sasisseurId != null ? "sasisseurId=" + sasisseurId + ", " : "") +
                (verificateurId != null ? "verificateurId=" + verificateurId + ", " : "") +
                (factureId != null ? "factureId=" + factureId + ", " : "") +
            "}";
    }

}
