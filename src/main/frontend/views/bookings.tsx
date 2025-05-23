import { useEffect, useState } from 'react';
import { TextField } from '@mui/material';
import { UserEndpoint, DelegationEndpoint } from 'Frontend/generated/endpoints';
import Button from "@mui/material/Button";

export default function BookingForm() {
  const [delegations, setDelegations] = useState<any[]>([]);
  const [delegationId, setDelegationId] = useState('');
  const [plateNumber, setPlateNumber] = useState('');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [userId, setUserId] = useState('');
  const [status, setStatus] = useState<string | null>(null);

  useEffect(() => {
    DelegationEndpoint.getAllDelegations().then(result => {
      setDelegations(result ?? []);
    });
  }, []);

  const getDateRange = (start: string, end: string): string[] => {
    const dates: string[] = [];
    let current = new Date(start);
    const final = new Date(end);
    while (current <= final) {
      dates.push(current.toISOString().split('T')[0]);
      current.setDate(current.getDate() + 1);
    }
    return dates;
  };

  const handleBooking = async () => {
    if (!userId || !delegationId || !plateNumber || !startDate || !endDate) {
      setStatus('Por favor completa todos los campos.');
      return;
    }

    const booking = {
      userId: `USER#${userId}`,
      operation: `BOOKING#${new Date().getFullYear()}#${Date.now()}`,
      delegationId: delegationId,
      plateNumber: plateNumber,
      bookedDates: getDateRange(startDate, endDate),
      price: 0,
      status: 'pending'
    };

    try {
      await UserEndpoint.saveBooking(booking);
      setStatus('Reserva creada con éxito.');
    } catch (error) {
      console.error(error);
      setStatus('Error al crear la reserva.');
    }
  };

  return (
    <div className="p-6 max-w-xl mx-auto">
      <h2 className="text-xl font-bold mb-4">Reservar coche</h2>

      <TextField
        label="ID de usuario"
        fullWidth
        className="mb-4"
        value={userId}
        onChange={(e) => setUserId(e.target.value)}
      />

      <label className="block mb-2 font-medium">Delegación</label>
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

      <TextField
        label="Matrícula del coche"
        fullWidth
        className="mb-4"
        value={plateNumber}
        onChange={(e) => setPlateNumber(e.target.value)}
      />
      <TextField
        label="Fecha inicio (YYYY-MM-DD)"
        fullWidth
        className="mb-4"
        value={startDate}
        onChange={(e) => setStartDate(e.target.value)}
      />
      <TextField
        label="Fecha fin (YYYY-MM-DD)"
        fullWidth
        className="mb-4"
        value={endDate}
        onChange={(e) => setEndDate(e.target.value)}
      />

      <Button onClick={handleBooking} className="mt-2">Reservar</Button>
      {status && <p className="mt-4 text-sm text-gray-700">{status}</p>}
    </div>
  );
}
