import { useEffect, useState } from 'react';
import { DelegationEndpoint } from 'Frontend/generated/endpoints';
import { TextField } from '@mui/material';
import Button from '@mui/material/Button';

export default function AvailableCarsView() {
  const [delegations, setDelegations] = useState<any[]>([]);
  const [delegationId, setDelegationId] = useState('');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [availableCars, setAvailableCars] = useState<any[]>([]);
  const [status, setStatus] = useState<string | null>(null);

  useEffect(() => {
    DelegationEndpoint.getAllDelegations()
      .then(result => setDelegations(result ?? []))
      .catch(() => setStatus('Error al cargar delegaciones'));
  }, []);

  const handleSearch = async () => {
    if (!delegationId || !startDate || !endDate) {
      setStatus('Por favor completa todos los campos.');
      return;
    }

    try {
      const cars = await DelegationEndpoint.getAvailableCars(delegationId, startDate, endDate);
      setAvailableCars(cars);
      setStatus(null);
    } catch (error) {
      console.error(error);
      setStatus('Error al buscar coches disponibles.');
    }
  };

  return (
    <div className="p-6 max-w-4xl mx-auto">
      <h2 className="text-2xl font-bold mb-4">Buscar coches disponibles</h2>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
        <div>
          <label className="block mb-1 font-semibold">Delegación</label>
          <select
            className="w-full border rounded px-3 py-2"
            value={delegationId}
            onChange={(e) => setDelegationId(e.target.value)}
          >
            <option value="">-- Selecciona una delegación --</option>
            {delegations.map((d) => (
              <option key={d.delegationId} value={d.delegationId}>
                {d.name} – {d.city}
              </option>
            ))}
          </select>
        </div>
        <TextField
          label="Fecha inicio"
          type="date"
          value={startDate}
          onChange={(e) => setStartDate(e.target.value)}
          InputLabelProps={{ shrink: true }}
        />
        <TextField
          label="Fecha fin"
          type="date"
          value={endDate}
          onChange={(e) => setEndDate(e.target.value)}
          InputLabelProps={{ shrink: true }}
        />
      </div>

      <Button variant="contained" onClick={handleSearch} className="mb-6">
        Buscar coches
      </Button>

      {status && <p className="text-red-600 mb-4">{status}</p>}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {availableCars.map((car) => (
          <div key={car.plateNumber} className="border rounded shadow p-4">
            <img
              src={car.imageUrl || 'https://via.placeholder.com/300x200.png?text=Imagen'}
              alt={car.model}
              className="w-full h-48 object-cover rounded mb-4"
            />
            <h3 className="font-bold text-lg mb-1">{car.brand} {car.model}</h3>
            <p>Año: {car.year}</p>
            <p>Color: {car.color}</p>
            <p>Matrícula: {car.plateNumber}</p>
            <p className="font-semibold text-green-600 mt-2">Precio estimado: {car.price || 'Consultar'}</p>
          </div>
        ))}
      </div>
    </div>
  );
}
