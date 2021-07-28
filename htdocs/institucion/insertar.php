<?php

include '../conexion.php';

$usuario = $_POST['usuario'];
$nombre = $_POST['nombre'];
$cuit = $_POST['cuit'];
$password = md5($_POST['password']);
//$id_direccion = $_POST['id_direccion'];

$consulta ="INSERT INTO instituciones (mail, nombre_inst, cuit, contrasena, codigo_recuperacion, qr)
            VALUES ('".$usuario."', '".$nombre."',  '".$cuit."', '".$password."', '".md5($usuario)."','".md5($cuit)."')";
mysqli_query($conexion, $consulta) or die ("Problemas al insertar".mysqli_error($conexion));
mysqli_close ($conexion);

?>
