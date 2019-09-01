import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Facture from './facture';
import Correction from './correction';
import TempsTravail from './temps-travail';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/facture`} component={Facture} />
      <ErrorBoundaryRoute path={`${match.url}/correction`} component={Correction} />
      <ErrorBoundaryRoute path={`${match.url}/temps-travail`} component={TempsTravail} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
