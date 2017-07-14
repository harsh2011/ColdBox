<?php

//3-6-2017
use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require '../vendor/autoload.php';

define('hostname','mysql.hostinger.in');
define('user','u712147781_vacci');
define('password','12345678');
define('databaseName','u712147781_vacci');


$app = new \Slim\App;

$app->post('/updateuser',function(Request $request,Response $response){
	$connect = mysqli_connect(hostname,user,password,databaseName);

	$var =  $request->getParsedBody();

	$oldusername = $var["oldusername"];
	$username = $var["username"];
	$fullname = $var["fullname"];
	$birthdate= $var["birthdate"];

	
	$query = "UPDATE  u712147781_vacci.user SET  username =  '".$username."' , fullname = '".$fullname."' , birthdate = '".$birthdate."' WHERE  user.username =  '".$oldusername."'; ";

	$result = mysqli_query($connect,$query) or die(mysqli_error($connect));

	if($result ==1){
		$query = "SELECT * FROM  u712147781_vacci.user WHERE user.username = '$username' ";

		$result = mysqli_query($connect,$query) or die(mysqli_error($connect));
		$number_of_rows = mysqli_num_rows($result);

		$temp;

		if($number_of_rows == 1){
			$row = mysqli_fetch_assoc($result);
			$temp = $row;
			
			header('Content-type: application/json');
			echo json_encode(array ("status"=>"true",
									"description"=>"update successfully",
									"userlogin"=>$temp));
		}
		else{
	        header('Content-type: application/json');
			echo json_encode(array ("status"=>"false",
									"description"=>"update unsuccessful"));
		}
	}
	else{
		header('Content-type: application/json');
			echo json_encode(array ("status"=>"false",
									"description"=>"update unsuccessful"));
	}
	mysqli_close($connect);

});

$app->post('/getvaccineparamedicinfo',function(Request $request,Response $response){
	$connect = mysqli_connect(hostname,user,password,databaseName);

	$var = $request->getParsedBody();
	$vaccineid = $var["vaccineid"];
	$pid = $var["pid"];

	$vquery = "SELECT * FROM u712147781_vacci.vaccination WHERE vaccination.id = ".$vaccineid.";";
	$resultv = mysqli_query($connect,$vquery) or die(mysqli_error($connect));
	if($pid != 'null'){
		$pquery = "SELECT * FROM u712147781_vacci.paramedics WHERE paramedics.id = ".$pid.";";
		$resultp = mysqli_query($connect,$pquery) or die(mysqli_error($connect));
		if(mysqli_num_rows($resultv) == 1 && mysqli_num_rows($resultp) == 1){
			header('Content-type: application/json');
			echo json_encode($arrayName = array("status" => "true"
												,"vaccine" => mysqli_fetch_assoc($resultv)
												,"pstatus"=> "true"
												,"paramedics" => mysqli_fetch_assoc($resultp)));
		}
		else{
			header('Content-type: application/json');
			echo json_encode($arrayName = array("status" => "false"));
		}

	}
	else{
		if(mysqli_num_rows($resultv) == 1){
			header('Content-type: application/json');
			echo json_encode($arrayName = array("status" => "true"
												,"vaccine" => mysqli_fetch_assoc($resultv)
												,"pstatus"=> "false"
												,"paramedics" => "null"));
		}
		else{
			header('Content-type: application/json');
			echo json_encode($arrayName = array("status" => "false"));
		}
	}
});

