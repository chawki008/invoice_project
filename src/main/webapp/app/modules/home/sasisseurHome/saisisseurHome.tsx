import React, { useState, useEffect } from 'react';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { Button, Row, Col, Label } from 'reactstrap';
import { getFactureVide, updateEntity, closeModal, getCorrectionsBySasisseur, getFactureEnTrainDeSaisie } from './sasisseurHome.reducer';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { RouteComponentProps } from 'react-router-dom';
import { defaultValue } from 'app/shared/model/facture.model';
import CorrectionModal from './correctionModal';

export interface ISaisisseurHomeProp extends StateProps, DispatchProps, RouteComponentProps {}

export class SaisisseurHome extends React.Component<ISaisisseurHomeProp> {
  private dataPolling;

  constructor(props) {
    super(props);
    this.props.getFactureEnTrainDeSaisie(props.account.id);

    this.dataPolling = setInterval(() => {
      this.props.getCorrectionsBySasisseur(this.props.account.id);
    }, 10000);
  }

  componentWillUnmount() {
    clearInterval(this.dataPolling);
  }

  componentWillReceiveProps(props) {
    clearInterval(this.dataPolling);
    if (props.polling) {
      this.dataPolling = setInterval(() => {
        this.props.getCorrectionsBySasisseur(this.props.account.id);
      }, 10000);
    }
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { facture } = this.props;
      const entity = {
        ...facture,
        etat: 'SAISIE',
        sasisseurId: this.props.account.id,
        sasisseurLogin: this.props.account.login,
        ...values
      };
      this.props.updateEntity(entity);
    }
  };

  render() {
    const { facture, loading } = this.props;
    const { image, imageContentType } = facture;
    const img_url = `data:${imageContentType};base64,${image}`;
    const img_style = { maxWidth: '100%', height: '100%', width: '100%' };
    return (
      <Row>
        {this.props.showModal ? (
          <CorrectionModal showModal={this.props.showModal} handleClose={this.props.closeModal} corrections={this.props.corrections} />
        ) : (
          <div />
        )}
        <Col lg="8" xs="8" md="8" xl="8">
          <iframe src={img_url} style={img_style} />
        </Col>
        {loading ? (
          <p>Loading...</p>
        ) : (
          <Col lg="4" xs="4" md="4" xl="4">
            {facture == defaultValue ? (
              <h2> AUCUNE FACTURE N'EST DISPONIBLE POUR LE MOMENT!</h2>
            ) : (
              <AvForm model={facture} onSubmit={this.saveEntity}>
                <AvGroup>
                  <Label id="typeLabel" for="facture-type">
                    <Translate contentKey="invoiceProjectApp.facture.type">Type</Translate>
                  </Label>
                  <AvField id="facture-type" type="text" name="type" />
                </AvGroup>
                <AvGroup>
                  <Label id="dateLabel" for="facture-date">
                    <Translate contentKey="invoiceProjectApp.facture.date">Date</Translate>
                  </Label>
                  <AvField id="facture-date" type="date" className="form-control" name="date" />
                </AvGroup>
                <AvGroup>
                  <Label id="infoLabel" for="facture-info">
                    <Translate contentKey="invoiceProjectApp.facture.info">Info</Translate>
                  </Label>
                  <AvField id="facture-info" type="text" name="info" />
                </AvGroup>
                <AvGroup>
                  <Label id="numeroLabel" for="facture-numero">
                    <Translate contentKey="invoiceProjectApp.facture.numero">Numero</Translate>
                  </Label>
                  <AvField id="facture-numero" type="text" name="numero" />
                </AvGroup>
                <AvGroup>
                  <Label id="montantTTCLabel" for="facture-montantTTC">
                    <Translate contentKey="invoiceProjectApp.facture.montantTTC">Montant TTC</Translate>
                  </Label>
                  <AvField id="facture-montantTTC" type="string" className="form-control" name="montantTTC" />
                </AvGroup>
                <AvGroup>
                  <Label id="fournisseurLabel" for="facture-fournisseur">
                    <Translate contentKey="invoiceProjectApp.facture.fournisseur">Fournisseur</Translate>
                  </Label>
                  <AvField id="facture-fournisseur" type="text" name="fournisseur" />
                </AvGroup>
                <AvGroup>
                  <Label id="ecoTaxLabel" for="facture-ecoTax">
                    <Translate contentKey="invoiceProjectApp.facture.ecoTax">Eco Tax</Translate>
                  </Label>
                  <AvField id="facture-ecoTax" type="string" className="form-control" name="ecoTax" />
                </AvGroup>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit">
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
                <br />
                <br />
                <Button color="danger" id="save-entity" type="danger" disabled="true">
                  <span>
                    Factures saisies aujourd'hui: <strong>{this.props.account.nbrFactureSaisies}</strong>
                  </span>
                </Button>
              </AvForm>
            )}
          </Col>
        )}
      </Row>
    );
  }
}

const mapDispatchToProps = { getFactureVide, updateEntity, closeModal, getCorrectionsBySasisseur, getFactureEnTrainDeSaisie };

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated,
  facture: storeState.sasisseurHome.facture,
  loading: storeState.sasisseurHome.loading,
  corrections: storeState.sasisseurHome.corrections,
  showModal: storeState.sasisseurHome.showModal,
  polling: storeState.sasisseurHome.polling
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SaisisseurHome);
