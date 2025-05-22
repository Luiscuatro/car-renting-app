import { useState, useEffect } from 'react';
import { TextField } from '@mui/material';
import { DelegationEndpoint } from 'Frontend/generated/endpoints';

export default function DelegationsView() {
  const [delegationId, setDelegationId] = useState('');
  const [name, setName] = useState('');
  const [city, setCity] = useState('');
  const [address, setAddress] = useState('');
  const [status, setStatus] = useState<string | null>(null);
  const [delegations, setDelegations] = useState<any[]>([]);

  const handleSubmit = async () => {
    try {
      await DelegationEndpoint.saveDelegation({
        delegationId: `DEL#${delegationId.trim().toUpperCase()}`,
        operation: 'DATA',
        name,
        city,
        address
      });
      setStatus('Delegación guardada correctamente.');
      fetchDelegations();
    } catch (error) {
      setStatus('Error al guardar delegación.');
      console.error(error);
    }
  };

  const fetchDelegations = async () => {
    try {
      const result = await DelegationEndpoint.getAllDelegations();
      setDelegations(result ?? []);
    } catch (error) {
      console.error('Error al cargar delegaciones:', error);
    }
  };

  useEffect(() => {
    fetchDelegations();
  }, []);

  return (
    <div className="p-4 max-w-lg mx-auto">
      <h2 className="text-xl font-bold mb-4">Crear nueva delegación</h2>

      <TextField
        label="ID corta de la delegación (ej: BCN, ZAR, VEN)"
        fullWidth
        value={delegationId}
        onChange={(e) => setDelegationId(e.target.value)}
        className="mb-4"
      />
      <TextField
        label="Nombre"
        fullWidth
        value={name}
        onChange={(e) => setName(e.target.value)}
        className="mb-4"
      />
      <TextField
        label="Ciudad"
        fullWidth
        value={city}
        onChange={(e) => setCity(e.target.value)}
        className="mb-4"
      />
      <TextField
        label="Dirección"
        fullWidth
        value={address}
        onChange={(e) => setAddress(e.target.value)}
        className="mb-4"
      />

      <button
        onClick={handleSubmit}
        className="mt-2 px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 transition"
      >
        Guardar delegación
      </button>

      {status && <p className="mt-4 text-sm text-gray-700">{status}</p>}

      <div className="mt-8">
        <h3 className="text-lg font-semibold mb-2">Delegaciones existentes</h3>
        <ul className="list-disc list-inside">
          {delegations.map((d) => (
            <li key={d.delegationId}>{d.delegationId} - {d.name}, {d.city}</li>
          ))}
        </ul>
      </div>
    </div>
  );
}
