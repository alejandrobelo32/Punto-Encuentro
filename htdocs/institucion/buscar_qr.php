<?php

include '../conexion.php';
$codigo_qr = $_GET['codigo_qr'];

$consultaInstitucion="SELECT * FROM instituciones WHERE codigo_qr = '$codigo_qr'";
$resultado = $conexion -> query($consultaInstitucion);

while ($fila=$resultado -> fetch_array()){

	$institucion[]= array_map('utf8_encode', $fila);
	
}

echo json_encode($institucion);
$resultado -> close();

?>