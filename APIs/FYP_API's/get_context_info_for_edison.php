<?php 

require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();

$response=array();

$table_column_names=array("Walking","Resting","Sleeping"); //column names of the table in the context table

for($i=0;$i<count($table_column_names);$i++)
{
	$column_name=$table_column_names[$i];
	$find_activity_query=mysql_query("SELECT `$column_name` FROM context_table");
	while($row=mysql_fetch_assoc($find_activity_query))
	{
		$activity_status=array();
		$activity_status[$column_name]=$row[$column_name]; //gets status of activity
		if($activity_status[$column_name]==$column_name)
		{
			$activity=$activity_status[$column_name]; //this is the current activity under way
		}
	}
}

	
	//find out the latest temperature here
	$latest_temperature_query=mysql_query("SELECT temperature FROM sensor_data ORDER BY ID DESC LIMIT 1");
	while($row=mysql_fetch_assoc($latest_temperature_query))
	{
		$latest_temp_value=$row['temperature']; //gets the latest temperature
	}

	//find out the latest heart rate here
	$latest_heart_query=mysql_query("SELECT heart_rate FROM heart_rate_values ORDER BY ID DESC LIMIT 1");
	while($row=mysql_fetch_assoc($latest_heart_query))
	{
		$heart_rate=$row['heart_rate']; //gets the latest heart rate
	}


	//echo "activity is: ".$activity."<br>"." latest temp is: ".$latest_temp_value." latest heart rate is: ".$heart_rate."<br>";
	

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
		if(($heart_rate>=70 && $heart_rate<90) && $latest_temp_value>27)
		{
			$response["activity"]=$activity;
			$response["fan_action"]=1;
		}
	
		if(($heart_rate>=60 && $heart_rate<70) && $latest_temp_value<21)
		{
			$response["activity"]=$activity;
			$response["fan_action"]=0;
		}

		
	}

	
	echo json_encode($response);

?>
