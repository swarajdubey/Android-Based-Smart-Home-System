<?php

require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();

$response=array();

if(isset($_POST['context_key'],$_POST['heart_value']))
{
	$activity=$_POST['context_key'];
	$heart_rate=$_POST['heart_value'];
	$update_context=mysql_query("UPDATE context_table SET Walking='no', Resting='no', Sleeping='no'");	
	$update_context=mysql_query("UPDATE context_table SET `$activity`='$activity'"); //only one activity can occur at a time


	//find out the temperature here
	$latest_temperature_query=mysql_query("SELECT temperature FROM sensor_data ORDER BY ID DESC LIMIT 1");
	while($row=mysql_fetch_assoc($latest_temperature_query))
	{
		$latest_temp_value=$row['temperature']; //gets the latest temperature
	}

	//implement the conditions here
	if($activity=="Walking") //all conditions for walking go in here
	{
		//check temperature and heart rate condition here
		if(($heart_rate>=70 && $heart_rate<90) && $latest_temp_value>27)
		{
			$response["activity"]=$activity;
			$response["fan_action"]=1;
		}

		if(($heart_rate>=60 && $heart_rate<70) && $latest_temp_value>21)
		{
			$response["activity"]=$activity;
			$response["fan_action"]=0;
		}

		if($heart_rate>=90 && $heart_rate<100)
		{
			$response["activity"]=$activity;
			$response["caution"]="high";
		}

		if($heart_rate>=100)
		{
			$response["activity"]=$activity;
			$response["caution"]="veryhigh";
		}

		if($heart_rate<40)
		{
			$response["activity"]=$activity;
			$response["caution"]="verylow";
		}
	}	

	if($activity=="Resting") //all conditions for walking go in here
	{
		//check temperature and heart rate condition here
		if($heart_rate>=90 && $heart_rate<100)
		{
			$response["activity"]=$activity;
			$response["caution"]="high";
		}

		if($heart_rate>=40 && $heart_rate<60)
		{
			$response["activity"]=$activity;
			$response["caution"]="low";
		}

		if($heart_rate<40)
		{
			$response["activity"]=$activity;
			$response["caution"]="verylow";
		}
	}

	if($activity=="Sleeping") //all conditions for walking go in here
	{
		//check temperature and heart rate condition here
	}

	
	echo json_encode($response);
}

?>
