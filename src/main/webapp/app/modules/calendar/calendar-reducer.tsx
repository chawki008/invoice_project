import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

export const ACTION_TYPES = {
  FETCH_FACTURE_BY_DATE: 'calendar/FETCH_FACTURE_BY_DATE',
  UPDATE_CALENDAR_USER: 'calendar/UPDATE_CALENDAR_USER'
};

const initialState = {
  loading: false,
  errorMessage: null,
  events: [
    {
      id: 0,
      title: 'initial event',
      allDay: false,
      start: new Date('2019-09-07T03:24:00'),
      end: new Date('2019-09-07T06:24:00')
    }
  ],
  entity: {},
  updating: false,
  totalItems: 0,
  updateSuccess: false,
  userId: ''
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
        events: getCountFacturesEvents(action.payload.data)
      };
    case ACTION_TYPES.UPDATE_CALENDAR_USER:
      return {
        ...state,
        userId: action.payload
      };
    default:
      return state;
  }
};

const getCountFacturesEvents = data => Object.keys(data).map(mapFactureCountToCalendarEvents(data));
const mapFactureCountToCalendarEvents = data => date => {
  return {
    id: 0,
    title: `Factures effectués: ${data[date]}`,
    allDay: true,
    start: new Date(`${date}`),
    end: new Date(`${date}`)
  };
};
const apiUrl = 'api/factures/countByDate';

// Actions

export const getFacturesByDate = (userId, from, to) => {
  const requestUrl = `${apiUrl}`;
  return {
    type: ACTION_TYPES.FETCH_FACTURE_BY_DATE,
    payload: axios.get(
      `${requestUrl}?lastModifiedAt.greaterThan=${from.getFullYear()}-${('0' + (from.getMonth() + 1)).slice(-2)}-${(
        '0' + from.getDate()
      ).slice(-2)}T03%3A24%3A00Z&lastModifiedAt.lessThan=${to.getFullYear()}-${('0' + (to.getMonth() + 1)).slice(-2)}-${(
        '0' + to.getDate()
      ).slice(-2)}T23%3A24%3A00Z&sasisseurId.equals=${userId}`
    )
  };
};

export const updateCalendarUser = userId => {
  return {
    type: ACTION_TYPES.UPDATE_CALENDAR_USER,
    payload: userId
  };
};
