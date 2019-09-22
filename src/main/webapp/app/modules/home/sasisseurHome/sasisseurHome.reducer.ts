import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { IFacture, defaultValue } from 'app/shared/model/facture.model';
import { ICorrection } from 'app/shared/model/correction.model';
import axios from 'axios';
import { cleanEntity } from 'app/shared/util/entity-utils';

export type SaisisseurHomeState = Readonly<typeof initialState>;

// Reducer
const initialState = {
  loading: false,
  errorMessage: null,
  facture: defaultValue,
  updating: false,
  updateSuccess: false,
  showModal: false,
  corrections: [],
  polling: true
};

export const ACTION_TYPES = {
  FETCH_FACTURE_VIDE: 'sasisseurHome/FETCH_FACTURE_VIDE',
  UPDATE_FACTURE_SAISISSEUR: 'sasisseurHome/UPDATE_FACTURE_SAISISSEUR',
  CLOSE_MODAL: 'sasisseurHome/CLOSE_MODAL',
  FETCH_FACTURE_EN_TRAIN_DE_SAISIE: 'sasisseurHome/FETCH_FACTURE_EN_TRAIN_DE_SAISIE',
  FETCH_CORRECTIONS: 'sasisseurHome/FETCH_CORRECTIONS'
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
    case FAILURE(ACTION_TYPES.FETCH_FACTURE_VIDE):
      return {
        ...state,
        loading: false,
        facture: defaultValue
      };
    case REQUEST(ACTION_TYPES.FETCH_FACTURE_EN_TRAIN_DE_SAISIE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case SUCCESS(ACTION_TYPES.FETCH_FACTURE_EN_TRAIN_DE_SAISIE):
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
    case ACTION_TYPES.CLOSE_MODAL:
      return {
        ...state,
        showModal: false,
        polling: true
      };
    case SUCCESS(ACTION_TYPES.FETCH_CORRECTIONS): {
      if (action.payload.data.length != 0)
        action.payload.data.forEach(correction => {
          correction.etat = 'VUE';
          axios.put(apiUrlCorrection, cleanEntity(correction));
        });
      return {
        ...state,
        corrections: action.payload.data,
        showModal: action.payload.data.length != 0,
        polling: action.payload.data.length == 0
      };
    }
    default:
      return state;
  }
};
const apiUrlFacture = 'api/factures';
const apiUrlCorrection = 'api/corrections';

export const getFactureVide: ICrudGetAllAction<IFacture> = () => {
  const requestUrl = `${apiUrlFacture}/vide`;
  return {
    type: ACTION_TYPES.FETCH_FACTURE_VIDE,
    payload: axios.get<IFacture>(requestUrl)
  };
};

export const getFactureEnTrainDeSaisie = userId => async dispatch => {
  const requestUrl = `${apiUrlFacture}/en_train_de_saisie/${userId}`;
  const payload = axios.get<IFacture>(requestUrl);
  payload.catch(function(error) {
    if (error.response.status == 404) dispatch(getFactureVide());
  });

  const result = await dispatch({
    type: ACTION_TYPES.FETCH_FACTURE_EN_TRAIN_DE_SAISIE,
    payload: axios.get<IFacture>(requestUrl)
  });
  return result;
};

export const getCorrectionsBySasisseur: ICrudGetAllAction<ICorrection> = id => {
  const requestUrl = `${apiUrlCorrection}?sasisseurId.equals=${id}&etat.equals=PAS_VUE`;
  return {
    type: ACTION_TYPES.FETCH_CORRECTIONS,
    payload: axios.get<ICorrection>(requestUrl)
  };
};

export const updateEntity: ICrudPutAction<IFacture> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FACTURE_SAISISSEUR,
    payload: axios.put(apiUrlFacture, cleanEntity(entity))
  });
  dispatch(getFactureVide());
  return result;
};

export const closeModal = () => {
  return {
    type: ACTION_TYPES.CLOSE_MODAL
  };
};