$app->post('/updatephone',function(Request $request,Response $response){
	$connect = mysqli_connect(hostname,user,password,databaseName);

	$var =  $request->getParsedBody();

	$username = $var["username"];
	$phone= $var["phone"];

	
	$query = "UPDATE  u712147781_vacci.user SET  phone_number =  '".$phone."' WHERE  user.username =  '".$username."'; ";
	$result = mysqli_query($connect,$query) or die(mysqli_error($connect));
	if($result ==1){
		$query = "SELECT * FROM  u712147781_vacci.user WHERE user.username = '$username' ";

		$result = mysqli_query($connect,$query) or die(mysqli_error($connect));
		$number_of_rows = mysqli_num_rows($result);

		$temp;

		if($number_of_rows == 1){
			$row = mysqli_fetch_assoc($result);
			$temp = $row;
			
			header('Content-type: application/json');
			echo json_encode(array ("status"=>"true",
									"description"=>"update successfully",
									"userlogin"=>$temp));
		}
		else{
	        header('Content-type: application/json');
			echo json_encode(array ("status"=>"false",
									"description"=>"update unsuccessful"));
		}
	}
	else{
		header('Content-type: application/json');
			echo json_encode(array ("status"=>"false",
									"description"=>"update unsuccessful"));
	}
	mysqli_close($connect);

});
$app->post('/fcmtokenaupdate',function(Request $request,Response $response){

	$connect = mysqli_connect(hostname,user,password,databaseName);

	$var =  $request->getParsedBody();

	$username = $var["username"];
	$token = $var["token"];
	
	$query = "UPDATE  u712147781_vacci.user SET fcmtoken =  '".$token."' WHERE  username =  '".$username."';";
	$result = mysqli_query($connect,$query) or die(mysql_error($connect));

   if($result==1){
		header(
			'Content-type : application/json'
		);
		echo json_encode($arrayName = array(
			"result" => "success",
			"description" => "token is updated"
		 ));
	}
	else{
		header(
			'Content-type : application/json'
		);
		echo json_encode($arrayName = array(
			"result" => "unsuccess",
			"description" => "error :".$result
		 ));
	}
	mysqli_close($connect);

});

$app->post('/neworder',function(Request $request,Response $response){
	$connect = mysqli_connect(hostname,user,password,databaseName);

	$var =  $request->getParsedBody();

	$vaccine_id = $var["vaccineid"];
	$vaccine_name = $var["vaccinename"];
	$date = $var["date"];
	$address = $var["address"];
	$price = $var["price"];
	$username = $var["username"];
	$status = "pending";

	$query = "INSERT INTO u712147781_vacci.home_vaccine (vaccine_id, p_id, vaccine_name, date, address, price, username, status) VALUES ('".$vaccine_id."', NULL, '".$vaccine_name."', '".$date."', '".$address."', '".$price."', '".$username."', '".$status."');";

	$result = mysqli_query($connect,$query) or die(mysql_error($connect));
	

	if($result==1){
		header('Content-type: application/json');
		echo json_encode($arrayName = array(
			"status"=>"success",
			"description"=>"order is successful"
			));
	}
	else{
		header('Content-type: application/json');
		echo json_encode($arrayName = array(
			"status"=>"unsuccess",
			"description"=>"order is unsuccessful"
			));
	}

	mysqli_close($connect);

});

$app->post('/getordered',function(Request $request,Response $response){
	
	$connect = mysqli_connect(hostname,user,password,databaseName);

	$var =  $request->getParsedBody();

	$username = $var["username"];
	
	$query = "SELECT * FROM u712147781_vacci.home_vaccine WHERE home_vaccine.username='".$username."';";

	$result = mysqli_query($connect,$query) or die(mysqli_error($connect));
	
	$number_of_rows = mysqli_num_rows($result);

    if($number_of_rows>0){
		$orderedlist = array();
		while($row = mysqli_fetch_assoc($result))
		{
			$orderedlist[] = $row;
		}
		header('Content-type: application/json');
		echo json_encode($orderedlist);
	}
	else{
		header('Content-type: application/json');
		echo json_encode($arrayName = array(
			"status"=>"something went wrong"
			));
	}
	mysqli_close($connect);

});

$app->get('/vaccinations',function (Request $request, Response $response){

	$connect = mysqli_connect(hostname,user,password,databaseName);

	$query = "SELECT * FROM u712147781_vacci.vaccination";
	$result = mysqli_query($connect,$query) or die(mysqli_error($connect));

	$number_of_rows = mysqli_num_rows($result);

    if($number_of_rows>0){
		$emparray = array();
		while($row = mysqli_fetch_assoc($result))
	    {
	 		$emparray[] = $row;
	    }
	    header('Content-type: application/json');
		echo json_encode($arrayName = array(
			"vaccinationlist" =>  $emparray
			));
	}
	else{
		header('Content-type: application/json');
		echo json_encode($arrayName = array(
			"status"=>"something went wrong",
			"description" =>  "no vaccination found"
			));
	}
    //close the db connection
	mysqli_close($connect);
});


