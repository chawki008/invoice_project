package com.cheikh.invoice.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

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

    @Column(name = "old_value")
    private String oldValue;

    @Column(name = "new_value")
    private String newValue;

    @Column(name = "etat")
    private String etat;

    @ManyToOne
    @JsonIgnoreProperties("corrections")
    private User sasisseur;

    @ManyToOne
    @JsonIgnoreProperties("corrections")
    private User verificateur;

    @ManyToOne
    @JsonIgnoreProperties("corrections")
    private Facture facture;

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

    public String getOldValue() {
        return oldValue;
    }

    public Correction oldValue(String oldValue) {
        this.oldValue = oldValue;
        return this;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public Correction newValue(String newValue) {
        this.newValue = newValue;
        return this;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getEtat() {
        return etat;
    }

    public Correction etat(String etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(String etat) {
        this.etat = etat;
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

    public Facture getFacture() {
        return facture;
    }

    public Correction facture(Facture facture) {
        this.facture = facture;
        return this;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
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
            ", oldValue='" + getOldValue() + "'" +
            ", newValue='" + getNewValue() + "'" +
            ", etat='" + getEtat() + "'" +
            "}";
    }
}
