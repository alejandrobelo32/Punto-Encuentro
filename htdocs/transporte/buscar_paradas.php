<?php
//Set default_socket_timeout to 180 seconds.
ini_set('default_socket_timeout', 180);
//extiende tiempo maximo de ejecucion a 5 min
ini_set('max_execution_time', 300);

include '../conexion.php';

$diferencia="00:00:00";

$client_id ="ea03c5b9420346428319ee00e18430b4";

$client_secret="D07A7821d7384Bd1a39C98Dc34fEDD81";

$consultaParadas="SELECT id_parada FROM paradas WHERE TIMEDIFF(CURRENT_TIMESTAMP, ultima_actualizacion) > '$diferencia' LIMIT 10";

$paradas = $conexion -> query($consultaParadas);

while ($fila=$paradas -> fetch_array()){	

	$horariosParada = file_get_contents('https://apitransporte.buenosaires.gob.ar/colectivos/forecastGTFS?StopCode='.$fila['id_parada'].'&json=1&client_id='.$client_id.'&client_secret='.$client_secret.'');

	$consulta = "INSERT INTO log_consultas (id_parada, fecha_consulta, estado_consulta, respuesta_consulta) VALUES (".$fila['id_parada'].", CURRENT_TIMESTAMP, 1, ".$horariosParada.")";

	$conexion -> query($consulta);

	if ($horariosParada!="{}"){

		$paradaDisponible = "UPDATE paradas SET id_estado_parada = 1, ultima_actualizacion = CURRENT_TIMESTAMP WHERE id_parada = ".$fila['id_parada'].";" ;

		$conexion -> query($paradaDisponible);

		$array = json_decode($horariosParada, true);

		foreach ($array as $value) {

			$horarioParada= "INSERT INTO horarios_transporte (id_parada, linea, hora_arrivo) VALUES (".$fila['id_parada'].",".$value['route_short_name'].",'".$value['forecast_time']."');";
			
			$conexion -> query($horarioParada);

		}

	}else{

		$paradaNoDisponible = "UPDATE paradas SET id_estado_parada = 2, ultima_actualizacion = CURRENT_TIMESTAMP WHERE id_parada = ".$fila['id_parada'].";" ;
		$conexion -> query($paradaNoDisponible);

	}

}

$paradas -> close();

?>