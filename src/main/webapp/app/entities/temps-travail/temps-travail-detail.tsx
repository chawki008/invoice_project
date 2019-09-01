import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './temps-travail.reducer';
import { ITempsTravail } from 'app/shared/model/temps-travail.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITempsTravailDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class TempsTravailDetail extends React.Component<ITempsTravailDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { tempsTravailEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="invoiceProjectApp.tempsTravail.detail.title">TempsTravail</Translate> [<b>{tempsTravailEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="startDate">
                <Translate contentKey="invoiceProjectApp.tempsTravail.startDate">Start Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={tempsTravailEntity.startDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="endDate">
                <Translate contentKey="invoiceProjectApp.tempsTravail.endDate">End Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={tempsTravailEntity.endDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="invoiceProjectApp.tempsTravail.user">User</Translate>
            </dt>
            <dd>{tempsTravailEntity.userId ? tempsTravailEntity.userId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/temps-travail" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/temps-travail/${tempsTravailEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ tempsTravail }: IRootState) => ({
  tempsTravailEntity: tempsTravail.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TempsTravailDetail);
