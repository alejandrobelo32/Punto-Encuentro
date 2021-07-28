<?php

include '../conexion.php';
$nombre = $_GET['nombrePartido'];

$consultaPaises="SELECT L.nombre, L.id_localidad FROM localidades L, partidos P WHERE P.nombre = '$nombre' and P.id_partido = L.id_partido";
$resultado = $conexion -> query($consultaPaises);

while ($fila=$resultado -> fetch_array()){

	$paises[]= array_map('utf8_encode', $fila);
	
}

echo json_encode($paises);
$resultado -> close();

?>