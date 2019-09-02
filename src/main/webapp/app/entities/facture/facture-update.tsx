import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { ICorrection } from 'app/shared/model/correction.model';
import { getEntities as getCorrections } from 'app/entities/correction/correction.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './facture.reducer';
import { IFacture } from 'app/shared/model/facture.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IFactureUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IFactureUpdateState {
  isNew: boolean;
  idscorrection: any[];
  sasisseurId: string;
  verificateurId: string;
}

export class FactureUpdate extends React.Component<IFactureUpdateProps, IFactureUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      idscorrection: [],
      sasisseurId: '0',
      verificateurId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getUsers();
    this.props.getCorrections();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.lastModifiedAt = convertDateTimeToServer(values.lastModifiedAt);

    if (errors.length === 0) {
      const { factureEntity } = this.props;
      const entity = {
        ...factureEntity,
        ...values,
        corrections: mapIdList(values.corrections)
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/facture');
  };

  render() {
    const { factureEntity, users, corrections, loading, updating } = this.props;
    const { isNew } = this.state;

    const { image, imageContentType } = factureEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="invoiceProjectApp.facture.home.createOrEditLabel">
              <Translate contentKey="invoiceProjectApp.facture.home.createOrEditLabel">Create or edit a Facture</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : factureEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="facture-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="facture-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="etatLabel" for="facture-etat">
                    <Translate contentKey="invoiceProjectApp.facture.etat">Etat</Translate>
                  </Label>
                  <AvField id="facture-etat" type="text" name="etat" />
                </AvGroup>
                <AvGroup>
                  <Label id="typeLabel" for="facture-type">
                    <Translate contentKey="invoiceProjectApp.facture.type">Type</Translate>
                  </Label>
                  <AvField id="facture-type" type="text" name="type" />
                </AvGroup>
                <AvGroup>
                  <Label id="createdAtLabel" for="facture-createdAt">
                    <Translate contentKey="invoiceProjectApp.facture.createdAt">Created At</Translate>
                  </Label>
                  <AvInput
                    id="facture-createdAt"
                    type="datetime-local"
                    className="form-control"
                    name="createdAt"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.factureEntity.createdAt)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="lastModifiedAtLabel" for="facture-lastModifiedAt">
                    <Translate contentKey="invoiceProjectApp.facture.lastModifiedAt">Last Modified At</Translate>
                  </Label>
                  <AvInput
                    id="facture-lastModifiedAt"
                    type="datetime-local"
                    className="form-control"
                    name="lastModifiedAt"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.factureEntity.lastModifiedAt)}
                  />
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
                <AvGroup>
                  <AvGroup>
                    <Label id="imageLabel" for="image">
                      <Translate contentKey="invoiceProjectApp.facture.image">Image</Translate>
                    </Label>
                    <br />
                    {image ? (
                      <div>
                        <a onClick={openFile(imageContentType, image)}>
                          <Translate contentKey="entity.action.open">Open</Translate>
                        </a>
                        <br />
                        <Row>
                          <Col md="11">
                            <span>
                              {imageContentType}, {byteSize(image)}
                            </span>
                          </Col>
                          <Col md="1">
                            <Button color="danger" onClick={this.clearBlob('image')}>
                              <FontAwesomeIcon icon="times-circle" />
                            </Button>
                          </Col>
                        </Row>
                      </div>
                    ) : null}
                    <input id="file_image" type="file" onChange={this.onBlobChange(false, 'image')} />
                    <AvInput type="hidden" name="image" value={image} />
                  </AvGroup>
                </AvGroup>
                <AvGroup>
                  <Label for="facture-sasisseur">
                    <Translate contentKey="invoiceProjectApp.facture.sasisseur">Sasisseur</Translate>
                  </Label>
                  <AvInput id="facture-sasisseur" type="select" className="form-control" name="sasisseurId">
                    <option value="" key="0" />
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.login}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="facture-verificateur">
                    <Translate contentKey="invoiceProjectApp.facture.verificateur">Verificateur</Translate>
                  </Label>
                  <AvInput id="facture-verificateur" type="select" className="form-control" name="verificateurId">
                    <option value="" key="0" />
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.login}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="facture-correction">
                    <Translate contentKey="invoiceProjectApp.facture.correction">Correction</Translate>
                  </Label>
                  <AvInput
                    id="facture-correction"
                    type="select"
                    multiple
                    className="form-control"
                    name="corrections"
                    value={factureEntity.corrections && factureEntity.corrections.map(e => e.id)}
                  >
                    <option value="" key="0" />
                    {corrections
                      ? corrections.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/facture" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  users: storeState.userManagement.users,
  corrections: storeState.correction.entities,
  factureEntity: storeState.facture.entity,
  loading: storeState.facture.loading,
  updating: storeState.facture.updating,
  updateSuccess: storeState.facture.updateSuccess
});

const mapDispatchToProps = {
  getUsers,
  getCorrections,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FactureUpdate);
