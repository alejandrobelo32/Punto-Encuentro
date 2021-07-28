<?php

include '../conexion.php';
$usuario = $_GET['usuario'];
$contrasena = md5($_GET['contrasena']);

$consultaUsuarios="SELECT * FROM usuarios WHERE mail = '$usuario' AND contrasena = '$contrasena'";
$usuarios = $conexion -> query($consultaUsuarios);

while ($fila=$usuarios -> fetch_array()){

	$producto[]= array_map('utf8_encode', $fila);
	
}

echo json_encode($producto);
$usuarios -> close();

?>