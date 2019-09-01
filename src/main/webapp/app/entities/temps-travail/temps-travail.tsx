import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './temps-travail.reducer';
import { ITempsTravail } from 'app/shared/model/temps-travail.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITempsTravailProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class TempsTravail extends React.Component<ITempsTravailProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { tempsTravailList, match } = this.props;
    return (
      <div>
        <h2 id="temps-travail-heading">
          <Translate contentKey="invoiceProjectApp.tempsTravail.home.title">Temps Travails</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="invoiceProjectApp.tempsTravail.home.createLabel">Create a new Temps Travail</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {tempsTravailList && tempsTravailList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="invoiceProjectApp.tempsTravail.startDate">Start Date</Translate>
                  </th>
                  <th>
                    <Translate contentKey="invoiceProjectApp.tempsTravail.endDate">End Date</Translate>
                  </th>
                  <th>
                    <Translate contentKey="invoiceProjectApp.tempsTravail.user">User</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {tempsTravailList.map((tempsTravail, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${tempsTravail.id}`} color="link" size="sm">
                        {tempsTravail.id}
                      </Button>
                    </td>
                    <td>
                      <TextFormat type="date" value={tempsTravail.startDate} format={APP_DATE_FORMAT} />
                    </td>
                    <td>
                      <TextFormat type="date" value={tempsTravail.endDate} format={APP_DATE_FORMAT} />
                    </td>
                    <td>{tempsTravail.userId ? tempsTravail.userId : ''}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${tempsTravail.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${tempsTravail.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${tempsTravail.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">
              <Translate contentKey="invoiceProjectApp.tempsTravail.home.notFound">No Temps Travails found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ tempsTravail }: IRootState) => ({
  tempsTravailList: tempsTravail.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TempsTravail);
