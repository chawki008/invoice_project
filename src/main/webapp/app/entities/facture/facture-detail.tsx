import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './facture.reducer';
import { IFacture } from 'app/shared/model/facture.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFactureDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class FactureDetail extends React.Component<IFactureDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { factureEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="invoiceProjectApp.facture.detail.title">Facture</Translate> [<b>{factureEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="etat">
                <Translate contentKey="invoiceProjectApp.facture.etat">Etat</Translate>
              </span>
            </dt>
            <dd>{factureEntity.etat}</dd>
            <dt>
              <span id="type">
                <Translate contentKey="invoiceProjectApp.facture.type">Type</Translate>
              </span>
            </dt>
            <dd>{factureEntity.type}</dd>
            <dt>
              <span id="createdAt">
                <Translate contentKey="invoiceProjectApp.facture.createdAt">Created At</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={factureEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="lastModifiedAt">
                <Translate contentKey="invoiceProjectApp.facture.lastModifiedAt">Last Modified At</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={factureEntity.lastModifiedAt} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="date">
                <Translate contentKey="invoiceProjectApp.facture.date">Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={factureEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="info">
                <Translate contentKey="invoiceProjectApp.facture.info">Info</Translate>
              </span>
            </dt>
            <dd>{factureEntity.info}</dd>
            <dt>
              <span id="numero">
                <Translate contentKey="invoiceProjectApp.facture.numero">Numero</Translate>
              </span>
            </dt>
            <dd>{factureEntity.numero}</dd>
            <dt>
              <span id="montantTTC">
                <Translate contentKey="invoiceProjectApp.facture.montantTTC">Montant TTC</Translate>
              </span>
            </dt>
            <dd>{factureEntity.montantTTC}</dd>
            <dt>
              <span id="fournisseur">
                <Translate contentKey="invoiceProjectApp.facture.fournisseur">Fournisseur</Translate>
              </span>
            </dt>
            <dd>{factureEntity.fournisseur}</dd>
            <dt>
              <span id="ecoTax">
                <Translate contentKey="invoiceProjectApp.facture.ecoTax">Eco Tax</Translate>
              </span>
            </dt>
            <dd>{factureEntity.ecoTax}</dd>
            <dt>
              <span id="image">
                <Translate contentKey="invoiceProjectApp.facture.image">Image</Translate>
              </span>
            </dt>
            <dd>
              {factureEntity.image ? (
                <div>
                  <a onClick={openFile(factureEntity.imageContentType, factureEntity.image)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                  <span>
                    {factureEntity.imageContentType}, {byteSize(factureEntity.image)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <Translate contentKey="invoiceProjectApp.facture.sasisseur">Sasisseur</Translate>
            </dt>
            <dd>{factureEntity.sasisseurLogin ? factureEntity.sasisseurLogin : ''}</dd>
            <dt>
              <Translate contentKey="invoiceProjectApp.facture.verificateur">Verificateur</Translate>
            </dt>
            <dd>{factureEntity.verificateurLogin ? factureEntity.verificateurLogin : ''}</dd>
            <dt>
              <Translate contentKey="invoiceProjectApp.facture.correction">Correction</Translate>
            </dt>
            <dd>
              {factureEntity.corrections
                ? factureEntity.corrections.map((val, i) => (
                    <span key={val.id}>
                      <a>{val.id}</a>
                      {i === factureEntity.corrections.length - 1 ? '' : ', '}
                    </span>
                  ))
                : null}
            </dd>
          </dl>
          <Button tag={Link} to="/entity/facture" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/facture/${factureEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ facture }: IRootState) => ({
  factureEntity: facture.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FactureDetail);
