<?php

require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();
$response=array();
if(isset($_POST['help_msg']))
{
	$message=$_POST['help_msg'];
	$upload_message=mysql_query("UPDATE fall_detection SET status='$message'");
	$response["message"]="good";

	echo json_encode($response);

}
?>
