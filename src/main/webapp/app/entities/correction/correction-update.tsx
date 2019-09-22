import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IFacture } from 'app/shared/model/facture.model';
import { getEntities as getFactures } from 'app/entities/facture/facture.reducer';
import { getEntity, updateEntity, createEntity, reset } from './correction.reducer';
import { ICorrection } from 'app/shared/model/correction.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICorrectionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ICorrectionUpdateState {
  isNew: boolean;
  sasisseurId: string;
  verificateurId: string;
  factureId: string;
}

export class CorrectionUpdate extends React.Component<ICorrectionUpdateProps, ICorrectionUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      sasisseurId: '0',
      verificateurId: '0',
      factureId: '0',
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
    this.props.getFactures();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { correctionEntity } = this.props;
      const entity = {
        ...correctionEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/correction');
  };

  render() {
    const { correctionEntity, users, factures, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="invoiceProjectApp.correction.home.createOrEditLabel">
              <Translate contentKey="invoiceProjectApp.correction.home.createOrEditLabel">Create or edit a Correction</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : correctionEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="correction-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="correction-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="champLabel" for="correction-champ">
                    <Translate contentKey="invoiceProjectApp.correction.champ">Champ</Translate>
                  </Label>
                  <AvField id="correction-champ" type="text" name="champ" />
                </AvGroup>
                <AvGroup>
                  <Label id="oldValueLabel" for="correction-oldValue">
                    <Translate contentKey="invoiceProjectApp.correction.oldValue">Old Value</Translate>
                  </Label>
                  <AvField id="correction-oldValue" type="text" name="oldValue" />
                </AvGroup>
                <AvGroup>
                  <Label id="newValueLabel" for="correction-newValue">
                    <Translate contentKey="invoiceProjectApp.correction.newValue">New Value</Translate>
                  </Label>
                  <AvField id="correction-newValue" type="text" name="newValue" />
                </AvGroup>
                <AvGroup>
                  <Label id="etatLabel" for="correction-etat">
                    <Translate contentKey="invoiceProjectApp.correction.etat">Etat</Translate>
                  </Label>
                  <AvField id="correction-etat" type="text" name="etat" />
                </AvGroup>
                <AvGroup>
                  <Label for="correction-sasisseur">
                    <Translate contentKey="invoiceProjectApp.correction.sasisseur">Sasisseur</Translate>
                  </Label>
                  <AvInput id="correction-sasisseur" type="select" className="form-control" name="sasisseurId">
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
                  <Label for="correction-verificateur">
                    <Translate contentKey="invoiceProjectApp.correction.verificateur">Verificateur</Translate>
                  </Label>
                  <AvInput id="correction-verificateur" type="select" className="form-control" name="verificateurId">
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
                  <Label for="correction-facture">
                    <Translate contentKey="invoiceProjectApp.correction.facture">Facture</Translate>
                  </Label>
                  <AvInput id="correction-facture" type="select" className="form-control" name="factureId">
                    <option value="" key="0" />
                    {factures
                      ? factures.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/correction" replace color="info">
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
  factures: storeState.facture.entities,
  correctionEntity: storeState.correction.entity,
  loading: storeState.correction.loading,
  updating: storeState.correction.updating,
  updateSuccess: storeState.correction.updateSuccess
});

const mapDispatchToProps = {
  getUsers,
  getFactures,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CorrectionUpdate);
