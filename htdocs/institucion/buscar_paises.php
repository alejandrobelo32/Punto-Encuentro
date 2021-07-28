<?php

include '../conexion.php';

$consultaPaises="SELECT nombre FROM paises";
$resultado = $conexion -> query($consultaPaises);

while ($fila=$resultado -> fetch_array()){

	$paises[]= array_map('utf8_encode', $fila);
	
}

echo json_encode($paises);
$resultado -> close();

?>