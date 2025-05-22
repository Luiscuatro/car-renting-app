import { useEffect, useState } from 'react';
import { TextField } from '@mui/material';
import { DelegationEndpoint } from 'Frontend/generated/endpoints';

export default function CreateCarForm() {
  const [delegations, setDelegations] = useState<any[]>([]);
  const [delegationId, setDelegationId] = useState('');
  const [brand, setBrand] = useState('');
  const [model, setModel] = useState('');
  const [year, setYear] = useState('');
  const [plateNumber, setPlateNumber] = useState('');
  const [color, setColor] = useState('');
  const [type, setType] = useState('');
  const [status, setStatus] = useState<string | null>(null);

  useEffect(() => {
    DelegationEndpoint.getAllDelegations()
      .then((result) => setDelegations(result ?? []))
      .catch((error) => {
        console.error('Error al obtener delegaciones:', error);
        setDelegations([]);
      });
  }, []);

  const handleSubmit = async () => {
    try {
      await DelegationEndpoint.saveCar({
        plateNumber,
        brand,
        model,
        year,
        color,
        type
      }, delegationId);

      setStatus('Coche guardado correctamente.');
    } catch (error) {
      console.error(error);
      setStatus('Error al guardar el coche.');
    }
  };

  return (
    <div className="p-4 max-w-lg mx-auto">
      <h2 className="text-xl font-bold mb-4">Crear coche</h2>

      <label className="block mb-2 font-medium">Seleccionar delegación</label>
      <select
        className="w-full p-2 border mb-4 rounded"
        value={delegationId}
        onChange={(e) => setDelegationId(e.target.value)}
      >
        <option value="">-- Selecciona una delegación --</option>
        {delegations.map((d) => (
          <option key={d.delegationId} value={d.delegationId}>
            {d.name} – {d.city} ({d.delegationId})
          </option>
        ))}
      </select>

      <TextField label="Marca" fullWidth className="mb-4" value={brand} onChange={(e) => setBrand(e.target.value)} />
      <TextField label="Modelo" fullWidth className="mb-4" value={model} onChange={(e) => setModel(e.target.value)} />
      <TextField label="Año" fullWidth className="mb-4" value={year} onChange={(e) => setYear(e.target.value)} />
      <TextField label="Matrícula" fullWidth className="mb-4" value={plateNumber} onChange={(e) => setPlateNumber(e.target.value)} />
      <TextField label="Color" fullWidth className="mb-4" value={color} onChange={(e) => setColor(e.target.value)} />
      <TextField label="Tipo" fullWidth className="mb-4" value={type} onChange={(e) => setType(e.target.value)} />

      <button
        onClick={handleSubmit}
        className="mt-2 px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition"
      >
        Guardar coche
      </button>

      {status && <p className="mt-4 text-sm text-gray-700">{status}</p>}
    </div>
  );
}
