<?php

require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();
date_default_timezone_set("Asia/Kuala_Lumpur");
$current_date=date("Y-m-d");
$response=array();
if(isset($_POST['stepupdate']))
{
	$newstepstaken=$_POST['stepupdate'];
	$get_current_steps=mysql_query("SELECT Steps FROM step_counter_monitor WHERE _Date='$current_date'");
	while($row=mysql_fetch_assoc($get_current_steps))
	{
		$stepssofar=$row['Steps'];
	}
	$newtotalsteps=$newstepstaken+$stepssofar;
	
	$insertnewstepcounter=mysql_query("UPDATE step_counter_monitor SET Steps='$newtotalsteps' WHERE	_Date='$current_date'");
	
	$response["message"] = "successfully updated.";

    	echo json_encode($response);//return JSON string	
}
?>
