import React from 'react';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { Button, Row, Col, Label } from 'reactstrap';
import { getFactureVide, updateEntity } from './sasisseurHome.reducer';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { convertDateTimeToServer } from 'app/shared/util/date-utils';
import { RouteComponentProps } from 'react-router-dom';

export interface ISaisisseurHomeProp extends StateProps, DispatchProps, RouteComponentProps {}

export class SaisisseurHome extends React.Component<ISaisisseurHomeProp> {
  constructor(props) {
    super(props);
    this.state = {};
    this.props.getFactureVide();
  }

  saveEntity = (event, errors, values) => {
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.lastModifiedAt = convertDateTimeToServer(values.lastModifiedAt);

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
    const img_style = { maxWidth: '100%', height: 'auto' };
    return (
      <Row>
        <Col md="8">
          <img src={img_url} style={img_style} />
        </Col>
        <Col md="4">
          {loading ? (
            <p>Loading...</p>
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
            </AvForm>
          )}
        </Col>
      </Row>
    );
  }
}

const mapDispatchToProps = { getFactureVide, updateEntity };

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated,
  facture: storeState.sasisseurHome.facture,
  loading: storeState.sasisseurHome.loading
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SaisisseurHome);
