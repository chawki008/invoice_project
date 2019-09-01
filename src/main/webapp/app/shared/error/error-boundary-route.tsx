import React from 'react';
import { Route, RouteProps } from 'react-router-dom';
import ErrorBoundary from 'app/shared/error/error-boundary';
import { sendActivity } from 'app/config/websocket-middleware';

export const ErrorBoundaryRoute = ({ component: Component, ...rest }: RouteProps) => {
  const encloseInErrorBoundary = props => {
    sendActivity();
    return (
      <ErrorBoundary>
        <Component {...props} />
      </ErrorBoundary>
    );
  };

  if (!Component) throw new Error(`A component needs to be specified for path ${(rest as any).path}`);

  return <Route {...rest} render={encloseInErrorBoundary} />;
};

export default ErrorBoundaryRoute;
