package com.cheikh.invoice.service.dto;
import java.time.Instant;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.cheikh.invoice.domain.Facture} entity.
 */
public class FactureDTO implements Serializable {

    private Long id;

    private String etat;

    private String type;

    private Instant createdAt;

    private Instant lastModifiedAt;

    private LocalDate date;

    private String info;

    private String numero;

    private Integer montantTTC;

    private String fournisseur;

    private Integer ecoTax;

    @Lob
    private byte[] image;

    private String imageContentType;

    private Long sasisseurId;

    private String sasisseurLogin;

    private Long verificateurId;

    private String verificateurLogin;

    private Set<CorrectionDTO> corrections = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Instant lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Integer getMontantTTC() {
        return montantTTC;
    }

    public void setMontantTTC(Integer montantTTC) {
        this.montantTTC = montantTTC;
    }

    public String getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Integer getEcoTax() {
        return ecoTax;
    }

    public void setEcoTax(Integer ecoTax) {
        this.ecoTax = ecoTax;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
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

    public Set<CorrectionDTO> getCorrections() {
        return corrections;
    }

    public void setCorrections(Set<CorrectionDTO> corrections) {
        this.corrections = corrections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FactureDTO factureDTO = (FactureDTO) o;
        if (factureDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), factureDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FactureDTO{" +
            "id=" + getId() +
            ", etat='" + getEtat() + "'" +
            ", type='" + getType() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", lastModifiedAt='" + getLastModifiedAt() + "'" +
            ", date='" + getDate() + "'" +
            ", info='" + getInfo() + "'" +
            ", numero='" + getNumero() + "'" +
            ", montantTTC=" + getMontantTTC() +
            ", fournisseur='" + getFournisseur() + "'" +
            ", ecoTax=" + getEcoTax() +
            ", image='" + getImage() + "'" +
            ", sasisseur=" + getSasisseurId() +
            ", sasisseur='" + getSasisseurLogin() + "'" +
            ", verificateur=" + getVerificateurId() +
            ", verificateur='" + getVerificateurLogin() + "'" +
            "}";
    }
}
