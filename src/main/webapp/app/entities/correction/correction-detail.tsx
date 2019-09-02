import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './correction.reducer';
import { ICorrection } from 'app/shared/model/correction.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICorrectionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class CorrectionDetail extends React.Component<ICorrectionDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { correctionEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="invoiceProjectApp.correction.detail.title">Correction</Translate> [<b>{correctionEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="champ">
                <Translate contentKey="invoiceProjectApp.correction.champ">Champ</Translate>
              </span>
            </dt>
            <dd>{correctionEntity.champ}</dd>
            <dt>
              <Translate contentKey="invoiceProjectApp.correction.sasisseur">Sasisseur</Translate>
            </dt>
            <dd>{correctionEntity.sasisseurLogin ? correctionEntity.sasisseurLogin : ''}</dd>
            <dt>
              <Translate contentKey="invoiceProjectApp.correction.verificateur">Verificateur</Translate>
            </dt>
            <dd>{correctionEntity.verificateurLogin ? correctionEntity.verificateurLogin : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/correction" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/correction/${correctionEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ correction }: IRootState) => ({
  correctionEntity: correction.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CorrectionDetail);
