import { REQUEST, SUCCESS } from 'app/shared/reducers/action-type.util';
import { ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { IFacture, defaultValue } from 'app/shared/model/facture.model';
import axios from 'axios';
import { cleanEntity } from 'app/shared/util/entity-utils';

export type SaisisseurHomeState = Readonly<typeof initialState>;

// Reducer
const initialState = {
  loading: false,
  errorMessage: null,
  facture: defaultValue,
  updating: false,
  updateSuccess: false
};

export const ACTION_TYPES = {
  FETCH_FACTURE_VIDE: 'facture/FETCH_FACTURE_VIDE',
  UPDATE_FACTURE_SAISISSEUR: 'facture/UPDATE_FACTURE_SAISISSEUR'
};

export default (state: SaisisseurHomeState = initialState, action): SaisisseurHomeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_FACTURE_VIDE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case SUCCESS(ACTION_TYPES.FETCH_FACTURE_VIDE):
      return {
        ...state,
        updating: false,
        loading: false,
        updateSuccess: true,
        facture: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.UPDATE_FACTURE_SAISISSEUR):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        facture: action.payload.data
      };
    default:
      return state;
  }
};
const apiUrl = 'api/factures';

export const getFactureVide: ICrudGetAllAction<IFacture> = () => {
  const requestUrl = `${apiUrl}/vide`;
  return {
    type: ACTION_TYPES.FETCH_FACTURE_VIDE,
    payload: axios.get<IFacture>(requestUrl)
  };
};

export const updateEntity: ICrudPutAction<IFacture> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FACTURE_SAISISSEUR,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getFactureVide());
  return result;
};
