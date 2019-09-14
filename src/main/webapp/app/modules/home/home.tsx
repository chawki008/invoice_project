import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { Row, Alert } from 'reactstrap';

import SaisseurHome from './sasisseurHome/saisisseurHome';

export type IHomeProp = StateProps;

export const Home = (props: IHomeProp) => {
  const { account } = props;
  let render = (
    <div>
      <Alert color="warning">
        <Translate contentKey="global.messages.info.authenticated.prefix">If you want to </Translate>
        <Link to="/login" className="alert-link">
          <Translate contentKey="global.messages.info.authenticated.link"> sign in</Translate>
        </Link>
      </Alert>
    </div>
  );
  if (account && account.login && account.authorities.includes('ROLE_SAISISSEUR')) {
    render = (
      <div>
        <SaisseurHome />
      </div>
    );
  } else if (account && account.login && account.authorities.includes('ROLE_VERIFICATEUR')) {
    render = <div>Verificateur</div>;
  }
  return <Row>{render}</Row>;
};

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated
});

type StateProps = ReturnType<typeof mapStateToProps>;

export default connect(mapStateToProps)(Home);
