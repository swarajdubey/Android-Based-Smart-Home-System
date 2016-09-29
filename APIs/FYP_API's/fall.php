<?php

// array for JSON response
require_once __DIR__ . '/db_connect.php';
$response = array();
echo "a";
// check for required fields
if ( isset($_POST['stat']) ) {
    echo "b";
    $status = $_POST['stat'];
    //$heart_rate=$_POST['heart_mtr'];

    echo "status is $status";
    $mac_addr="AA:11";

    // include db connect class
    echo "c";
    // connecting to db
    $db = new DB_CONNECT();
    echo "reached here";
    // mysql update row with matched pid
    $result = mysql_query("UPDATE fall_detection SET status_person = '$status' WHERE mac_address = '$mac_addr' ");

    // check if row inserted or not
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "successfully updated.";

        // echoing JSON response
        echo json_encode($response);
    } else {
       echo "wrong mysql query";
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>
