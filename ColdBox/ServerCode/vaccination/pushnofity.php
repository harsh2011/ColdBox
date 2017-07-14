<?php

define('hostname','mysql.hostinger.in');
define('user','u712147781_vacci');
define('password','12345678');
define('databaseName','u712147781_vacci');

$connect = mysqli_connect(hostname,user,password,databaseName);

$query = "SELECT user.birthdate,user.username,user.fcmtoken FROM u712147781_vacci.user";

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

		echo $userbirthdate;
		echo $username;
		printf("%d",$days);

		$query1 = "SELECT  vaccination.timestamp,vaccination.id,vaccination.vaccination_name FROM  u712147781_vacci.vaccination WHERE timestamp > ".$days." LIMIT 1;";

		$result1 = mysqli_query($connect,$query1) or die(mysql_error($connect));

		$number_of_rows1 = mysqli_num_rows($result1);


		if($number_of_rows1 == 1){
			$rows1 = mysqli_fetch_assoc($result1);

			$timestamp = $rows1['timestamp'];
			echo $timestamp;
			$vaccination_name = $rows1['vaccination_name'];

			$daysdiff = $timestamp - $days;

			if($daysdiff<10){
				echo $daysdiff;
				sendnotification($userfcmtoken,$vaccination_name,$daysdiff);
			}
		}
	}
}
	
function sendnotification($fcmtoken,$vaccination_name,$daysdiff){

	echo $fcmtoken;
	echo "<br>";

	$rvalue = send($fcmtoken,$vaccination_name." after ".$daysdiff." days");

	echo json_encode($rvalue);

}

// sending push message to single user by gcm registration id
function send($to, $message) {

	$messagearray = array("message"=> $message,
		"other"=>"hii");

    $fields = array(
        'to' => $to,
        'data' => $messagearray
    );
    return sendPushNotification($fields);
}

// function makes curl request to gcm servers
function sendPushNotification($fields) {

    // Set POST variables
    $url = 'https://fcm.googleapis.com/fcm/send';

    $headers = array(
        'Authorization:key=AAAA-Tcar1o:APA91bF1SL0DWazUw4w1896cWhJPXZ3yOJDPM2XZvNWn-gYvjw6PhrsmEjrscOP3Eoqj3FSWLU66PA1ZFjrw94SarTGKTNmMOqaYmTUa_CylmCj4i2KtRxy3-mUJ8oXVPw7R2uzLumgT',
        'Content-Type:application/json'
    );
    // Open connection
    $ch = curl_init();

    // Set the url, number of POST vars, POST data
    curl_setopt($ch, CURLOPT_URL, $url);

    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

    // Disabling SSL Certificate support temporarly
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);

    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));

    // Execute post
    $result = curl_exec($ch);
    if ($result === FALSE) {
        die('Curl failed: ' . curl_error($ch));
    }

    // Close connection
    curl_close($ch);

    return $result;
}

?>