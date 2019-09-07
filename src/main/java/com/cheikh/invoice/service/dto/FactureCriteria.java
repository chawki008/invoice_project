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
import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.cheikh.invoice.domain.Facture} entity. This class is used
 * in {@link com.cheikh.invoice.web.rest.FactureResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /factures?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FactureCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter etat;

    private StringFilter type;

    private InstantFilter createdAt;

    private InstantFilter lastModifiedAt;

    private LocalDateFilter date;

    private StringFilter info;

    private StringFilter numero;

    private IntegerFilter montantTTC;

    private StringFilter fournisseur;

    private IntegerFilter ecoTax;

    private LongFilter sasisseurId;

    private LongFilter verificateurId;

    private LongFilter correctionId;

    public FactureCriteria(){
    }

    public FactureCriteria(FactureCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.etat = other.etat == null ? null : other.etat.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.lastModifiedAt = other.lastModifiedAt == null ? null : other.lastModifiedAt.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.info = other.info == null ? null : other.info.copy();
        this.numero = other.numero == null ? null : other.numero.copy();
        this.montantTTC = other.montantTTC == null ? null : other.montantTTC.copy();
        this.fournisseur = other.fournisseur == null ? null : other.fournisseur.copy();
        this.ecoTax = other.ecoTax == null ? null : other.ecoTax.copy();
        this.sasisseurId = other.sasisseurId == null ? null : other.sasisseurId.copy();
        this.verificateurId = other.verificateurId == null ? null : other.verificateurId.copy();
        this.correctionId = other.correctionId == null ? null : other.correctionId.copy();
    }

    @Override
    public FactureCriteria copy() {
        return new FactureCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getEtat() {
        return etat;
    }

    public void setEtat(StringFilter etat) {
        this.etat = etat;
    }

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public InstantFilter getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(InstantFilter lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public StringFilter getInfo() {
        return info;
    }

    public void setInfo(StringFilter info) {
        this.info = info;
    }

    public StringFilter getNumero() {
        return numero;
    }

    public void setNumero(StringFilter numero) {
        this.numero = numero;
    }

    public IntegerFilter getMontantTTC() {
        return montantTTC;
    }

    public void setMontantTTC(IntegerFilter montantTTC) {
        this.montantTTC = montantTTC;
    }

    public StringFilter getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(StringFilter fournisseur) {
        this.fournisseur = fournisseur;
    }

    public IntegerFilter getEcoTax() {
        return ecoTax;
    }

    public void setEcoTax(IntegerFilter ecoTax) {
        this.ecoTax = ecoTax;
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

    public LongFilter getCorrectionId() {
        return correctionId;
    }

    public void setCorrectionId(LongFilter correctionId) {
        this.correctionId = correctionId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FactureCriteria that = (FactureCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(etat, that.etat) &&
            Objects.equals(type, that.type) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(lastModifiedAt, that.lastModifiedAt) &&
            Objects.equals(date, that.date) &&
            Objects.equals(info, that.info) &&
            Objects.equals(numero, that.numero) &&
            Objects.equals(montantTTC, that.montantTTC) &&
            Objects.equals(fournisseur, that.fournisseur) &&
            Objects.equals(ecoTax, that.ecoTax) &&
            Objects.equals(sasisseurId, that.sasisseurId) &&
            Objects.equals(verificateurId, that.verificateurId) &&
            Objects.equals(correctionId, that.correctionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        etat,
        type,
        createdAt,
        lastModifiedAt,
        date,
        info,
        numero,
        montantTTC,
        fournisseur,
        ecoTax,
        sasisseurId,
        verificateurId,
        correctionId
        );
    }

    @Override
    public String toString() {
        return "FactureCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (etat != null ? "etat=" + etat + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
                (lastModifiedAt != null ? "lastModifiedAt=" + lastModifiedAt + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (info != null ? "info=" + info + ", " : "") +
                (numero != null ? "numero=" + numero + ", " : "") +
                (montantTTC != null ? "montantTTC=" + montantTTC + ", " : "") +
                (fournisseur != null ? "fournisseur=" + fournisseur + ", " : "") +
                (ecoTax != null ? "ecoTax=" + ecoTax + ", " : "") +
                (sasisseurId != null ? "sasisseurId=" + sasisseurId + ", " : "") +
                (verificateurId != null ? "verificateurId=" + verificateurId + ", " : "") +
                (correctionId != null ? "correctionId=" + correctionId + ", " : "") +
            "}";
    }

}
