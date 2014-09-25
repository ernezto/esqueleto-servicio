# esqueleto-servicio

Base para arrancar un microservicio desde cero

## Instrucciones

Instalar [blackbox](https://github.com/StackExchange/blackbox) y asegurarse
que sus ejecutables estén en el PATH

````sh
which blackbox_initialize
```

Clonar este repositorio y entrar en su directorio

```sh
git clone https://github.com/Senescyt/esqueleto-servicio
cd esqueleto-servicio
```

Copiar en la raíz el archivo `sniese.keystore`

```sh
cp ../servicio-usuario/sniese.keystore .
```

Ejecutar la tarea de `gradle` para crear un nuevo microservicio

```sh
./gradlew -b crear.gradle -Pnombre=<nombre microservicio> -Ppuerto=<puerto inicial> -Pkpass=<contraseña del keystore>
```

Reemplazando:  
`<nombre microservicio>` por un nombre, por ejemplo `mascotas`  
`<puerto inicial>` por el número de puerto HTTP inicial, por ejemplo `8080`  
`<contraseña del keystore>` por la contraseña para el archivo
`sniese.keystore`, por ejemplo `superSecreto`  

Así:

```sh
./gradlew -b crear.gradle -Pnombre=mascotas -Ppuerto=8080 -Pkpass=superSecreto
```

Para este ejemplo se creará un nuevo directorio en `../servicio-mascotas`, con
todo lo necesario para poder empezar a usarlo y desarrollarlo

```sh
cd ../servicio-mascotas
./gradlew idea
./gradlew run
```
