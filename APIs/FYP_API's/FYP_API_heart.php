<?php

require_once __DIR__ . '/db_connect.php';
$response = array();// array for JSON response
$db = new DB_CONNECT();// connecting to the database

if( isset($_POST['heart_mtr']) ) //if heart rate has been received with this key
{
    $heart_rate=$_POST['heart_mtr']; //heart rate of the person using the android device

    $result=mysql_query("INSERT INTO `heart_rate_values`(`heart_rate`) VALUES ($heart_rate)");

    $response["message"] = "successfully updated.";

    echo json_encode($response);//return JSON string
} 
?>
