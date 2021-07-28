<?php

include '../conexion.php';

$id_institucion = $_POST['id_institucion'];
$id_usuario = $_POST['id_usuario'];

// $id_usuario=1;
// $id_institucion=1;

$consulta="INSERT INTO usuarios_instituciones (id_usuario,id_institucion) VALUES ('.$id_usuario.','.$id_institucion.')";

mysqli_query($conexion, $consulta) or die ("Problemas al insertar".mysqli_error($conexion));

$consulta = "UPDATE usuarios SET habilitado=1 WHERE id_usuario='$id_usuario'";

mysqli_query($conexion, $consulta) or die ("Problemas al insertar".mysqli_error($conexion));

mysqli_close ($conexion);

?>