import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

export const ACTION_TYPES = {
  FETCH_FACTURE_BY_DATE: 'calendar/FETCH_FACTURE_BY_DATE',
  FETCH_TEMPSTRAVAIL: 'calendar/FETCH_TEMPSTRAVAIL',
  UPDATE_CALENDAR_USER: 'calendar/UPDATE_CALENDAR_USER'
};

const initialState = {
  loading: false,
  errorMessage: null,
  events: {
    facture: [],
    tempstravail: []
  },
  entity: {},
  updating: false,
  totalItems: 0,
  updateSuccess: false,
  user: {}
};

export type CalendarState = Readonly<typeof initialState>;

// Reducer

export default (state: CalendarState = initialState, action): CalendarState => {
  switch (action.type) {
    case FAILURE(ACTION_TYPES.FETCH_FACTURE_BY_DATE):
      return state;
    case SUCCESS(ACTION_TYPES.FETCH_FACTURE_BY_DATE):
      return {
        ...state,
        loading: false,
        events: { facture: getCountFacturesEvents(action.payload.data, state.user), tempstravail: state.events.tempstravail }
      };
    case FAILURE(ACTION_TYPES.FETCH_TEMPSTRAVAIL):
      return state;
    case SUCCESS(ACTION_TYPES.FETCH_TEMPSTRAVAIL):
      return {
        ...state,
        loading: false,
        events: { facture: state.events.facture, tempstravail: action.payload.data.map(mapTempsToCalendarEvents) }
      };
    case ACTION_TYPES.UPDATE_CALENDAR_USER:
      return {
        ...state,
        user: action.payload
      };
    default:
      return state;
  }
};

const getCountFacturesEvents = (data, user) => Object.keys(data).map(mapFactureCountToCalendarEvents(data, user));
const mapFactureCountToCalendarEvents = (data, user) => date => {
  let userAction = 'effectués';
  if (user.authorities.includes('ROLE_SAISISSEUR')) {
    userAction = 'saisies';
  } else if (user.authorities.includes('ROLE_VERIFICATEUR')) {
    userAction = 'verifiés';
  }
  return {
    id: 0,
    title: `Factures ${userAction}: ${data[date]}`,
    allDay: true,
    start: new Date(`${date}`),
    end: new Date(`${date}`)
  };
};

const mapTempsToCalendarEvents = tempsTravail => {
  return {
    id: 0,
    title: `Travaillé`,
    allDay: false,
    start: new Date(`${tempsTravail.startDate}`),
    end: new Date(`${tempsTravail.endDate}`)
  };
};
const apiUrl = 'api';

// Actions

export const getFacturesByDate = (user, from, to) => {
  let userCond = '';
  if (user.authorities.includes('ROLE_SAISISSEUR')) {
    userCond = `sasisseurId.equals=${user.id}`;
  } else if (user.authorities.includes('ROLE_VERIFICATEUR')) {
    userCond = `verificateurId.equals=${user.id}`;
  }

  const requestUrl = `${apiUrl}/factures/countByDate`;
  return {
    type: ACTION_TYPES.FETCH_FACTURE_BY_DATE,
    payload: axios.get(
      `${requestUrl}?lastModifiedAt.greaterThan=${from.getFullYear()}-${('0' + (from.getMonth() + 1)).slice(-2)}-${(
        '0' + from.getDate()
      ).slice(-2)}T03%3A24%3A00Z&lastModifiedAt.lessThan=${to.getFullYear()}-${('0' + (to.getMonth() + 1)).slice(-2)}-${(
        '0' + to.getDate()
      ).slice(-2)}T23%3A24%3A00Z&` + userCond
    )
  };
};

export const getTempsTravail = (userId, from, to) => {
  const requestUrl = `${apiUrl}/temps-travails`;
  return {
    type: ACTION_TYPES.FETCH_TEMPSTRAVAIL,
    payload: axios.get(
      `${requestUrl}?startDate.greaterThan=${from.getFullYear()}-${('0' + (from.getMonth() + 1)).slice(-2)}-${('0' + from.getDate()).slice(
        -2
      )}T03%3A24%3A00Z&startDate.lessThan=${to.getFullYear()}-${('0' + (to.getMonth() + 1)).slice(-2)}-${('0' + to.getDate()).slice(
        -2
      )}T23%3A24%3A00Z&userId.equals=${userId}`
    )
  };
};

export const updateCalendarUser = user => {
  return {
    type: ACTION_TYPES.UPDATE_CALENDAR_USER,
    payload: user
  };
};
