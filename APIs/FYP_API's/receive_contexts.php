<?php


require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();

$response=array();

$context_array=array("Sleeping","Resting","Walking");

for($i=0;$i<count($context_array);$i++)
{
	$contextType=$context_array[$i];
	$selectActivity=mysql_query("SELECT `$contextType` FROM context_table");
	while($row=mysql_fetch_assoc($selectActivity))
	{
		$status=array();
		$status[$contextType]=$row[$contextType];
		if($status[$contextType]==$contextType)
		{
			$response["activity"]=$contextType;
		}
	}
}

echo json_encode($response);

?>

