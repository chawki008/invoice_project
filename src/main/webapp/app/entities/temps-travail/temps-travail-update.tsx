import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, reset } from './temps-travail.reducer';
import { ITempsTravail } from 'app/shared/model/temps-travail.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ITempsTravailUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ITempsTravailUpdateState {
  isNew: boolean;
  userId: string;
}

export class TempsTravailUpdate extends React.Component<ITempsTravailUpdateProps, ITempsTravailUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      userId: '0',
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
  }

  saveEntity = (event, errors, values) => {
    values.startDate = convertDateTimeToServer(values.startDate);
    values.endDate = convertDateTimeToServer(values.endDate);

    if (errors.length === 0) {
      const { tempsTravailEntity } = this.props;
      const entity = {
        ...tempsTravailEntity,
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
    this.props.history.push('/entity/temps-travail');
  };

  render() {
    const { tempsTravailEntity, users, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="invoiceProjectApp.tempsTravail.home.createOrEditLabel">
              <Translate contentKey="invoiceProjectApp.tempsTravail.home.createOrEditLabel">Create or edit a TempsTravail</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : tempsTravailEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="temps-travail-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="temps-travail-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="startDateLabel" for="temps-travail-startDate">
                    <Translate contentKey="invoiceProjectApp.tempsTravail.startDate">Start Date</Translate>
                  </Label>
                  <AvInput
                    id="temps-travail-startDate"
                    type="datetime-local"
                    className="form-control"
                    name="startDate"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.tempsTravailEntity.startDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="endDateLabel" for="temps-travail-endDate">
                    <Translate contentKey="invoiceProjectApp.tempsTravail.endDate">End Date</Translate>
                  </Label>
                  <AvInput
                    id="temps-travail-endDate"
                    type="datetime-local"
                    className="form-control"
                    name="endDate"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.tempsTravailEntity.endDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="temps-travail-user">
                    <Translate contentKey="invoiceProjectApp.tempsTravail.user">User</Translate>
                  </Label>
                  <AvInput id="temps-travail-user" type="select" className="form-control" name="userId">
                    <option value="" key="0" />
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/temps-travail" replace color="info">
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
  tempsTravailEntity: storeState.tempsTravail.entity,
  loading: storeState.tempsTravail.loading,
  updating: storeState.tempsTravail.updating,
  updateSuccess: storeState.tempsTravail.updateSuccess
});

const mapDispatchToProps = {
  getUsers,
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
)(TempsTravailUpdate);
