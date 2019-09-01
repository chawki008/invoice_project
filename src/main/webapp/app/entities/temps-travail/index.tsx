import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TempsTravail from './temps-travail';
import TempsTravailDetail from './temps-travail-detail';
import TempsTravailUpdate from './temps-travail-update';
import TempsTravailDeleteDialog from './temps-travail-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TempsTravailUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TempsTravailUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TempsTravailDetail} />
      <ErrorBoundaryRoute path={match.url} component={TempsTravail} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={TempsTravailDeleteDialog} />
  </>
);

export default Routes;
