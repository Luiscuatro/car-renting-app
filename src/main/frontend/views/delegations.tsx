import { useState } from 'react';
import { DelegationEndpoint } from 'Frontend/generated/endpoints.js';
import Delegation from 'Frontend/generated/com/example/application/model/Delegation';
import Car from 'Frontend/generated/com/example/application/model/Car';

export default function CreateDelegationView() {
  const [delegationId, setDelegationId] = useState('');
  const [name, setName] = useState('');
  const [city, setCity] = useState('');
  const [address, setAddress] = useState('');

  const [cars, setCars] = useState<Car[]>([]);
  const [currentCar, setCurrentCar] = useState<Partial<Car>>({});

  const addCar = () => {
    if (!currentCar.carId) return;
    setCars([...cars, currentCar as Car]);
    setCurrentCar({});
  };

  const handleSave = async () => {
    const delegation: Delegation = {
      delegationId,
      operation: 'data',
      name,
      city,
      address,
      cars,
    };

    await DelegationEndpoint.saveDelegation(delegation);
    alert('Delegación guardada');
  };

  return (
    <div>
      <h2>Crear Delegación</h2>
      <input
        placeholder="Delegation ID"
        value={delegationId}
        onChange={e => setDelegationId(e.target.value)}
      />
      <input
        placeholder="Nombre"
        value={name}
        onChange={e => setName(e.target.value)}
      />
      <input
        placeholder="Ciudad"
        value={city}
        onChange={e => setCity(e.target.value)}
      />
      <input
        placeholder="Dirección"
        value={address}
        onChange={e => setAddress(e.target.value)}
      />

      <h3>Añadir coche</h3>
      <input
        placeholder="Car ID"
        value={currentCar.carId || ''}
        onChange={e => setCurrentCar({ ...currentCar, carId: e.target.value })}
      />
      <input
        placeholder="Marca"
        value={currentCar.brand || ''}
        onChange={e => setCurrentCar({ ...currentCar, brand: e.target.value })}
      />
      <input
        placeholder="Modelo"
        value={currentCar.model || ''}
        onChange={e => setCurrentCar({ ...currentCar, model: e.target.value })}
      />
      <input
        placeholder="Año"
        value={currentCar.year || ''}
        onChange={e => setCurrentCar({ ...currentCar, year: e.target.value })}
      />
      <input
        placeholder="Color"
        value={currentCar.color || ''}
        onChange={e => setCurrentCar({ ...currentCar, color: e.target.value })}
      />
      <input
        placeholder="Matrícula"
        value={currentCar.plateNumber || ''}
        onChange={e =>
          setCurrentCar({ ...currentCar, plateNumber: e.target.value })
        }
      />
      <input
        placeholder="Tipo (SUV, compacto...)"
        value={currentCar.type || ''}
        onChange={e => setCurrentCar({ ...currentCar, type: e.target.value })}
      />

      <button onClick={addCar}>Añadir coche</button>

      <h4>Coches añadidos:</h4>
      <ul>
        {cars.map((c, index) => (
          <li key={index}>
            {c.carId} - {c.brand} {c.model} ({c.plateNumber})
          </li>
        ))}
      </ul>

      <br />
      <button onClick={handleSave}>Guardar Delegación</button>
    </div>
  );
}
