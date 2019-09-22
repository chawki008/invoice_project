package com.cheikh.invoice.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.cheikh.invoice.domain.Correction} entity.
 */
public class CorrectionDTO implements Serializable {

    private Long id;

    private String champ;

    private String oldValue;

    private String newValue;

    private String etat;


    private Long sasisseurId;

    private String sasisseurLogin;

    private Long verificateurId;

    private String verificateurLogin;

    private Long factureId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChamp() {
        return champ;
    }

    public void setChamp(String champ) {
        this.champ = champ;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Long getSasisseurId() {
        return sasisseurId;
    }

    public void setSasisseurId(Long userId) {
        this.sasisseurId = userId;
    }

    public String getSasisseurLogin() {
        return sasisseurLogin;
    }

    public void setSasisseurLogin(String userLogin) {
        this.sasisseurLogin = userLogin;
    }

    public Long getVerificateurId() {
        return verificateurId;
    }

    public void setVerificateurId(Long userId) {
        this.verificateurId = userId;
    }

    public String getVerificateurLogin() {
        return verificateurLogin;
    }

    public void setVerificateurLogin(String userLogin) {
        this.verificateurLogin = userLogin;
    }

    public Long getFactureId() {
        return factureId;
    }

    public void setFactureId(Long factureId) {
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

        CorrectionDTO correctionDTO = (CorrectionDTO) o;
        if (correctionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), correctionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CorrectionDTO{" +
            "id=" + getId() +
            ", champ='" + getChamp() + "'" +
            ", oldValue='" + getOldValue() + "'" +
            ", newValue='" + getNewValue() + "'" +
            ", etat='" + getEtat() + "'" +
            ", sasisseur=" + getSasisseurId() +
            ", sasisseur='" + getSasisseurLogin() + "'" +
            ", verificateur=" + getVerificateurId() +
            ", verificateur='" + getVerificateurLogin() + "'" +
            ", facture=" + getFactureId() +
            "}";
    }
}
