import { useState } from 'react';
import { UserEndpoint } from 'Frontend/generated/endpoints.js';
import User from 'Frontend/generated/com/example/application/model/User';
import Booking from 'Frontend/generated/com/example/application/model/Booking';
import { DatePicker } from '@vaadin/react-components/DatePicker.js';

export default function CreateUserView() {
  const [userId, setUserId] = useState('');
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [licenseNumber, setLicenseNumber] = useState('');
  const [admin, setAdmin] = useState(false);

  const [bookings, setBookings] = useState<Booking[]>([]);
  const [currentBooking, setCurrentBooking] = useState<Partial<Booking>>({});

  const addBooking = () => {
    if (!currentBooking.bookingId) return;
    setBookings([...bookings, currentBooking as Booking]);
    setCurrentBooking({});
  };

  const handleSave = async () => {
    const user: User = {
      pk: `USER#${userId}`,
      sk: 'PROFILE',
      userId,
      fullName,
      email,
      phoneNumber,
      licenseNumber,
      admin,
      bookings,
    };

    await UserEndpoint.saveUser(user);
    alert('Usuario guardado');
  };

  return (
    <div>
      <h2>Crear usuario</h2>
      <input placeholder="User ID" value={userId} onChange={e => setUserId(e.target.value)} />
      <input placeholder="Nombre completo" value={fullName} onChange={e => setFullName(e.target.value)} />
      <input placeholder="Email" value={email} onChange={e => setEmail(e.target.value)} />
      <input placeholder="Teléfono" value={phoneNumber} onChange={e => setPhoneNumber(e.target.value)} />
      <input placeholder="Licencia" value={licenseNumber} onChange={e => setLicenseNumber(e.target.value)} />
      <label>
        Admin:
        <input type="checkbox" checked={admin} onChange={e => setAdmin(e.target.checked)} />
      </label>
      <h2>Añadir Booking</h2>
      <input
        placeholder="Booking ID"
        value={currentBooking.bookingId || ''}
        onChange={e => setCurrentBooking({ ...currentBooking, bookingId: e.target.value })}
      />
      <input
        placeholder="Car ID"
        value={currentBooking.carId || ''}
        onChange={e => setCurrentBooking({ ...currentBooking, carId: e.target.value })}
      />
      <DatePicker
        label="Start Date"
        value={currentBooking.startDate || ''}
        onChange={e =>
          setCurrentBooking({ ...currentBooking, startDate: e.target.value })
        }
      />
      <DatePicker
        label="End Date"
        value={currentBooking.endDate || ''}
        onChange={e =>
          setCurrentBooking({ ...currentBooking, endDate: e.target.value })
        }
      />
      <input
        placeholder="Price"
        type="number"
        value={currentBooking.price || ''}
        onChange={e =>
          setCurrentBooking({
            ...currentBooking,
            price: parseFloat(e.target.value),
          })
        }
      />
      <input
        placeholder="Status"
        value={currentBooking.status || ''}
        onChange={e => setCurrentBooking({ ...currentBooking, status: e.target.value })}
      />
      <button onClick={addBooking}>Añadir Booking</button>

      <h4>Bookings añadidos:</h4>
      <ul>
        {bookings.map((b, index) => (
          <li key={index}>
            {b.bookingId} - {b.carId} ({b.startDate} → {b.endDate})
          </li>
        ))}
      </ul>

      <br />
      <button onClick={handleSave}>Guardar Usuario</button>
    </div>
  );
}
