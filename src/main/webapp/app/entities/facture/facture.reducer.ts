import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IFacture, defaultValue } from 'app/shared/model/facture.model';

export const ACTION_TYPES = {
  FETCH_FACTURE_LIST: 'facture/FETCH_FACTURE_LIST',
  FETCH_FACTURE: 'facture/FETCH_FACTURE',
  CREATE_FACTURE: 'facture/CREATE_FACTURE',
  UPDATE_FACTURE: 'facture/UPDATE_FACTURE',
  DELETE_FACTURE: 'facture/DELETE_FACTURE',
  SET_BLOB: 'facture/SET_BLOB',
  RESET: 'facture/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFacture>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type FactureState = Readonly<typeof initialState>;

// Reducer

export default (state: FactureState = initialState, action): FactureState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_FACTURE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FACTURE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_FACTURE):
    case REQUEST(ACTION_TYPES.UPDATE_FACTURE):
    case REQUEST(ACTION_TYPES.DELETE_FACTURE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_FACTURE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FACTURE):
    case FAILURE(ACTION_TYPES.CREATE_FACTURE):
    case FAILURE(ACTION_TYPES.UPDATE_FACTURE):
    case FAILURE(ACTION_TYPES.DELETE_FACTURE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_FACTURE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_FACTURE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_FACTURE):
    case SUCCESS(ACTION_TYPES.UPDATE_FACTURE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_FACTURE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.SET_BLOB:
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType
        }
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/factures';

// Actions

export const getEntities: ICrudGetAllAction<IFacture> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_FACTURE_LIST,
    payload: axios.get<IFacture>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IFacture> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FACTURE,
    payload: axios.get<IFacture>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IFacture> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FACTURE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IFacture> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FACTURE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFacture> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FACTURE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType
  }
});

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
