import { useState } from 'react';
import { UserEndpoint } from 'Frontend/generated/endpoints.js';
import User from 'Frontend/generated/com/example/application/model/User';
import Booking from 'Frontend/generated/com/example/application/model/Booking';

export default function CreateUserView() {
  const [userId, setUserId] = useState('');
  const [fullName, setFullName] = useState('');
  const [bookings, setBookings] = useState<Booking[]>([]);
  const [start, setStart] = useState('');
  const [end, setEnd] = useState('');
  const [carId, setCarId] = useState('');
  const [price, setPrice] = useState(0);
  const [status, setStatus] = useState('');

  const handleAddBooking = () => {
    const newBooking: Booking = {
      bookingId: crypto.randomUUID(),
      startDate: start,
      endDate: end,
      userId: userId,
      carId,
      price,
      status,
    };
    setBookings([...bookings, newBooking]);
  };

  const handleSave = async () => {
    const user: User = {
      pk: `USER#${userId}`,
      sk: 'PROFILE',
      userId,
      fullName,
      email: `${userId}@example.com`,
      phoneNumber: '600123456',
      licenseNumber: 'ABC123',
      admin: false,
      bookings: bookings,
    };

    await UserEndpoint.saveUser(user);
    alert('Usuario guardado');
  };

  return (
    <div style={{ padding: '1rem' }}>
      <h3>Crear usuario</h3>
      <input
        placeholder="User ID"
        value={userId}
        onChange={(e) => setUserId(e.target.value)}
      />
      <input
        placeholder="Nombre completo"
        value={fullName}
        onChange={(e) => setFullName(e.target.value)}
      />

      <hr />
      <h3>Agregar booking</h3>
      <input
        placeholder="Start Date"
        type="date"
        value={start}
        onChange={(e) => setStart(e.target.value)}
      />
      <input
        placeholder="End Date"
        type="date"
        value={end}
        onChange={(e) => setEnd(e.target.value)}
      />
      <input
        placeholder="Car ID"
        value={carId}
        onChange={(e) => setCarId(e.target.value)}
      />
      <input
        placeholder="Price"
        type="number"
        value={price}
        onChange={(e) => setPrice(parseInt(e.target.value))}
      />
      <input
        placeholder="Status"
        value={status}
        onChange={(e) => setStatus(e.target.value)}
      />
      <button onClick={handleAddBooking}>Agregar Booking</button>

      <ul>
        {bookings.map((b, i) => (
          <li key={i}>
            {b.bookingId} — {b.carId} — {b.startDate} → {b.endDate} — {b.status}
          </li>
        ))}
      </ul>

      <hr />
      <button onClick={handleSave}>Guardar usuario con bookings</button>
    </div>
  );
}
