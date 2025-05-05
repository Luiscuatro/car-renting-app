import { useState } from 'react';
import { UserEndpoint } from 'Frontend/generated/endpoints.js';
import  User  from 'Frontend/generated/com/example/application/model/User';

export default function CreateUserView() {
  const [userId, setUserId] = useState('');
  const [fullName, setFullName] = useState('');

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
      bookings: [],
    };

    await UserEndpoint.saveUser(user);
    alert('Usuario guardado');
  };

  return (
    <div>
      <h2>Crear usuario</h2>
      <input placeholder="User ID" value={userId} onChange={e => setUserId(e.target.value)} />
      <input placeholder="Nombre completo" value={fullName} onChange={e => setFullName(e.target.value)} />
      <button onClick={handleSave}>Guardar</button>
    </div>
  );
}
