<?php

require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();

date_default_timezone_set("Asia/Kuala_Lumpur");


$number_of_steps_query=mysql_query("SELECT Steps FROM step_counter_monitor ORDER BY ID DESC LIMIT 6");
$number_of_steps=0;
while($row=mysql_fetch_assoc($number_of_steps_query))
{
	$number_of_steps=$number_of_steps+$row['Steps'];
}

$average_steps=$number_of_steps/5; //average number of steps taken for 5 days


$response=array();
//advise user based on the average steps taken per day
if($average_steps>2000)
{
	//good!
	$message="Good progress!";
	$response["step_message"]=$message;
	$response["no_of_steps"]=$average_steps;
}

if($average_steps<2000 && $average_steps>1200)
{
	//good but try walking more
	$message="Good, try to walk more";
	$response["step_message"]=$message;
	$response["no_of_steps"]=$average_steps;
}

if($average_steps<1200)
{
	//please walk more
	$message="You need to walk more";
	$response["step_message"]=$message; 
	$response["no_of_steps"]=$average_steps; 
}

	echo json_encode($response);

	//http://link.springer.com/article/10.1186%2F1479-5868-8-80 link that specifies how many steps an old person should walk
?>
