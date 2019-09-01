package com.cheikh.invoice.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Correction.
 */
@Entity
@Table(name = "correction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Correction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "champ")
    private String champ;

    @ManyToOne
    @JsonIgnoreProperties("corrections")
    private User sasisseur;

    @ManyToOne
    @JsonIgnoreProperties("corrections")
    private User verificateur;

    @ManyToMany(mappedBy = "corrections")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Facture> factures = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChamp() {
        return champ;
    }

    public Correction champ(String champ) {
        this.champ = champ;
        return this;
    }

    public void setChamp(String champ) {
        this.champ = champ;
    }

    public User getSasisseur() {
        return sasisseur;
    }

    public Correction sasisseur(User user) {
        this.sasisseur = user;
        return this;
    }

    public void setSasisseur(User user) {
        this.sasisseur = user;
    }

    public User getVerificateur() {
        return verificateur;
    }

    public Correction verificateur(User user) {
        this.verificateur = user;
        return this;
    }

    public void setVerificateur(User user) {
        this.verificateur = user;
    }

    public Set<Facture> getFactures() {
        return factures;
    }

    public Correction factures(Set<Facture> factures) {
        this.factures = factures;
        return this;
    }

    public Correction addFacture(Facture facture) {
        this.factures.add(facture);
        facture.getCorrections().add(this);
        return this;
    }

    public Correction removeFacture(Facture facture) {
        this.factures.remove(facture);
        facture.getCorrections().remove(this);
        return this;
    }

    public void setFactures(Set<Facture> factures) {
        this.factures = factures;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Correction)) {
            return false;
        }
        return id != null && id.equals(((Correction) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Correction{" +
            "id=" + getId() +
            ", champ='" + getChamp() + "'" +
            "}";
    }
}
