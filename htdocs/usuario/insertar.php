<?php

include '../conexion.php';

$usuario = $_POST['usuario'];
$password = md5($_POST['password']);
$dni = $_POST['dni'];
$nombre = $_POST['nombre'];
$apellido = $_POST['apellido'];

$consulta = "INSERT INTO usuarios (mail, dni, nombre, apellido, contrasena, habilitado, codigo_recuperacion) VALUES ('".$usuario."',".$dni.",'".$nombre."','".$apellido."','".$password."',2,'abc')" ;
mysqli_query($conexion, $consulta) or die ("Problemas al insertar".mysqli_error($conexion));

mysqli_close ($conexion);
?>