$app->post('/login',function(Request $request,Response $response){
	$connect = mysqli_connect(hostname,user,password,databaseName);

	$var =  $request->getParsedBody();

	$username = $var["username"];
	
	$query = "SELECT * FROM  u712147781_vacci.user WHERE user.username = '$username' ";
	$result = mysqli_query($connect,$query) or die(mysqli_error($connect));
	$number_of_rows = mysqli_num_rows($result);

	$temp;

	if($number_of_rows == 1){
		$row = mysqli_fetch_assoc($result);
		$temp = $row;
		
		header('Content-type: application/json');
		echo json_encode(array ("status"=>"true",
								"description"=>"user found",
								"userlogin"=>$temp));
	}
	else{
        header('Content-type: application/json');
		echo json_encode(array ("status"=>"false",
								"description"=>"user not found"));
	}
	
	mysqli_close($connect);

});

$app->post('/signup',function (Request $request, Response $response){

	$connect = mysqli_connect(hostname,user,password,databaseName);
	$var = $request->getParsedBody();

	$username = $var["username"];
	$fullname = $var["fullname"];
	$birthdate= $var["birthdate"];
	$email_id = $var["email_id"];
	$phone_number = $var["phone_number"];
	$password = $var["password"];	

	$query1 = "SELECT username FROM u712147781_vacci.user WHERE username = '".$username."'";

	$result1 = mysqli_query($connect,$query1)  or die(mysqli_error($connect));

	$number_of_rows1 = mysqli_num_rows($result1);

	if($number_of_rows1 ==1){
		header(
				'Content-type : application/json'
			);
			echo json_encode($arrayName = array(
				"result" => "unsuccess",
				"description" => "username already exist"
			 ));
	}
	else{		
		$query = "INSERT INTO u712147781_vacci.user (user.username, user.fullname, user.birthdate, user.email_id, user.phone_number, user.password) VALUES ('".$username."', '".$fullname."', '".$birthdate."', '".$email_id."', '".$phone_number."', '".$password."');";

		$result=mysqli_query($connect,$query) or die(mysqli_error($connect));

		if($result==1){
			header(
				'Content-type : application/json'
			);
			echo json_encode($arrayName = array(
				"result" => "success",
				"description" => "users signup successfully",
				"userSignUp" => $var
			 ));
		}
		else{
			header(
				'Content-type : application/json'
			);
			echo json_encode($arrayName = array(
				"result" => "unsuccess",
				"description" => "error :".$result
			 ));
		}
	}
 	mysqli_close($connect);
});

$app->post('/upcomingvaccination/{username}',function (Request $request, Response $response){

	$connect = mysqli_connect(hostname,user,password,databaseName);

	$name = $request->getAttribute('username');

	$query = "SELECT user.birthdate,user.username,user.fcmtoken FROM u712147781_vacci.user WHERE user.username='".$name."'";

	$result = mysqli_query($connect,$query) or die(mysql_error($connect));

	$number_of_rows = mysqli_num_rows($result);

	if($number_of_rows >0){
		while($row = mysqli_fetch_assoc($result))
		{
			$userbirthdate = $row['birthdate'];
			$username = $row['username'];
			$userfcmtoken = $row['fcmtoken'];

			date_default_timezone_set("Asia/Kolkata");
			$current = date("Y-m-d");
			//difference
			$diff = abs(strtotime($current) - strtotime($userbirthdate));
			//calculate the days
			$days = floor(($diff)/ (60*60*24));

			$query1 = "SELECT  vaccination.timestamp,vaccination.id,vaccination.vaccination_name FROM  u712147781_vacci.vaccination WHERE timestamp > ".$days." LIMIT 1;";

			$result1 = mysqli_query($connect,$query1) or die(mysql_error($connect));

			$number_of_rows1 = mysqli_num_rows($result1);


			if($number_of_rows1 == 1){
				$rows1 = mysqli_fetch_assoc($result1);

				$timestamp = $rows1['timestamp'];
				
				$vaccination_name = $rows1['vaccination_name'];

				$daysdiff = $timestamp - $days;

				$jsondata = array("vaccination"=>$vaccination_name,
									"dayleft"=>$daysdiff);
				header(
					'Content-type : application/json'
						);
				echo json_encode($jsondata);
			}
		}
	}	
 	mysqli_close($connect);
});


$app->run();