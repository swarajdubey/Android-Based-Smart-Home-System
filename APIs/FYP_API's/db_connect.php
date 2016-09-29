<?php

require_once __DIR__ . '/db_config.php';
class DB_CONNECT{

	function __construct() //constructor
	{
		$this->connect();
	}

	function __destruct()//destructor
	{
		$this->close();
	}

	function connect()
	{
		$con=mysql_connect(DB_SERVER,DB_USER,DB_PASS); //connect to the server
		$db=mysql_select_db(DB_DATABASE); //connect to the database
		return $con;
	}

	function close()
	{
		mysql_close(); //close connection
	}
}

?>
