import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ITempsTravail, defaultValue } from 'app/shared/model/temps-travail.model';

export const ACTION_TYPES = {
  FETCH_TEMPSTRAVAIL_LIST: 'tempsTravail/FETCH_TEMPSTRAVAIL_LIST',
  FETCH_TEMPSTRAVAIL: 'tempsTravail/FETCH_TEMPSTRAVAIL',
  CREATE_TEMPSTRAVAIL: 'tempsTravail/CREATE_TEMPSTRAVAIL',
  UPDATE_TEMPSTRAVAIL: 'tempsTravail/UPDATE_TEMPSTRAVAIL',
  DELETE_TEMPSTRAVAIL: 'tempsTravail/DELETE_TEMPSTRAVAIL',
  RESET: 'tempsTravail/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITempsTravail>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type TempsTravailState = Readonly<typeof initialState>;

// Reducer

export default (state: TempsTravailState = initialState, action): TempsTravailState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_TEMPSTRAVAIL_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TEMPSTRAVAIL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_TEMPSTRAVAIL):
    case REQUEST(ACTION_TYPES.UPDATE_TEMPSTRAVAIL):
    case REQUEST(ACTION_TYPES.DELETE_TEMPSTRAVAIL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_TEMPSTRAVAIL_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TEMPSTRAVAIL):
    case FAILURE(ACTION_TYPES.CREATE_TEMPSTRAVAIL):
    case FAILURE(ACTION_TYPES.UPDATE_TEMPSTRAVAIL):
    case FAILURE(ACTION_TYPES.DELETE_TEMPSTRAVAIL):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_TEMPSTRAVAIL_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_TEMPSTRAVAIL):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_TEMPSTRAVAIL):
    case SUCCESS(ACTION_TYPES.UPDATE_TEMPSTRAVAIL):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_TEMPSTRAVAIL):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/temps-travails';

// Actions

export const getEntities: ICrudGetAllAction<ITempsTravail> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_TEMPSTRAVAIL_LIST,
  payload: axios.get<ITempsTravail>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ITempsTravail> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TEMPSTRAVAIL,
    payload: axios.get<ITempsTravail>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ITempsTravail> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TEMPSTRAVAIL,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ITempsTravail> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TEMPSTRAVAIL,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITempsTravail> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TEMPSTRAVAIL,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
