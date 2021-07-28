<?php

include '../conexion.php';

$pais = $_POST['pais'];
$provincia = $_POST['provincia'];
$partido = $_POST['partido'];
$localidad = $_POST['localidad'];
$calle = $_POST['calle'];
$altura = $_POST['altura'];
$usuario = $_POST['usuario'];

/* TRAIGO ID DE LOCALIDAD */
$consulta = "SELECT id_localidad FROM localidades WHERE nombre = '$localidad'";
$resultado = $conexion -> query($consulta);
while ($fila=$resultado -> fetch_array()){
	$id = $fila["id_localidad"];
}
/* FIN TRAIGO ID LOCALIDAD */


/* INSERTO EN DIRECCIONES */
$consulta = "INSERT INTO direcciones (calle,          altura,      id_localidad)
                              VALUES ('".$calle."' ,  '".$altura."', '".$id."')";
mysqli_query($conexion, $consulta) or die ("PROB".mysqli_error($conexion));
/* FIN INSERTO EN DIRECCIONES */


/* TRAIGO ID DE LA DIRECCION */
$consulta = "SELECT id_direcciones FROM direcciones WHERE calle = '$calle' AND altura = '$altura' AND id_localidad = '$id'";
$resultado = $conexion -> query($consulta);
while ($fila=$resultado -> fetch_array()){
	$id = $fila["id_direcciones"];
}
/* TRAIGO ID DE LA DIRECCION */


/* UPDATEO EN INSTITUCIONES */
$consulta = "UPDATE instituciones SET id_direccion = '".$id."' WHERE mail = '".$usuario."'";
mysqli_query($conexion, $consulta) or die ("PROB2".mysqli_error($conexion));
/* FIN UPDATEO EN INSTITUCIONES */

mysqli_close ($conexion);

?>
