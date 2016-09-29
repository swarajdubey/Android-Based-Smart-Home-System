<?php

require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();

date_default_timezone_set("Asia/Kuala_Lumpur");
$time = time();

$prev_date=date("Y-m-d", mktime(0,0,0,date("n", $time),date("j",$time)- 1 ,date("Y", $time))); //gets the previous date
$prev_date2=date("Y-m-d", mktime(0,0,0,date("n", $time),date("j",$time) -2 ,date("Y", $time))); //gets the date 2 days before

$response=array();

$get_temperature_values=mysql_query("SELECT Temperature FROM sensor_data WHERE (date_of_update LIKE '%$prev_date%') OR (date_of_update LIKE '%$prev_date2%')");

$total_temp_sum=0;
$counter=0;
while($row=mysql_fetch_assoc($get_temperature_values))
{
	$total_temp_sum=$total_temp_sum+$row['Temperature'];
	$counter=$counter+1;	
}

$avg_temp_value=$total_temp_sum/$counter;

$response["avg_temp"]=$avg_temp_value;

echo json_encode($response);

?>
