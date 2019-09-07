package com.cheikh.invoice.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Facture.
 */
@Entity
@Table(name = "facture")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Facture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "etat")
    private String etat;

    @Column(name = "type")
    private String type;

    @Column(name = "created_at")
    private Instant createdAt;
    
    @UpdateTimestamp
    @Column(name = "last_modified_at")
    private Instant lastModifiedAt;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "info")
    private String info;

    @Column(name = "numero")
    private String numero;

    @Column(name = "montant_ttc")
    private Integer montantTTC;

    @Column(name = "fournisseur")
    private String fournisseur;

    @Column(name = "eco_tax")
    private Integer ecoTax;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @ManyToOne
    @JsonIgnoreProperties("factures")
    private User sasisseur;

    @ManyToOne
    @JsonIgnoreProperties("factures")
    private User verificateur;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "facture_correction",
               joinColumns = @JoinColumn(name = "facture_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "correction_id", referencedColumnName = "id"))
    private Set<Correction> corrections = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEtat() {
        return etat;
    }

    public Facture etat(String etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getType() {
        return type;
    }

    public Facture type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Facture createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getLastModifiedAt() {
        return lastModifiedAt;
    }

    public Facture lastModifiedAt(Instant lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
        return this;
    }

    public void setLastModifiedAt(Instant lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public LocalDate getDate() {
        return date;
    }

    public Facture date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public Facture info(String info) {
        this.info = info;
        return this;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getNumero() {
        return numero;
    }

    public Facture numero(String numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Integer getMontantTTC() {
        return montantTTC;
    }

    public Facture montantTTC(Integer montantTTC) {
        this.montantTTC = montantTTC;
        return this;
    }

    public void setMontantTTC(Integer montantTTC) {
        this.montantTTC = montantTTC;
    }

    public String getFournisseur() {
        return fournisseur;
    }

    public Facture fournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
        return this;
    }

    public void setFournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Integer getEcoTax() {
        return ecoTax;
    }

    public Facture ecoTax(Integer ecoTax) {
        this.ecoTax = ecoTax;
        return this;
    }

    public void setEcoTax(Integer ecoTax) {
        this.ecoTax = ecoTax;
    }

    public byte[] getImage() {
        return image;
    }

    public Facture image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Facture imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public User getSasisseur() {
        return sasisseur;
    }

    public Facture sasisseur(User user) {
        this.sasisseur = user;
        return this;
    }

    public void setSasisseur(User user) {
        this.sasisseur = user;
    }

    public User getVerificateur() {
        return verificateur;
    }

    public Facture verificateur(User user) {
        this.verificateur = user;
        return this;
    }

    public void setVerificateur(User user) {
        this.verificateur = user;
    }

    public Set<Correction> getCorrections() {
        return corrections;
    }

    public Facture corrections(Set<Correction> corrections) {
        this.corrections = corrections;
        return this;
    }

    public Facture addCorrection(Correction correction) {
        this.corrections.add(correction);
        correction.getFactures().add(this);
        return this;
    }

    public Facture removeCorrection(Correction correction) {
        this.corrections.remove(correction);
        correction.getFactures().remove(this);
        return this;
    }

    public void setCorrections(Set<Correction> corrections) {
        this.corrections = corrections;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Facture)) {
            return false;
        }
        return id != null && id.equals(((Facture) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Facture{" +
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
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
