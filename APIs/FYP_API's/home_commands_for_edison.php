<?php

require_once __DIR__ . '/db_connect.php';

$db=new DB_CONNECT(); //connect to the database

$command_query=mysql_query("SELECT Commands,action_id,state FROM smart_home_commands");//mysql query

$commands["Commands_info"]=array(); //array of JSON string

while($row=mysql_fetch_array($command_query)) //loop through entire table
{
	$command_info=array();//array for JSON object
	$command_info["Commands"]=$row["Commands"]; //main object of the command
	$command_info["action_id"]=$row["action_id"];//id of the command
	$command_info["state"]=$row["state"]; //state of the command (true or false)
	array_push($commands["Commands_info"],$command_info); 
}

echo json_encode($commands); //returning the JSON string

?>
