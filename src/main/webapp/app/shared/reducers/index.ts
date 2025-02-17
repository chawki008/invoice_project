import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
import sessions, { SessionsState } from 'app/modules/account/sessions/sessions.reducer';
import calendar, { CalendarState } from 'app/modules/calendar/calendar-reducer';
// prettier-ignore
import facture, {
  FactureState
} from 'app/entities/facture/facture.reducer';
// prettier-ignore
import correction, {
  CorrectionState
} from 'app/entities/correction/correction.reducer';
// prettier-ignore
import tempsTravail, {
  TempsTravailState
} from 'app/entities/temps-travail/temps-travail.reducer';
import sasisseurHome, { SaisisseurHomeState } from 'app/modules/home/sasisseurHome/sasisseurHome.reducer';
import verificateurHome, { VerificateurHomeState } from 'app/modules/home/verificateurHome/verificateurHome.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly locale: LocaleState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly sessions: SessionsState;
  readonly facture: FactureState;
  readonly correction: CorrectionState;
  readonly tempsTravail: TempsTravailState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
  readonly calendar: CalendarState;
  readonly sasisseurHome: SaisisseurHomeState;
  readonly verificateurHome: VerificateurHomeState;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  sessions,
  facture,
  correction,
  tempsTravail,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
  calendar,
  sasisseurHome,
  verificateurHome
});

export default rootReducer;
