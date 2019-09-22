import React from 'react';
import { Translate, translate } from 'react-jhipster';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter, Table, Label, Alert, Row, Col } from 'reactstrap';
import { AvForm, AvField, AvGroup, AvInput } from 'availity-reactstrap-validation';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export interface ICorrectionProps {
  showModal: boolean;
  handleClose: Function;
  corrections;
}

class CorrectionModal extends React.Component<ICorrectionProps> {
  render() {
    const { handleClose } = this.props;

    return (
      <Modal isOpen={this.props.showModal} toggle={handleClose} backdrop="static" id="login-page" autoFocus={false}>
        <ModalHeader id="login-title" toggle={handleClose}>
          <span>Corrections</span>
        </ModalHeader>
        <ModalBody>
          <div className="mt-1">&nbsp;</div>
          <Table responsive>
            <thead>
              <tr>
                <th className="hand">
                  <Translate contentKey="invoiceProjectApp.correction.champ">Champ</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand">
                  <Translate contentKey="invoiceProjectApp.correction.oldValue">Ancien valeur</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand">
                  <Translate contentKey="invoiceProjectApp.correction.newValue">Nouvelle valeur</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="invoiceProjectApp.correction.facture">Facture</Translate> <FontAwesomeIcon icon="sort" />
                </th>
              </tr>
            </thead>
            <tbody>
              {this.props.corrections.map((correction, i) => (
                <tr key={`entity-${i}`}>
                  <td>{correction.champ}</td>
                  <td>{correction.oldValue}</td>
                  <td>{correction.newValue}</td>
                  <td>{correction.factureId ? <Link to={`facture/${correction.factureId}`}>{correction.factureId}</Link> : ''}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        </ModalBody>
        <ModalFooter> </ModalFooter>
      </Modal>
    );
  }
}

export default CorrectionModal;
