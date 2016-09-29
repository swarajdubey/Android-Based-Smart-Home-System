<?php

require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();
date_default_timezone_set("Asia/Kuala_Lumpur");
$current_date=date("Y-m-d");
$response=array();
if(isset($_POST['stepkey']))//step counter data received
{
	$no_of_steps=$_POST['stepkey'];
	$updatequery=mysql_query("INSERT INTO step_counter_monitor(_Date,Steps) VALUES('$current_date','$no_of_steps')");

        $response["message"]="yes";

       echo json_encode($response);
}

?>

