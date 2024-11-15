import React, { useState } from 'react';
import './Modal.css';

function SubscribeModal({ onClose, onSubmit }) {
  const [percentage, setPercentage] = useState('');
  const [email, setEmail] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit({ percentage, email });
    onClose();
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h2>Subscribe for Alerts</h2>
        <form onSubmit={handleSubmit}>
        <div className="form-group">
            <div className="form-row">
                <label>
                    Change Threshold (%):
                </label>
                <input type="number" step={0.1} value={percentage} onChange={(e) => setPercentage(e.target.value)}
                    required/>
            </div>
          </div>
        <div className="form-group">
            <div className="form-row">
          <label>
            Email Address:
          </label>
          <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
            </div>
        </div>
          <div className="modal-buttons">
            <button type="submit">Subscribe</button>
            <button type="button" onClick={onClose}>Cancel</button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default SubscribeModal;