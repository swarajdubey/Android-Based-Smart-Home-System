<?php

// array for JSON response
require_once __DIR__ . '/db_connect.php';
$response = array();

$mac_addr="AA:11";
// connecting to db
$db = new DB_CONNECT();

$quer=mysql_query("SELECT heart_rate FROM fall_detection WHERE mac_address='$mac_addr'");

while($row=mysql_fetch_assoc($quer))
{
	$response["heart_rate"]=$row["heart_rate"];
	
}
    // check if row inserted or not
    if ($quer) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "successfully received.";

        // echoing JSON response
        echo json_encode($response);
    } else {
       echo "wrong mysql query";
    }

?>
