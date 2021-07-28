<?php

include '../conexion.php';
$nombre = $_GET['nombrePais'];

$consultaPaises="SELECT PR.nombre, PR.id_provincia FROM provincias PR, paises PA WHERE PA.nombre = '$nombre' and PA.id_pais = PR.id_pais";
$resultado = $conexion -> query($consultaPaises);

while ($fila=$resultado -> fetch_array()){

	$paises[]= array_map('utf8_encode', $fila);
	
}

echo json_encode($paises);
$resultado -> close();

?>