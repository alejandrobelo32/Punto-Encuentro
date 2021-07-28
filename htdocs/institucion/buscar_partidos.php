<?php

include '../conexion.php';
$nombre = $_GET['nombreProvincia'];

$consultaPaises="SELECT PA.nombre, PR.id_provincia FROM provincias PR, partidos PA WHERE PR.nombre = '$nombre' and PA.id_provincia = PR.id_provincia";
$resultado = $conexion -> query($consultaPaises);

while ($fila=$resultado -> fetch_array()){

	$paises[]= array_map('utf8_encode', $fila);
	
}

echo json_encode($paises);
$resultado -> close();

?>