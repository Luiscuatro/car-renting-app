import { useState } from 'react';
import { TextField, Checkbox, FormControlLabel, Button } from '@mui/material';
import { UserEndpoint } from 'Frontend/generated/endpoints';

export default function UserRegisterView() {
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [licenseNumber, setLicenseNumber] = useState('');
  const [admin, setAdmin] = useState(false);
  const [status, setStatus] = useState<string | null>(null);

  const handleRegister = async () => {
    if (!licenseNumber || !fullName || !email || !phoneNumber) {
      setStatus('Por favor, completa todos los campos obligatorios.');
      return;
    }

    const user = {
      userId: `USER#${licenseNumber}`,
      operation: 'profile',
      fullName,
      email,
      phoneNumber,
      licenseNumber,
      admin
    };

    try {
      await UserEndpoint.saveUser(user);
      setStatus('Usuario registrado con éxito');
    } catch (error) {
      console.error(error);
      setStatus('Error al registrar el usuario');
    }
  };

  return (
    <div className="p-6 max-w-lg mx-auto">
      <h2 className="text-2xl font-bold mb-4">Registro de Usuario</h2>

      <TextField
        label="Nombre completo"
        value={fullName}
        onChange={(e) => setFullName(e.target.value)}
        fullWidth
        className="mb-4"
      />
      <TextField
        label="Correo electrónico"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        fullWidth
        className="mb-4"
      />
      <TextField
        label="Teléfono"
        value={phoneNumber}
        onChange={(e) => setPhoneNumber(e.target.value)}
        fullWidth
        className="mb-4"
      />
      <TextField
        label="Número de licencia"
        value={licenseNumber}
        onChange={(e) => setLicenseNumber(e.target.value)}
        fullWidth
        className="mb-4"
      />
      <FormControlLabel
        control={
          <Checkbox
            checked={admin}
            onChange={(e) => setAdmin(e.target.checked)}
          />
        }
        label="¿Es administrador?"
      />

      <Button
        variant="contained"
        color="primary"
        onClick={handleRegister}
        className="mt-4"
      >
        Registrar Usuario
      </Button>

      {status && <p className="mt-4 text-sm text-gray-700">{status}</p>}
    </div>
  );
}
