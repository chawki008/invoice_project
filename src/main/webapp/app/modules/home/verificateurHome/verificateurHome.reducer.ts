import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { IFacture, defaultValue } from 'app/shared/model/facture.model';
import axios from 'axios';
import { cleanEntity } from 'app/shared/util/entity-utils';
import { ICorrection } from 'app/shared/model/correction.model';

export type VerificateurHomeState = Readonly<typeof initialState>;

// Reducer
const initialState = {
  loading: false,
  errorMessage: null,
  facture: defaultValue,
  updating: false,
  updateSuccess: false
};

export const ACTION_TYPES = {
  FETCH_FACTURE_SAISIE: 'facture/FETCH_FACTURE_SAISIE',
  UPDATE_FACTURE_VERIFICATEUR: 'facture/UPDATE_FACTURE_VERIFICATEUR',
  FETCH_FACTURE_EN_TRAIN_DE_VERIFIE: 'facture/FETCH_FACTURE_EN_TRAIN_DE_VERIFIE',
  CREATE_CORRECTION: 'facture/CREATE_CORRECTION'
};

export default (state: VerificateurHomeState = initialState, action): VerificateurHomeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_FACTURE_SAISIE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case SUCCESS(ACTION_TYPES.FETCH_FACTURE_SAISIE):
      return {
        ...state,
        updating: false,
        loading: false,
        updateSuccess: true,
        facture: action.payload.data
      };
    case REQUEST(ACTION_TYPES.FETCH_FACTURE_EN_TRAIN_DE_VERIFIE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case SUCCESS(ACTION_TYPES.FETCH_FACTURE_EN_TRAIN_DE_VERIFIE):
      return {
        ...state,
        updating: false,
        loading: false,
        facture: state.facture == defaultValue ? action.payload.data : state.facture
      };
    case FAILURE(ACTION_TYPES.FETCH_FACTURE_SAISIE):
      return {
        ...state,
        loading: false,
        facture: defaultValue
      };
    case SUCCESS(ACTION_TYPES.UPDATE_FACTURE_VERIFICATEUR):
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

export const getFactureSaisie: ICrudGetAllAction<IFacture> = () => {
  const requestUrl = `${apiUrl}/saisie`;
  return {
    type: ACTION_TYPES.FETCH_FACTURE_SAISIE,
    payload: axios.get<IFacture>(requestUrl)
  };
};

export const getFactureEnTrainDeVerifie = userId => async dispatch => {
  const requestUrl = `${apiUrl}/en_train_de_verifie/${userId}`;
  const payload = axios.get<IFacture>(requestUrl);
  payload.catch(function(error) {
    if (error.response.status == 404) dispatch(getFactureSaisie());
  });

  const result = await dispatch({
    type: ACTION_TYPES.FETCH_FACTURE_EN_TRAIN_DE_VERIFIE,
    payload: axios.get<IFacture>(requestUrl)
  });
  return result;
};

export const updateEntity: ICrudPutAction<IFacture> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FACTURE_VERIFICATEUR,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getFactureSaisie());
  return result;
};

export const createCorrection: ICrudPutAction<ICorrection> = correction => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CORRECTION,
    payload: axios.post('api/corrections', cleanEntity(correction))
  });
  return result;
};
