<?php

require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();
$response=array();
$get_message=mysql_query("SELECT status FROM fall_detection");
while($row=mysql_fetch_assoc($get_message))
{
	$message=$row['status'];
}

$response["stat"]=$message;
echo json_encode($response);
?>
