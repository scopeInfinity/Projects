<!DOCTYPE html>
<html>
<head>
	<title>ICPC</title>
</head>
<body>
<?php
$data = null;
$fname = "icpc.dat";
if(file_exists($fname)) {
	$con = file_get_contents($fname);
	if(sizeof($con)!=0)
	$data = json_decode($con,true);
}
if(is_null($data)) {
	$data  = array();
}
// $data['ICPC Onsite Regionals Kolkata 2016'] = array('start' => 0, 'code' => 'ACM16KOL', 'status' => [0,0,0,0,0,0,0,0,0,0,0,0],'fail' => [0,0,0,0,0,0,0,0,0,0,0,0],'live' => false );
// $data['ICPC Onsite Regionals KGP 2016']  = array('start' => 0, 'code' => 'KGP16', 'status' => [0,0,0,0,0,0,0,0,0,0,0],'fail' => [0,0,0,0,0,0,0,0,0,0,0],'live' => false );
// $data['ICPC Onsite Regionals Kanpur 2013']  = array('start' => 0, 'code' => 'KAN13', 'status' => [0,0,0,0,0,0,0,0,0], 'fail' => [0,0,0,0,0,0,0,0,0],'live' => false );
// $data['ICPC Onsite Regionals KGP 2013']  = array('start' => 0, 'code' => 'KGP13', 'status' => [0,0,0,0,0,0,0,0,0,0,0],'fail' => [0,0,0,0,0,0,0,0,0,0,0],'live' => false );
// file_put_contents($fname, json_encode($data));

if(isset($_GET['code'])) {
	$name = $_GET['code'];
	if(isset($_GET['start'])) {
		if($data[$name]['start']==0) {
			$data[$name]['start'] = microtime(true);
			header("LOCATION: /");	
			file_put_contents($fname, json_encode($data));
			exit();
		}
	}
	if($data[$name]['start']==0) {
		echo $name." Competition Not Started!!!";
	} else {
		$problem = (int)$_GET['problem'];
		if(isset($_GET['accept'])) {
			if($data[$name]['status'][$problem]==0 || $_GET['accept']=='force')
				$data[$name]['status'][$problem] = microtime(true);
		}
		if(isset($_GET['fail'])) {
			// echo $data[$name]['fail'][$problem];
			if($data[$name]['status'][$problem]==0 || $_GET['accept']=='force')
				$data[$name]['fail'][$problem] += 1;
		}
		header("LOCATION: /");	
		file_put_contents($fname, json_encode($data));
		exit();
	}
}

?>
<style type="text/css">
	.on {
		background-color: rgb(200,255,200);
		padding: 0.5em;
	}
	.off {
		background-color: rgb(240,240,200);
		padding: 0.5em;
	}
	a {
		color: rgb(30,30,30);
	}
	.success {
		color: rgb(0,150,0);
	}
	.fail {
		color: rgb(220,0,0);
	}
	form {
    display: inline-block; //Or display: inline; 
	}
</style>
<center><h1>CrazyDot</h1></center>
<?php
foreach ($data as $name => $val) {
	$class = "off";
	if($val['start']>0)
		$class = "on";
	?>
	<div class="<?php echo $class;?>">
	<h2><?php echo $name; ?></h2> &nbsp;<br>
	<form action="#">
		<input type="hidden" name="code" value="<?php echo $name;?>">
		<input type="hidden" name="start" value="1">
		<input type="submit" value="Start">
	</form>
	<br>
	
	<?php
	for($i = 0;$i<count($val['status']);$i+=1) {
		$pcodeA=$val['code'] . chr($i + ord('A'));
		$pname = chr($i + ord('A')) ."  (". ($val['fail'][$i]). ") ";
		$pclass = "none";
		if($val['status'][$i]!=0) {
			$pclass = "success";
			$sec =round($val['status'][$i] - $val['start']) ;
			$pname  .= "  " . floor($sec/60) . "min " . ($sec%60) . "sec";
		} else if($val['status'][$i]==0 && $val['fail'][$i]>0) {
			$pclass = "fail";
		}
		// $sz = 5;
		// if($i%$sz == 0)
		// 	echo "<br>";
	?>
	<a href="https://www.codechef.com/problems/<?php echo $pcodeA; ?>" class="<?php echo $pclass;?> ">Problem <?php echo $pname; ?> </a>
	&nbsp;&nbsp;&nbsp;&nbsp;<form action="#">
		<input type="hidden" name="code" value="<?php echo $name;?>">
		<input type="hidden" name="problem" value="<?php echo $i;?>">
		<input type="hidden" name="accept" value="1">
		<input type="submit" value="AC">
	</form>
	<form action="#">
		<input type="hidden" name="code" value="<?php echo $name;?>">
		<input type="hidden" name="problem" value="<?php echo $i;?>">
		<input type="hidden" name="fail" value="1">
		<input type="submit" value="WA/TLE/RTE">
	</form>
	<br>
	<?php
	}
	?>
	 &nbsp;
	</div>
	<?php
}
?>

</body>
</html>