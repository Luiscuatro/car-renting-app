import boto3
import json
import argparse
import os
from datetime import datetime, timedelta

def generar_fechas_disponibles(dias=180):
    hoy = datetime.now().date()
    return [(hoy + timedelta(days=i)).isoformat() for i in range(dias + 1)]

def importar_datos(nombre_tabla, archivo):
    if not os.path.exists(archivo):
        print(f"❌ Archivo '{archivo}' no encontrado.")
        return

    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(nombre_tabla)

    with open(archivo, 'r', encoding='utf-8') as f:
        for linea in f:
            if not linea.strip():
                continue

            item = json.loads(linea)
            op = item.get("operation", "")

            if op == "DATA":
                table.put_item(Item=item)
                print(f"🏢 Delegación insertada: {item.get('delegationId')}")

            elif op.startswith("CAR#"):
                table.put_item(Item=item)
                print(f"🚗 Coche insertado: {item.get('plateNumber')}")

                if 'delegationId' in item and 'plateNumber' in item:
                    calendar_item = {
                        'delegationId': item['delegationId'],
                        'operation': f"CALENDAR#CAR#{item['plateNumber']}",
                        'availableDates': generar_fechas_disponibles()
                    }
                    table.put_item(Item=calendar_item)
                    print(f"📅 Calendario generado para coche {item['plateNumber']}")
                else:
                    print(f"⚠️ Coche sin delegationId o plateNumber: {item}")

            else:
                print(f"❓ Operation desconocido: {op} → {item}")

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Importar delegaciones y coches a DynamoDB')
    parser.add_argument('--tabla', required=True, help='Nombre de la tabla DynamoDB')
    parser.add_argument('--archivo', required=True, help='Archivo .jsonl con datos')

    args = parser.parse_args()
    importar_datos(args.tabla, args.archivo)
