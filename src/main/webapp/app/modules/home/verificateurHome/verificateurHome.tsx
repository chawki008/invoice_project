import React from 'react';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { Button, Row, Col, Label } from 'reactstrap';
import { getFactureSaisie, updateEntity, createCorrection, getFactureEnTrainDeVerifie } from './verificateurHome.reducer';
import { AvForm, AvGroup, AvField } from 'availity-reactstrap-validation';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { convertDateTimeToServer } from 'app/shared/util/date-utils';
import { RouteComponentProps } from 'react-router-dom';
import { defaultValue } from 'app/shared/model/facture.model';

export interface IVerificateurHomeProp extends StateProps, DispatchProps, RouteComponentProps {}

export class VerificateurHome extends React.Component<IVerificateurHomeProp> {
  constructor(props) {
    super(props);
    this.state = {};
    this.props.getFactureEnTrainDeVerifie(props.account.id);
  }

  saveEntity = (event, errors, values) => {
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.lastModifiedAt = convertDateTimeToServer(values.lastModifiedAt);

    if (errors.length === 0) {
      const { facture } = this.props;
      const entity = {
        ...facture,
        etat: 'VERIFIE',
        verificateurId: this.props.account.id,
        verificateurLogin: this.props.account.login,
        ...values
      };
      let diffs = this.diff(facture, entity);
      let field;
      for (field in diffs) {
        let correction = {
          champ: field,
          oldValue: diffs[field].oldValue,
          newValue: diffs[field].newValue,
          factureId: facture.id,
          sasisseurId: facture.sasisseurId,
          etat: 'PAS_VUE'
        };
        this.props.createCorrection(correction);
      }
      this.props.updateEntity(entity);
    }
  };

  diff = function(obj1, obj2) {
    var compare = function(item1, item2, key) {
      var type1 = Object.prototype.toString.call(item1);
      var type2 = Object.prototype.toString.call(item2);
      if (
        key === 'lastModifiedAt' ||
        key === 'createdAt' ||
        key === 'etat' ||
        key === 'sasisseurId' ||
        key === 'sasisseurLogin' ||
        key === 'verificateurId' ||
        key === 'verificateurLogin'
      )
        return;
      if (type2 === '[object Undefined]') {
        diffs[key] = null;
        return;
      }
      if (type1 !== type2) {
        diffs[key] = { oldValue: item1, newValue: item2 };
        return;
      }
      if (type1 === '[object Object]') {
        var objDiff = this.diff(item1, item2);
        if (Object.keys(objDiff).length > 1) {
          diffs[key] = objDiff;
        }
        return;
      }
      if (type1 === '[object Function]') {
        if (item1.toString() !== item2.toString()) {
          diffs[key] = { oldValue: item1, newValue: item2 };
        }
      } else {
        if (item1 !== item2) {
          diffs[key] = { oldValue: item1, newValue: item2 };
        }
      }
    };
    if (!obj2 || Object.prototype.toString.call(obj2) !== '[object Object]') {
      return obj1;
    }
    var diffs = {};
    var key;
    for (key in obj1) {
      if (obj1.hasOwnProperty(key)) {
        compare(obj1[key], obj2[key], key);
      }
    }
    return diffs;
  };

  render() {
    const { facture, loading } = this.props;
    const { image, imageContentType } = facture;
    const img_url = `data:${imageContentType};base64,${image}`;
    const img_style = { maxWidth: '100%', height: '100%', width: '100%' };
    return (
      <Row>
        <Col md="8">
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
              </AvForm>
            )}
          </Col>
        )}
      </Row>
    );
  }
}

const mapDispatchToProps = { getFactureSaisie, updateEntity, createCorrection, getFactureEnTrainDeVerifie };

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated,
  facture: storeState.verificateurHome.facture,
  loading: storeState.verificateurHome.loading
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(VerificateurHome);
