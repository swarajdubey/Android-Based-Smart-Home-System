<?php

require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();

if(isset($_POST['temp'],$_POST['humid']))
{
	$temperature=$_POST['temp'];
	$humidity=$_POST['humid'];

	echo "temp is: ".$temperature."<br>";
	$query=mysql_query("INSERT INTO `sensor_data`(`Temperature`, `Humidity`, `time_stamp`, `date_of_update`) VALUES ($temperature,$humidity,CURTIME(),CURDATE())");

	if($query){echo "query ok"."<br>";}
	$response["message"] = "successfully updated.";
        echo json_encode($response); //return JSON string to the android device to complete the action
}

?>
